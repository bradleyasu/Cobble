package com.hexotic.cobble.ui.components.panels;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Constants;
import com.hexotic.cobble.constants.Theme;
import com.hexotic.lib.resource.Resources;

public class AboutPanel extends JPanel{

	private Image background;
	
	public AboutPanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		background = Resources.getInstance().getImage("minecraft/sheep_1.png");
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font fontOne = null;
		Font fontTwo = null;
		try {
			fontOne = Resources.getInstance().getFont("RobotoCondensed-Light.ttf").deriveFont(56F);
			fontTwo = Resources.getInstance().getFont("RobotoCondensed-Light.ttf").deriveFont(24F);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g2d.setFont(fontOne);
		g2d.drawString(Constants.APPLICATION_NAME, 100, 100);
		
		g2d.setFont(fontTwo);
		g2d.drawString(Constants.APPLICATION_VERSION, 260, 100);
		
		
		g2d.drawImage(background, getWidth() - background.getWidth(null) - 40, getHeight()/2-background.getHeight(null)/2, background.getWidth(null), background.getHeight(null), null);
		
	}
	
}
