package com.hexotic.cobble.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.ui.components.LogPanel;
import com.hexotic.cobble.ui.components.ServerControlPanel;
import com.hexotic.lib.ui.panels.FlipPanel;

public class MainPanel extends JPanel{

	private FlipPanel flipper;
	
	public MainPanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BorderLayout());
		
		ServerControlPanel front = new ServerControlPanel();
		LogPanel back = new LogPanel();
		
		
		FlipListener listener = new FlipListener(){
			@Override
			public void toggleFlip() {
				flipper.flip();
			}
		};
		
		front.addFlipListener(listener);
		back.addFlipListener(listener);
		
		
		flipper = new FlipPanel(front, back);
		flipper.setDirection(FlipPanel.UP);
		this.add(flipper);
		
	}
}
