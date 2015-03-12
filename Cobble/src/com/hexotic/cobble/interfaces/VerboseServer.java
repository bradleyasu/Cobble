package com.hexotic.cobble.interfaces;

public class VerboseServer {
	private static VerboseServer instance = null;

	private VerboseServer() {

	}

	public void simulatePlayerJoined(String player) {
		Server.getInstance().notifyListeners(":::"+player + " joined the game");
	}

	public void simulatePlayerLeft(String player) {
		Server.getInstance().notifyListeners(":::"+player + " left the game");
	}

	public static VerboseServer getInstance() {
		if (instance == null) {
			instance = new VerboseServer();
		}
		return instance;
	}
}
