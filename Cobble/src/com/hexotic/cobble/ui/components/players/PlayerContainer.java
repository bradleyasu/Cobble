package com.hexotic.cobble.ui.components.players;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.lib.ui.layout.AnimatedGridLayout;

public class PlayerContainer extends JPanel{

	private Map<String, Player> players;
	
	public PlayerContainer() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new AnimatedGridLayout(true));
		this.players = new HashMap<String, Player>();
	}
	
	
	public void addPlayer(Player player) {
		players.put(player.getName(), player);
		this.add(player);
		refresh();
	}
	
	public void refresh() {
		this.revalidate();
		this.repaint();
	}
	
	public void addPlayer(String playerName) {
		if(players.containsKey(playerName)){
			players.get(playerName).setVisible(true);
		} else {
			addPlayer(new Player(playerName));
		}
	}
	
	public void removePlayer(String playerName) {
		players.get(playerName).setVisible(false);
	}
	
}
