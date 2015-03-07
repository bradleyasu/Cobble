package com.hexotic.cobble.interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.hexotic.cobble.utils.Log;

public class Server {

	private static Server instance = null;
	private File serverJar;

	private List<ServerListener> listeners;

	/* The process in which the minecraft server is executed in */
	private Process proc;
	
	private String line;

	private BufferedWriter serverInput;
	private BufferedReader serverOutput;
	
	private Server() {
		listeners = new ArrayList<ServerListener>();
		serverJar = new File("D:\\games\\minecraft_server.1.8.jar");

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

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public void send(String command) {
		Log.getInstance().debug(this, "Sending Command: "+command);
		try {
			serverInput.write(command);
			serverInput.newLine();
			serverInput.flush();
		} catch (IOException e) {
			Log.getInstance().error(this, "Failed to send command to minecraft server", e);
		}
	}


	public void notifyListeners(String line) {
		for (ServerListener listener : listeners) {
			listener.outputRecieved(line);
		}
	}

	public void addListener(ServerListener listener) {
		listeners.add(listener);
	}

}
