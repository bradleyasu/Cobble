package com.hexotic.cobble.constants;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Class: Theme.java
 * Date: 2/14/2015
 * 
 * Description: Theme class defines the look and feel of the application.  Colors, Fonts, and Sizes will be defined here
 *  
 * @author Bradley Sheets
 * 
 * Copyright Hexotic Software.  All Rights Reserved
 * 
 */
public final class Theme {
	
	// Window Dimensions
	public static final Dimension MAIN_WINDOW_DIMENSION = new Dimension(980,640);
	public static final Dimension CONSOLE_DIMENSION = new Dimension(850,400);
	public static final Dimension CONSOLE_INPUT_DIMENSION = new Dimension(750,30);
	
	// Fonts
	
	
	// Colors Themes
	public static final Color MAIN_FOREGROUND = new Color(0x333333);
	public static final Color MAIN_BACKGROUND = new Color(0xf2f2f2);
	public static final Color CONSOLE_FOREGROUND = new Color(0xFFFFFF);
	public static final Color CONSOLE_BACKGROUND = new Color(0x333333);
	public static final Color MAIN_COLOR_ONE = new Color(0x007D9D);
	public static final Color MAIN_COLOR_TWO = new Color(0x7DB436);
	public static final Color MAIN_COLOR_THREE = new Color(0x62882D);
	public static final Color MAIN_COLOR_FOUR = new Color(0x009ECB);
	public static final Color MAIN_COLOR_FIVE = new Color(0x8ECA40);
	public static final Color MAIN_COLOR_SIX = new Color(0xE3AAD3);
	public static final Color CLEAR = new Color(0,0,0,0);
	

	private Theme() {
		/* We do not need to do anything here */
	}
}
