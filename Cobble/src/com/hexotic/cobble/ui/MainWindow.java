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
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.hexotic.cobble.constants.Constants;
import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.cobble.ui.startPanel.StartPanel;
import com.hexotic.cobble.ui.startPanel.StartupListener;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.util.WinOps;

/**
 * 
 * Class: MainWindow.java Date: 2/14/2015
 * 
 * Description: Main Window creates sub controls and builds the main window
 * 
 * @author Bradley Sheets
 * 
 *         Copyright Hexotic Software. All Rights Reserved
 * 
 */
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 5092527045208783765L;

	private List<JInternalFrame> frames;
	
	private JInternalFrame startupPanelFrame;
	private JInternalFrame mainPanel;

	public MainWindow() {
		Log.getInstance().debug(this, Constants.APPLICATION_NAME + " " + Constants.APPLICATION_VERSION + " - " + Constants.APPLICATION_COMPANY);
		frames = new ArrayList<JInternalFrame>();
		this.setTitle(Constants.APPLICATION_NAME + " " + Constants.APPLICATION_VERSION + " - " + Constants.APPLICATION_COMPANY);
		this.setPreferredSize(Theme.MAIN_WINDOW_DIMENSION);
		this.setMinimumSize(Theme.MAIN_WINDOW_DIMENSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			this.setIconImage(Resources.getInstance().getImage("logo/logo4.png"));
		} catch (ResourceException e) {
			Log.getInstance().error(this, "Couldn't Load Image Resource From Jar for software Icon", e);
		}

		// Setup Desktop Pane/Internal Windows
		JDesktopPane desktop = new JDesktopPane();
		this.setContentPane(desktop);
		desktop.setBackground(Color.BLACK);

		startupPanelFrame = createStartupMenu();
		mainPanel = createMain();
		desktop.add(startupPanelFrame);
		desktop.add(mainPanel);

		pack();
		bindSizing();
		WinOps.centreWindow(this);
		this.setVisible(true);
		Log.getInstance().debug(this, "Main Window Intialized and Visible");

		//startServer();
	}

	private void bindSizing() {
		this.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {

				int targetWidth = e.getComponent().getWidth() - 16;
				int targetHeight = e.getComponent().getHeight() - 38;
				for (JInternalFrame frame : frames) {
					frame.setSize(targetWidth, targetHeight);
					frame.setLocation(0, 0);
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

	private void bindFrameToParent(JInternalFrame frame) {
		frames.add(frame);
	}

	private JInternalFrame createStartupMenu() {
		JInternalFrame startupPanel = new JInternalFrame();

		startupPanel.setSize(Theme.MAIN_WINDOW_DIMENSION.width, Theme.MAIN_WINDOW_DIMENSION.height);
		startupPanel.setBorder(BorderFactory.createEmptyBorder());

		StartPanel startPanel = new StartPanel();
		startupPanel.add(startPanel);
		startPanel.addStarupListener(new StartupListener(){
			@Override
			public void serverSelected(String server) {
				if(server.isEmpty()){
					startupPanelFrame.setVisible(true);
					mainPanel.setVisible(false);
				} else {
					startupPanelFrame.setVisible(false);
					mainPanel.setVisible(true);
				}
			}
		});

		// Remove the title bar
		((BasicInternalFrameUI) startupPanel.getUI()).setNorthPane(null);

		// Log.getInstance().debug(this, "Main Window Created");

		startupPanel.setVisible(true);
		bindFrameToParent(startupPanel);
		return startupPanel;
	}
	
	private JInternalFrame createMain() {
		JInternalFrame main = new JInternalFrame();

		main.setSize(Theme.MAIN_WINDOW_DIMENSION.width, Theme.MAIN_WINDOW_DIMENSION.height);
		main.setBorder(BorderFactory.createEmptyBorder());

		main.add(new MainPanel());

		// Remove the title bar
		((BasicInternalFrameUI) main.getUI()).setNorthPane(null);

		// Log.getInstance().debug(this, "Main Window Created");

		main.setVisible(false);
		bindFrameToParent(main);
		return main;
	}

}
