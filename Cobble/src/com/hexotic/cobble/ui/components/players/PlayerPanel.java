package com.hexotic.cobble.ui.components.players;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.cobble.constants.Constants;
import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.lib.ui.panels.SimpleScroller;

public class PlayerPanel extends JPanel {

	private PlayerContainer playerContainer;

	public PlayerPanel() {
		// this.setLayout(new AnimatedGridLayout(true));
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
					String playerName = Server.getInstance().parseLine(output).split(" ")[0];
					
					Server.getInstance().send("title "+playerName+" title {\"text\":\"The Journey Continues...\",\"bold\":\"false\",\"color\":\"white\"}");
					Server.getInstance().send("tellraw "+playerName+" {\"text\":\"Welcome! You've been enchanted with ABSORPTION.  Take advantage of this gift from the GODS!\",\"bold\":\"true\",\"color\":\"aqua\"}");
					Server.getInstance().send("effect "+playerName+" 22");
					if(!"MarnBeast".equals(playerName)){
						Server.getInstance().send("give "+playerName+" minecraft:golden_apple");
					}
					playerContainer.addPlayer(playerName);
				} else if (output.contains("left the game")) {
					String playerName = Server.getInstance().parseLine(output).split(" ")[0];
					playerContainer.removePlayer(playerName);
				}
			}
		});
	}
}
