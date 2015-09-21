package com.hexotic.cobble.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;

import com.hexotic.cobble.utils.Log;

public class Server {

	private static Server instance = null;
	
	public static final int CACHE_PREV = 0;
	public static final int CACHE_NEXT = 1;
	public static final int CACHE_SIZE = 10;
	
	private File serverJar;

	private List<String> commandCache;
	private int cacheIndex = 1;
	private List<ServerListener> listeners;

	/* The process in which the minecraft server is executed in */
	private Process proc;
	
	private String line;

	private BufferedWriter serverInput;
	private BufferedReader serverOutput;
	
	private ServerProperties properties;
	private LevelDat levelDat;
	

	private Server() {
		listeners = new ArrayList<ServerListener>();
		commandCache = new ArrayList<String>();
		levelDat = new LevelDat();
	}
	
	public void setServer(String serverPath) {
		serverJar = new File(serverPath);
		
		// Search for the server.properties file in the serverJar path
		properties = new ServerProperties(serverJar.getParentFile().getPath());
		
		// Try to load level dat file
		
		String levelName = properties.getProperty("level-name");
		levelDat.setDatFile(new File(serverJar.getParentFile().getPath()+"\\"+levelName+"\\level.dat"));
		
		levelDat.start();
	}


	public void startup() {
		try {
			// Setup the minecraft server process
			ProcessBuilder builder = new ProcessBuilder("java", "-Xmx1024M", "-Xms1024M", "-jar",  serverJar.getAbsolutePath(), "nogui");
			builder.directory( serverJar.getParentFile()); // this is where you set the root folder for the executable to run with
			builder.redirectErrorStream(true);
			proc =  builder.start();
			
			
			// Setup a thread to execute when the program is terminated. 
			Thread closeChildThread = new Thread() {
				public void run() {
					// Tell the server to save and stop the world
					send("stop");
					
					// Give a few seconds for minecraft to get it's shit together
					try {
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						Log.getInstance().error(this, "Failed to wait a few seconds after stop command", e);
					}
					// If the process hasn't been killed, kill it
					if(proc.isAlive()){
						proc.destroy();
					}
					// Wait for the process to die
					try {
						proc.waitFor();
					} catch (InterruptedException e) {
						Log.getInstance().error(this, "Couldn't wait on process", e);
					}
					
					// Log the exit value.  Successful should be 0
					Log.getInstance().debug(this, "Minecraft Server Terminated with exit code: " + proc.exitValue());
				}
			};
			
			Runtime.getRuntime().addShutdownHook(closeChildThread);
			
			// serverInput opens a stream in which commands can be sent to the server
			serverInput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
			
			// serverOutput opens up a stream in which server output can be read
			serverOutput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			
			// Start a thread that will continuously read data from the minecraft server (serverOutput)
			new Thread(new Runnable() {
		        public void run() {
		        	try {
						while ((line = serverOutput.readLine()) != null) {
							Log.getInstance().debug(this, line);
							notifyListeners(line);
						}
					} catch (IOException e) {
						Log.getInstance().error(this, "Failed to read minecraft server output", e);
					}
		        }
		    }).start();
			
		} catch (IOException e) {
			Log.getInstance().error(this, "Failed To Launch Minecraft Server :(", e);
		}

		
	}
	
	public LevelDat getLevelDat() {
		return levelDat;
	}

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public void send(String command) {
		Log.getInstance().debug(this, "Sending Command: "+command);
		try {
			if(proc.isAlive()){
				serverInput.write(command);
				serverInput.newLine();
				serverInput.flush();
			}
			cache(command);
		} catch (IOException e) {
			Log.getInstance().error(this, "Failed to send command to minecraft server", e);
		}
	}
	
	private void cache(String cache){
		if(commandCache.size() > CACHE_SIZE){
			commandCache.remove(0);
		}
		commandCache.add(cache);
		cacheIndex = commandCache.size()+1;
		
	}
	
	public String getCache(int direction){
		String cache = "";
		if(cacheIndex > 1 && commandCache.size() > 0 && direction == CACHE_PREV){
			cacheIndex--;
		} else if (cacheIndex <= CACHE_SIZE && cacheIndex < commandCache.size()  && direction == CACHE_NEXT){
			cacheIndex++;
		}
		if(commandCache.size() >= cacheIndex){
			cache = commandCache.get(cacheIndex-1);
		}
		return cache;
	}

	public void notifyListeners(String line) {
		for (ServerListener listener : listeners) {
			listener.outputRecieved(line);
		}
	}

	public void addListener(ServerListener listener) {
		listeners.add(listener);
	}
	
	public String parseLine(String line){
		return line.split(":")[3].trim();
	}
	
}
