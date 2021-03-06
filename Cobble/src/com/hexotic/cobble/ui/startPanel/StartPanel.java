package com.hexotic.cobble.ui.startPanel;

import java.awt.BorderLayout;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.exceptions.ResourceException;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.loaders.ProgressCircle;
import com.hexotic.lib.ui.panels.FlipPanel;

public class StartPanel extends JPanel{

	private StartPanelMenu homeMenu;
	private ProgressCircle startProgress;
	private List<StartupListener> listeners;
	
	public StartPanel() {
		this.setBackground(Theme.MAIN_COLOR_FOUR);
		this.listeners = new ArrayList<StartupListener>();
		
		// Create Layout
		this.setLayout(new GridBagLayout());
		homeMenu = new StartPanelMenu();
		startProgress = new ProgressCircle();
		startProgress.setColor(Theme.MAIN_BACKGROUND, Theme.MAIN_BACKGROUND);
		startProgress.setFontColor(Theme.MAIN_BACKGROUND);
		startProgress.setProgress(100);
		startProgress.showText(false);
		startProgress.cycle();
		this.add(homeMenu);
		
		Server.getInstance().addListener(new ServerListener(){
			@Override
			public void outputRecieved(String output) {
				if(output.contains("Done") && output.contains("For help, type \"help\" or \"?\"")){
					startProgress.setProgress(100);
				} else if(output.contains("Server Shutdown")){
					notifyStartup("");
				}
			}
		});
		
		homeMenu.setVisible(true);
		
	}
	
	public void addStarupListener(StartupListener listener){
		listeners.add(listener);
	}
	
	public void notifyStartup(String server){
		for(StartupListener listener : listeners){
			listener.serverSelected(server);
		}
		if(server.isEmpty()){
			homeMenu.setVisible(true);
		}
	}

	public void loadServer(final String server) {
		homeMenu.setVisible(false);
		Server.getInstance().setServer(server);
		Server.getInstance().startup();
		startProgress.setProgress(5);
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(startProgress.getProgress() < 100){
					try {
						Thread.sleep(75);
					} catch (InterruptedException e) { }
					refresh();
				}
				notifyStartup(server);
			}
		});
		t.start();
	}
	
	private void refresh() {
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		try {
			g2d.drawImage(Resources.getInstance().getImage("minecraft/background_1.png"),0, 0, getWidth(), getHeight(), null);
		} catch (ResourceException e) {
			Log.getInstance().error(this, "Couldn't load startup menu background - WOMP", e);
		}
		
		startProgress.Draw(g2d, getWidth()/2-100, getHeight()/2 -100, 200, 200);
		g2d.setColor(Theme.MAIN_BACKGROUND);
		try {
			g2d.setFont(Resources.getInstance().getFont("RobotoCondensed-Regular.ttf").deriveFont(32F));
		} catch (FontFormatException | IOException e) { }
		g2d.drawString("Loading...", getWidth()/2-50, getHeight()/2+10);
	}
	
	
	private class StartPanelMenu extends JPanel{
		
		private FlipPanel flipper;
		
		public StartPanelMenu() {
			this.setBackground(Theme.MAIN_BACKGROUND);
			this.setBorder(BorderFactory.createLineBorder(Theme.MAIN_BACKGROUND.darker()));
			this.setPreferredSize(Theme.START_MENU_FIXED_DIMENSION);
			this.setLayout(new BorderLayout());
			
			ServerChooser front = new ServerChooser();
			ServerMaker back = new ServerMaker();
			flipper = new FlipPanel(front, back);
			flipper.setDirection(FlipPanel.LEFT);

			FlipListener listener = new FlipListener(){
				@Override
				public void toggleFlip() {
					flipper.flip();
				}
			};

			StartupListener startupListener = new StartupListener() {
				@Override
				public void serverSelected(String server) {
					loadServer(server);
					if(flipper.isFlipped()){
						flipper.flip();
					}
				}
			};
					
			
			this.setFocusable(true);
			
			front.addFlipListener(listener);
			back.addFlipListener(listener);
			
			front.addStarupListener(startupListener);
			back.addStartupListener(startupListener);
			
			this.add(flipper, BorderLayout.CENTER);
			
			
		}
	}
}

