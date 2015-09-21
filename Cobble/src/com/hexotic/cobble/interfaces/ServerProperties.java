package com.hexotic.cobble.interfaces;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hexotic.cobble.utils.Log;

public class ServerProperties {

	private static final String PROPERTIES_FILE = "server.properties";
	
	private Properties properties;
	
	public  ServerProperties(String serverPath) {
		loadProperties(serverPath);
	}
	
	private void loadProperties(String serverPath){
		properties = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(serverPath +"\\"+PROPERTIES_FILE);

			// load a properties file
			properties.load(input);

		} catch (IOException ex) {
			Log.getInstance().error(this, "Couldn't Load Server Properties file for minecraft server", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) { }
			}
		}
		
	}
	
	public String getProperty(String property){
		return properties.getProperty(property, "");
	}
	
}
