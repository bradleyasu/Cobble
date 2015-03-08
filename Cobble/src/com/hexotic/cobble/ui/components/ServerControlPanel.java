package com.hexotic.cobble.ui.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;

public class ServerControlPanel extends JPanel{
	
	private List<FlipListener> listeners;
	
	public ServerControlPanel() {
		listeners = new ArrayList<FlipListener>();
		setupPanel();
		setupListener();
		
	}

	private void setupPanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);
	}
	
	private void setupListener() {
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				notifyListeners();
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
		g2d.setColor(Theme.MAIN_COLOR_ONE);
		g2d.fillRect(getWidth() -200, 10, 200, 50);
		g2d.setColor(Theme.MAIN_COLOR_FOUR);
		g2d.drawRect(getWidth() -200, 10, 200, 50);
		
		g2d.setColor(Theme.MAIN_COLOR_TWO);
		g2d.fillRect(0, getHeight()-4, getWidth(), getHeight());
		g2d.fillRoundRect(getWidth()-220, getHeight()-20, 200, getHeight(), 4,4);
		
	}
}
