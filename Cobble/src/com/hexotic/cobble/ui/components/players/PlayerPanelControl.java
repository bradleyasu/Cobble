package com.hexotic.cobble.ui.components.players;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.lib.switches.BasicSwitch;

public class PlayerPanelControl extends JPanel{
	
	
	public PlayerPanelControl() {
		this.setPreferredSize(Theme.CONSOLE_INPUT_DIMENSION);
		this.setBackground(Theme.MAIN_BACKGROUND);
		BasicSwitch bs = new BasicSwitch("all","online", 120, 25, 0);
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bs.setBackground(Theme.MAIN_COLOR_FIVE);
		bs.setForeground(Theme.MAIN_FOREGROUND);
		
		this.add(bs, BorderLayout.EAST);
		
		
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	}

}
