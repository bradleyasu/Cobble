package com.hexotic.cobble.constants;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;

public class Fonts {

	private static Fonts instance = null;
	
	private Font playerFont;
	
	private Fonts() {
		
		try {
			playerFont = Resources.getInstance().getFont("RobotoCondensed-Bold.ttf").deriveFont(16F);
		} catch (FontFormatException | IOException e) {
			Log.getInstance().error(this, "Failed to load player font", e);
			playerFont = new Font("Arial", Font.BOLD, 16);
		}
		
	}
	
	public Font getPlayerFont() {
		return playerFont;
	}
	
	
	public static Fonts getInstance() {
		if (instance == null){
			instance = new Fonts();
		}
		return instance;
	}
}
