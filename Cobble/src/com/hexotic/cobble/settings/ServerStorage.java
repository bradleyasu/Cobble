package com.hexotic.cobble.settings;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.ini4j.Wini;

import com.hexotic.cobble.utils.Log;

public class ServerStorage {

	public static final String SERVER_FILE="server.storage";
	public static ServerStorage instance = null;
	
	private Wini storage = null;
	
	// List of all saved server names with paths to server executable location
	private Map<String, String> serverList;
	
	private ServerStorage() {
		try {
			File outFile = new File(SERVER_FILE);
			if(!outFile.exists()){
				outFile.createNewFile();
			}
			storage = new Wini(outFile);
		} catch (IOException e) {
			Log.getInstance().error(this, "Failed to create server storage!", e);
		}
		serverList = new TreeMap<String, String>();
		
		loadServers();
	}
	
	public void loadServers() {
		for(String serverName : storage.keySet()){
			serverList.put(serverName, storage.get(serverName, "path"));
		}
	}
	
	public Map<String, String> getAllServers() {
		return serverList;
	}
	
	public void saveServer(String serverName, String serverLocation) throws IOException {
		storage.put(serverName, "path", serverLocation);
		storage.store();
	}
	
	public static ServerStorage getInstance() {
		if (instance == null){
			instance = new ServerStorage();
		}
		 
		return instance;
	}
}
