package com.hexotic.cobble.ui.components.panels.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.LevelDat;
import com.hexotic.cobble.interfaces.LevelDatListener;
import com.hexotic.cobble.interfaces.Server;

public class ServerHomePanel extends JPanel {

	public ServerHomePanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		Server.getInstance().getLevelDat().addLevelDatListener(new LevelDatListener() {
			@Override
			public void updated() {
				// Level dat was updated, so update the UI
				refresh();
			}
		});
	}

	private void refresh() {
		this.revalidate();
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		LevelDat dat = Server.getInstance().getLevelDat();
		
		g2d.drawString(dat.getLevelName(), 20, 20);
		g2d.drawString("Current Time: " + Server.getInstance().getLevelDat().getCurrentTime(), 20, 40);
		g2d.drawString("Seed: " + Server.getInstance().getLevelDat().getSeed(), 20, 60);
		g2d.drawString("Game Type: " + Server.getInstance().getLevelDat().getGameType(), 20, 80);

	}
}
