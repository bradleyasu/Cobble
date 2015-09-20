package com.hexotic.cobble.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.ui.components.ConsolePanel;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.ui.components.ServerControlPanel;
import com.hexotic.lib.ui.panels.FlipPanel;

public class MainPanel extends JPanel{

	private FlipPanel flipper;
	
	public MainPanel() {
		this.setBackground(Theme.CONSOLE_BACKGROUND);
		this.setLayout(new BorderLayout());
		
		ServerControlPanel front = new ServerControlPanel();
		final ConsolePanel back = new ConsolePanel();
		
		
		FlipListener listener = new FlipListener(){
			@Override
			public void toggleFlip() {
				flipper.flip();
			}
		};
		this.setFocusable(true);
		
		front.addFlipListener(listener);
		back.addFlipListener(listener);
		
		addSoftwareHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_F5,  0), "FlipToggle", new AbstractAction() {
			private static final long serialVersionUID = 6104870250516634592L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				flipper.flip();
			}
		});
		
		flipper = new FlipPanel(front, back);
		flipper.setDirection(FlipPanel.UP);
		this.add(flipper);
		
	}
	
	private void addSoftwareHotKey(KeyStroke keyStroke, String key, Action action){
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		this.getActionMap().put(key, action);
	}
}
