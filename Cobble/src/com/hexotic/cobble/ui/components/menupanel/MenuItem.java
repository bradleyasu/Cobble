package com.hexotic.cobble.ui.components.menupanel;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;

public class MenuItem extends JPanel implements Comparable<MenuItem>{

	private JLabel label;
	private boolean selected = false;
	private List<MenuItemListener> listeners;
	private int id = 0;
	
	public MenuItem(int id, String label) {
		this.id = id;
		listeners = new ArrayList<MenuItemListener>();
		this.label = new JLabel(label);
		this.label.setBackground(Theme.MAIN_BACKGROUND);
		this.label.setForeground(Theme.MAIN_FOREGROUND);
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.add(this.label);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setupListeners();
	}
	
	public String getText() {
		return label.getText();
	}
	
	private void setupListeners() {
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
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
				notifyListeners();
			}
		});
	}
	
	public void addMenuItemListener(MenuItemListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(MenuItemListener listener : listeners){
			listener.itemClicked(this, label.getText());
		}
	}
	
	public void setMenutItemFont(Font font){
		label.setFont(font);
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if(selected) {
			g2d.setColor(Theme.MAIN_COLOR_FOUR);
			g2d.fillRect(0, getHeight()-4, getWidth(), getHeight());
		}
		
	}

	public int getId() {
		return id;
	}
	
	@Override
	public int compareTo(MenuItem item) {
		return getId() - item.getId();
	}
}
