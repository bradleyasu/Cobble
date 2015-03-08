package com.hexotic.cobble.ui.components;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.interfaces.ServerListener;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.ui.panels.SimpleScroller;

public class LogPanel extends JPanel {

	private List<FlipListener> listeners;
	private JEditorPane console;
	private Image background;
	private JTextField input;

	public LogPanel() {
		listeners = new ArrayList<FlipListener>();
		console = new JEditorPane();
		background = Resources.getInstance().getImage("corner_creeper.png");

		setupOutputPanel();
		setupListener();
	}

	private void setupListener() {
		this.addMouseListener(new MouseListener() {
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

	public void addFlipListener(FlipListener listener) {
		listeners.add(listener);
	}

	public void notifyListeners() {
		for (FlipListener listener : listeners) {
			listener.toggleFlip();
		}
	}

	private void setupOutputPanel() {
		this.setBackground(Theme.MAIN_COLOR_TWO);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 40));
		console.setBackground(Theme.MAIN_BACKGROUND);
		console.setForeground(Theme.MAIN_FOREGROUND);
		console.setPreferredSize(Theme.CONSOLE_DIMENSION);
		console.setEditable(false);
		
		JScrollPane scroller = new JScrollPane(console);
		scroller.setBorder(BorderFactory.createLineBorder(Theme.MAIN_COLOR_FIVE));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(5,5));
		this.add(scroller);

		Server.getInstance().addListener(new ServerListener() {
			@Override
			public void outputRecieved(String output) {
				try {
					appendLine(output);
					
				} catch (BadLocationException e) {
					Log.getInstance().error(this, "Failed to update Console", e);
				}
			}
		});
		
		input = new JTextField();
		input.setBackground(Theme.MAIN_BACKGROUND);
		input.setForeground(Theme.MAIN_FOREGROUND);
		input.setPreferredSize(Theme.CONSOLE_INPUT_DIMENSION);
		input.setBorder(BorderFactory.createLineBorder(Theme.MAIN_COLOR_FIVE));
		input.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					Server.getInstance().send(input.getText());
					input.setText("");
				}
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		
		this.add(input);
		
	}

	public void appendLine(String line) throws BadLocationException {
		Document doc = console.getDocument();
		doc.insertString(doc.getLength(), line + "\n", null);
		console.setCaretPosition(doc.getLength());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Theme.MAIN_BACKGROUND);
		g2d.fillRect(0, 0, getWidth(), 4);
		g2d.fillRoundRect(getWidth() - 220, 0, 200, 20, 4, 4);

		g2d.drawImage(background, 0, getHeight() - background.getHeight(null), background.getWidth(null), background.getHeight(null), null);

	}

}
