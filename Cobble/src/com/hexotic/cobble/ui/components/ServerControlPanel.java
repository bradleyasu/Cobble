package com.hexotic.cobble.ui.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.ui.components.menupanel.MenuItem;
import com.hexotic.cobble.ui.components.menupanel.MenuItemListener;
import com.hexotic.cobble.ui.components.menupanel.MenuPanel;
import com.hexotic.cobble.ui.components.panels.AboutPanel;
import com.hexotic.cobble.ui.components.panels.server.ServerHomePanel;
import com.hexotic.cobble.ui.components.players.PlayerPanel;

public class ServerControlPanel extends JPanel{
	
	private List<FlipListener> listeners;
	private int consoleTabWidth = 200;
	private int consoleTabX = 0;
	private int consoleTabY = 0;
	private CardLayout layout;
	private JPanel pagesPanel;
	private MenuPanel menu;
	
	private Map<MenuItem, JPanel> pages;
	
	public ServerControlPanel() {
		listeners = new ArrayList<FlipListener>();
		pages = new TreeMap<MenuItem, JPanel>();
		
		int itemId = 0;
		pages.put(new MenuItem(itemId++, "Server"), new ServerHomePanel());
		pages.put(new MenuItem(itemId++, "Players"), new PlayerPanel());
		pages.put(new MenuItem(itemId++, "Settings"), new AboutPanel());
		pages.put(new MenuItem(itemId++, "About"), new AboutPanel());
		
		setupMenuPanel();
		setupPanel();
		setupContainerPanel();
		setupListener();
		addFooter();
	}

	private void setupPanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BorderLayout());
		
		this.add(menu, BorderLayout.NORTH);
		
	}
	
	private MenuPanel setupMenuPanel(){
		menu = new MenuPanel();
				
		return menu;
	}
	
	private void setupContainerPanel() {
		layout = new CardLayout();
		pagesPanel = new JPanel(layout);
		
		for(MenuItem item : pages.keySet()){
			pagesPanel.add(pages.get(item), item.getText());
			item.addMenuItemListener(new MenuItemListener(){
				@Override
				public void itemClicked(MenuItem item, String value) {
					layout.show(pagesPanel, value);
					menu.resetAll();
					item.setSelected(true);
					menu.refresh();
				}
			});
			if(item.getId() == 0){
				item.setSelected(true);
			}
			menu.addMenuItem(item);
		}
		
		this.add(pagesPanel, BorderLayout.CENTER);
	}

	private void setupListener() {
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getY() >= consoleTabY && arg0.getX() >= consoleTabX && arg0.getX() <= consoleTabX + consoleTabWidth){
					notifyListeners();
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}
	
	private void addFooter() {
		JLabel label = new JLabel();
		label.setPreferredSize(Theme.CONSOLE_INPUT_DIMENSION);
		this.add(label,BorderLayout.SOUTH);
	}
	
	public void addFlipListener(FlipListener listener){
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(FlipListener listener : listeners){
			listener.toggleFlip();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Theme.MAIN_COLOR_TWO);
		g2d.fillRect(0, getHeight()-4, getWidth(), getHeight());
		consoleTabX = getWidth()-consoleTabWidth-20;
		consoleTabY = getHeight()-20;
		g2d.fillRoundRect(consoleTabX, consoleTabY, consoleTabWidth, getHeight(), 4,4);
		
	}
}
