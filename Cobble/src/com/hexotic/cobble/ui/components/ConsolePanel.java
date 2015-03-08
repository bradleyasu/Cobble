package com.hexotic.cobble.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
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

/**
 * ConsolePanel
 * 
 * This panel contains the controls for the CLI interface with minecraft.
 * Raw output from Minecraft will be displayed here and an input area is provided
 * to manually enter commands as you would with the default minecraft server
 *  
 * @author Bradley Sheets
 *
 */
public class ConsolePanel extends JPanel {

	// List of objects that are listening for this panel to be activated
	private List<FlipListener> listeners;
	
	// The pane where output will be printed
	private JEditorPane console;
	
	// An input field for user command input
	private JTextField input;
	
	// Load the console font that will be used into this variable
	private Font consoleFont;

	public ConsolePanel() {
		listeners = new ArrayList<FlipListener>();
		console = new JEditorPane();
		try {
			consoleFont = Resources.getInstance().getFont("font_one.ttf");
		} catch (FontFormatException | IOException e) {
			Log.getInstance().error(this, "Failed to load console font", e);
		}
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

	/**
	 * Construct the output panel view.  Creates the console and input area
	 */
	private void setupOutputPanel() {
		this.setBackground(Theme.CONSOLE_BACKGROUND);
		// this.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 40));
		this.setLayout(new BorderLayout(0, 0));

		console.setBackground(Theme.CONSOLE_BACKGROUND);
		console.setForeground(Theme.CONSOLE_FOREGROUND);
		console.setPreferredSize(Theme.CONSOLE_DIMENSION);
		console.setFont(consoleFont.deriveFont(10F));
		console.setSelectionColor(Theme.MAIN_COLOR_SIX);
		console.setEditable(false);
		console.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				input.requestFocus();
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
		});
		

		JScrollPane scroller = new JScrollPane(console);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.getVerticalScrollBar().setUI(new SimpleScroller());
		scroller.getVerticalScrollBar().setPreferredSize(new Dimension(5, 5));
		this.add(scroller, BorderLayout.CENTER);

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
		input.setBackground(Theme.CONSOLE_BACKGROUND);
		input.setForeground(Theme.CONSOLE_FOREGROUND);
		input.setPreferredSize(Theme.CONSOLE_INPUT_DIMENSION);
		input.setBorder(BorderFactory.createEmptyBorder());
		input.setCaret(new BlockCaret());
		input.setSelectionColor(Theme.MAIN_COLOR_SIX);
		input.setCaretColor(Theme.CONSOLE_FOREGROUND);
		input.setFont(consoleFont.deriveFont(12F));
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					Server.getInstance().send(input.getText());
					input.setText("");
				} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE){
					notifyListeners();
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});

		this.add(input, BorderLayout.SOUTH);

		// add space to the top of the frame
		JLabel space = new JLabel("");
		space.setPreferredSize(Theme.CONSOLE_INPUT_DIMENSION);
		this.add(space, BorderLayout.NORTH);

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

		g2d.setColor(Theme.MAIN_COLOR_FOUR);
		g2d.fillRect(0, 0, getWidth(), 4);
		g2d.fillRoundRect(getWidth() - 220, 0, 200, 20, 4, 4);

	}

}
