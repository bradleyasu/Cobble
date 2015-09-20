package com.hexotic.cobble.utils;

import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.VerboseServer;

public class CobbleCommand {

	private CobbleCommand() {
		
	}
	

	public static void execute(String command){
		if("start".equals(command)){
			Server.getInstance().startup();
		} else if("exit".equals(command)){
			try{
				Server.getInstance().send("stop");
			}catch (NullPointerException e){/*Server isn't running*/}
			System.exit(0);
		}
		
		
		if(command.startsWith("simulate-playerJoin:")){
			VerboseServer.getInstance().simulatePlayerJoined(command.split(":")[1]);
		}
		
		if(command.startsWith("simulate-playerLeft:")){
			VerboseServer.getInstance().simulatePlayerLeft(command.split(":")[1]);
		}
	}
	
}
