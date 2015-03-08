package com.hexotic.cobble.ui.components.menupanel;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;

public class MenuPanel extends JPanel{
	
	private Font itemFont;
	
	private List<MenuItem> items;
	
	public MenuPanel() {
		items = new ArrayList<MenuItem>();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBackground(Theme.MAIN_BACKGROUND);
		try {
			itemFont = Resources.getInstance().getFont("RobotoCondensed-Light.ttf").deriveFont(28F);
		} catch (FontFormatException | IOException e) {
			itemFont = new Font("Arial", Font.BOLD, 28);
			Log.getInstance().error(this, "Failed to load Menu Item Font Face", e);
		}
	}

	public void refresh(){
		this.revalidate();
		this.repaint();
	}
	public void resetAll() {
		for(MenuItem item : items){
			item.setSelected(false);
		}
	}
	
	public void addMenuItem(MenuItem item){
		this.add(item);
		items.add(item);
		item.setMenutItemFont(itemFont);
	}
	
}
