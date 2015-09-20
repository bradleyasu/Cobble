package com.hexotic.cobble.ui.components.players;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.hexotic.cobble.constants.Constants;
import com.hexotic.cobble.constants.Fonts;
import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;

public class Player extends JPanel implements Comparable<Player>{

	private static final Dimension SIZE = new Dimension(175, 275);
	
	private String name;
	private Image skin;
	private Image background;
	private int skinWidth = 105;
	private int xoffset = 30;
	private int yoffset = 30;
	private boolean online = false;
	
	
	public Player(String name){
		this.name = name;
		this.setPreferredSize(SIZE);
		
		try {
			background = Resources.getInstance().getImage("player/background/temp.jpg");
		} catch (ResourceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loadSkin();
		} catch (IOException e) {
			Log.getInstance().error(this, "Failed to load player skin", e);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int compareTo(Player player){
		return this.name.compareToIgnoreCase(player.getName());
	}
	
	private void loadSkin() throws IOException {
		URL url = new URL(Constants.SKIN_SERVICE.replace("[player]", name));
		skin = ImageIO.read(url);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(background, 0, 0, getWidth(), getHeight()-50, null);
		
		g2d.setColor(Theme.MAIN_BACKGROUND.darker());
		
		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		g2d.drawLine(0, getHeight() - 50, getWidth()-1, getHeight() - 50);
		
		g2d.setColor(Theme.MAIN_FOREGROUND);
		g2d.setFont(Fonts.getInstance().getPlayerFont());
		g2d.drawString(name, 5, getHeight() - 30);
		
		g2d.drawImage(skin, getWidth()/2 - (skinWidth-xoffset)/2-10, 10, skinWidth+xoffset, getHeight() - 50 - yoffset,
							xoffset, yoffset, skinWidth+xoffset, skin.getHeight(null)-yoffset, null);
	}
	
}
