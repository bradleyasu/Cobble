package com.hexotic.cobble.ui.startPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.settings.ServerStorage;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.layout.AnimatedGridLayout;
import com.hexotic.lib.ui.panels.SimpleScroller;

public class ServerChooser extends JPanel{

	private List<FlipListener> listeners;
	private Font font;
	private JPanel chooser;
	private List<StartupListener> startupListeners;
	
	public ServerChooser() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		
		listeners = new ArrayList<FlipListener>();
		startupListeners = new ArrayList<StartupListener>();
		
		this.setLayout(new BorderLayout());
		
		try {
			font = Resources.getInstance().getFont("RobotoCondensed-Light.ttf").deriveFont(34F);
		} catch (FontFormatException | IOException e) {
			font = new Font("Arial", Font.BOLD, 34);
			Log.getInstance().error(this, "Failed to load Menu Item Font Face", e);
		}
		
		JLabel label = new JLabel("Select Minecraft World");
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		chooser = createChooserPanel();
		JScrollPane scroller = new JScrollPane(chooser);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(5, 5));
		scroller.getVerticalScrollBar().setUnitIncrement(16);
		label.setForeground(Theme.MAIN_FOREGROUND);
		this.add(label, BorderLayout.NORTH);
		this.add(scroller, BorderLayout.CENTER);
	}
	
	private JPanel createChooserPanel() {
		AnimatedGridLayout layout = new AnimatedGridLayout(10,10, true);
		chooser = new JPanel(layout);
		chooser.setBackground(Theme.MAIN_BACKGROUND);
		refreshServerList();
		
		return chooser;
	}
	
	private void refreshServerList() {
		chooser.removeAll();
		int id = 0;
		for(String serverName : ServerStorage.getInstance().getAllServers().keySet()){
			chooser.add(new ServerChooserItem(id++, serverName));
		}
		chooser.add(new ServerChooserNewItem());
	}
	
	public void addFlipListener(FlipListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for(FlipListener listener : listeners){
			listener.toggleFlip();
		}
	}
	
	private void notifyStartup(String server) {
		for(StartupListener listener : startupListeners){
			listener.serverSelected(server);
		}
	}
	
	public void addStarupListener(StartupListener listener){
		startupListeners.add(listener);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Theme.MAIN_BACKGROUND);
		g2d.fillRect(0, 0, getWidth(), 60);
		
		g2d.setColor(Theme.MAIN_COLOR_ONE);
		g2d.fillRect(0, 5, 5, 50);
		
		g2d.setColor(Theme.MAIN_BACKGROUND.darker());
		g2d.drawLine(0, 59, getWidth(), 59);
		
		g2d.setColor(Theme.MAIN_BACKGROUND.brighter());
		g2d.drawLine(0, 60, getWidth(), 60);

	}
	
	private class ServerChooserItem extends JPanel implements ChooserItem, Comparable<ChooserItem> {
		
		private String serverName;
		private String serverExec;
		private int id;
		private boolean hovering = false;
		
		public ServerChooserItem(int id, String name){
			this.setPreferredSize(Theme.SERVER_CHOOSER_ITEM_DIMENSION);
			this.setBackground(Theme.MAIN_FOREGROUND);
			this.serverName = name;
			serverExec = ServerStorage.getInstance().getAllServers().get(name);
			this.id = id;
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					notifyStartup(serverExec);
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					hovering = true;
					revalidate();
					repaint();
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					hovering = false;
					revalidate();
					repaint();
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
				}
				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
			});
		}

		public int getId(){
			return id;
		}

		@Override
		public int compareTo(ChooserItem item) {
			return id - item.getId();
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Panel footer background
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, getHeight() - 50, getWidth(), getHeight());
			
			if(hovering){
				g2d.setColor(Theme.MAIN_COLOR_FOUR);
			} else {
				g2d.setColor(Theme.MAIN_BACKGROUND.darker());
			}

			// Draw Card Border
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			
			g2d.setColor(Theme.MAIN_BACKGROUND.darker());

			
			// Draw Card Footer
			g2d.drawLine(0, getHeight()-50, getWidth(), getHeight()-50);
			
			g2d.setColor(Theme.MAIN_FOREGROUND);
			g2d.setFont(font.deriveFont(14F));
			g2d.drawString(serverName, 5, getHeight()-30);
		}
		
	}
	
	private class ServerChooserNewItem extends JPanel implements ChooserItem, Comparable<ChooserItem>{
		private boolean hovering = false;
		
		private int id = 100000;
		
		public ServerChooserNewItem() {
			this.setPreferredSize(Theme.SERVER_CHOOSER_ITEM_DIMENSION);
			this.setBackground(Theme.MAIN_BACKGROUND);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					notifyListeners();
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					hovering = true;
					revalidate();
					repaint();
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					hovering = false;
					revalidate();
					repaint();
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
				}
				@Override
				public void mouseReleased(MouseEvent arg0) {
				}
			});
		}
		
		public int getId(){
			return id;
		}
		
		@Override
		public int compareTo(ChooserItem item) {
			return id - item.getId();
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			float dash1[] = {10.0f};
			g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));

			if(hovering){
				g2d.setColor(Theme.MAIN_COLOR_FOUR);
			} else {
				g2d.setColor(Theme.MAIN_BACKGROUND.darker());
			}
			
			g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20,20);
			g2d.setFont(font.deriveFont((float) 104.0));
			g2d.drawString("+", getWidth()/2-25, getHeight()/2+20);
			g2d.setFont(font.deriveFont((float) 28.0));
			g2d.drawString("New World", 14, getHeight()-20);
		}
	}
}
