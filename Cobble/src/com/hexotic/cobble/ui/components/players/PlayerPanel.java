package com.hexotic.cobble.ui.components.players;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.lib.audio.SoundFX;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.panels.SimpleScroller;

public class PlayerPanel extends JPanel {

	private PlayerContainer playerContainer;
	private List<String> seen;

	public PlayerPanel() {
		// this.setLayout(new AnimatedGridLayout(true));
		seen = new ArrayList<String>(); // TODO delete this
		playerContainer = new PlayerContainer();
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BorderLayout(0, 0));

		this.add(new PlayerPanelControl(), BorderLayout.NORTH);

		JScrollPane scroller = new JScrollPane(playerContainer);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setBackground(Theme.MAIN_BACKGROUND);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(8, 8));
		scroller.getVerticalScrollBar().setUnitIncrement(80);
		
		this.add(scroller, BorderLayout.CENTER);

		
		registerListener();
	}

	private void registerListener() {
		Server.getInstance().addListener(new ServerListener() {
			@Override
			public void outputRecieved(String output) {
				if (output.contains("joined")) {
					
					SoundFX.play("audio/login.wav");
					
					String playerName = Server.getInstance().parseLine(output).split(" ")[0];
					
					Server.getInstance().send("tellraw "+playerName+" {\"text\":\"Welcome! You've been enchanted with ABSORPTION.  Take advantage of this gift from the GODS!\",\"bold\":\"false\",\"color\":\"aqua\"}");
					Server.getInstance().send("effect "+playerName+" 22");
					if(!"MarnBeast".equals(playerName)){
						Server.getInstance().send("give "+playerName+" minecraft:golden_apple");
					}
					
					if(!seen.contains(playerName)){
						//Server.getInstance().send("give "+playerName+" minecraft:chainmail_helmet");
						//Server.getInstance().send("give "+playerName+" minecraft:chainmail_chestplate");
						//Server.getInstance().send("give "+playerName+" minecraft:chainmail_leggings");
						//Server.getInstance().send("give "+playerName+" minecraft:chainmail_boots");
						seen.add(playerName);
					}
					
					playerContainer.addPlayer(playerName);
				} else if (output.contains("left the game")) {
					SoundFX.play("audio/logout.wav");
					String playerName = Server.getInstance().parseLine(output).split(" ")[0];
					playerContainer.removePlayer(playerName);
				}
			}
		});
	}
}
