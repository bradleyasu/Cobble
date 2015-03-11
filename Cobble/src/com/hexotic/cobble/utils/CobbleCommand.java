package com.hexotic.cobble.utils;

import com.hexotic.cobble.interfaces.Server;

public class CobbleCommand {

	private CobbleCommand() {
		
	}
	

	public static void execute(String command){
		if("start".equals(command)){
			Server.getInstance().startup();
		}
		
	}
	
}
