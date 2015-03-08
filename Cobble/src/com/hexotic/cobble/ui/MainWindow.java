package com.hexotic.cobble.ui;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.cobble.constants.Constants;
import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.cobble.ui.components.LogPanel;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.util.WinOps;

/**
 * 
 * Class: MainWindow.java 
 * Date: 2/14/2015
 * 
 * Description: Main Window creates sub controls and builds the main window
 * 
 * @author Bradley Sheets
 * 
 *         Copyright Hexotic Software. All Rights Reserved
 * 
 */
public class MainWindow extends JFrame{

	private static final long serialVersionUID = 5092527045208783765L;
	
	private List<JInternalFrame> frames;

	public MainWindow() {
		Log.getInstance().debug(this, Constants.APPLICATION_NAME + " version: " + Constants.APPLICATION_VERSION + " - " + Constants.APPLICATION_COMPANY);
		frames = new ArrayList<JInternalFrame>();
		
		this.setTitle(Constants.APPLICATION_NAME + " version: " + Constants.APPLICATION_VERSION + " - " + Constants.APPLICATION_COMPANY);
		this.setPreferredSize(Theme.MAIN_WINDOW_DIMENSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Setup Desktop Pane/Internal Windows
		JDesktopPane desktop = new JDesktopPane();
		this.setContentPane(desktop);
		desktop.setBackground(Color.BLACK);
		
		desktop.add(createMain());
				
		pack();
		bindSizing();
		WinOps.centreWindow(this);
		this.setVisible(true);
		Log.getInstance().debug(this, "Main Window Intialized and Visible");
		
		startServer();
		
	}
	
	
	private void bindSizing() {
		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				
				int targetWidth = e.getComponent().getWidth() - 16;
				int targetHeight = e.getComponent().getHeight() - 38;
				for(JInternalFrame frame : frames) {
					frame.setSize(targetWidth, targetHeight);
					frame.setLocation(0,0);
				}
			}
			@Override
			public void componentHidden(ComponentEvent e) {
			}
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			@Override
			public void componentShown(ComponentEvent e) {
			}
		});

	}
	
	private void startServer() {
		Server.getInstance().startup();
		Server.getInstance().addListener(new ServerListener(){
			@Override
			public void outputRecieved(String output) {
				if(output.contains("joined")){
					Server.getInstance().send("say Welcome! The server is undergoing maintenance and you may get disconnected now and then.  Sorry :(");
				} 
			}
		});
	}
	
	
	private void  bindFrameToParent(JInternalFrame frame){
		frames.add(frame);
	}
	
	private JInternalFrame createMain() {
		JInternalFrame main = new JInternalFrame();

		main.setSize(Theme.MAIN_WINDOW_DIMENSION.width, Theme.MAIN_WINDOW_DIMENSION.height);
		main.setBorder(BorderFactory.createEmptyBorder());

		main.add(new MainPanel());
		
		// Remove the title bar
		((BasicInternalFrameUI) main.getUI()).setNorthPane(null);

		//Log.getInstance().debug(this, "Main Window Created");
		
		main.setVisible(true);
		bindFrameToParent(main);
		return main;
	}
	
	
}
