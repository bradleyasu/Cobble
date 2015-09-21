package com.hexotic.cobble.ui.startPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.interfaces.Server;
import com.hexotic.cobble.settings.ServerStorage;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.ui.components.TextFieldWithPrompt;
import com.hexotic.cobble.ui.components.menupanel.MenuItem;
import com.hexotic.cobble.ui.components.menupanel.MenuItemListener;
import com.hexotic.cobble.ui.components.menupanel.MenuPanel;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.switches.BasicSwitch;
import com.hexotic.lib.ui.buttons.SoftButton;

public class ServerMaker extends JPanel{

	private List<FlipListener> listeners;
	private Font font;
	private JLabel header = new JLabel("New World Setup");
	private JPanel pagesPanel;
	private CardLayout layout;
	private Map<MenuItem, JPanel> pages;
	private MenuPanel menu;
	
	private List<StartupListener> startupListeners;
	
	private TextFieldWithPrompt worldName;
	private TextFieldWithPrompt port;
	private TextFieldWithPrompt playerCount;
	private BasicSwitch hardCoreMode;
	private TextFieldWithPrompt mtod;
	private TextFieldWithPrompt serverLocation;
	
	
	public ServerMaker() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BorderLayout());
		this.pages = new TreeMap<MenuItem, JPanel>();
		listeners = new ArrayList<FlipListener>();
		startupListeners = new ArrayList<StartupListener>();
		
		
		try {
			font = Resources.getInstance().getFont("RobotoCondensed-Regular.ttf").deriveFont(14F);
		} catch (FontFormatException | IOException e) {
			font = new Font("Arial", Font.BOLD, 12);
			Log.getInstance().error(this, "Failed to load Menu Item Font Face", e);
		}
		
		header.setFont(font.deriveFont(30F));
		header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		header.setForeground(Theme.MAIN_BACKGROUND);
		
		int itemId = 0;
		pages.put(new MenuItem(itemId++, "Basic Setup"), new BasicMakerPanel());
		pages.put(new MenuItem(itemId++, "Extended"), new JPanel());
		
		this.add(setupMenuPanel(), BorderLayout.NORTH);
		this.add(createFooterPanel(), BorderLayout.SOUTH);
		setupContainerPanel();
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
	
	/**
	 * Creates Footer panel of server maker which contains a cancel and 
	 * create button.  If the create button is selected, the world will be created
	 * and automatically started.
	 * 
	 * @return
	 */
	private JPanel createFooterPanel() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		footer.setBackground(this.getBackground());
		
		SoftButton cancel = new SoftButton("Cancel", Theme.MAIN_BACKGROUND, Theme.MAIN_FOREGROUND, font.deriveFont(Font.BOLD));
		SoftButton create = new SoftButton("Create and Start!", Theme.MAIN_COLOR_FIVE, Theme.MAIN_FOREGROUND, font.deriveFont(Font.BOLD));
		
		cancel.setArc(4);
		create.setArc(4);
		
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				notifyListeners();
			}
		});
		
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				createServer();
			}
		});
		
		footer.add(cancel);
		footer.add(create);
		
		return footer;
	}
	
	private void createServer() {
		try {
			ServerStorage.getInstance().saveServer(worldName.getText(), serverLocation.getText());
			notifyStartup(serverLocation.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addFlipListener(FlipListener listener){
		listeners.add(listener);
	}
	
	public void addStartupListener(StartupListener listener){
		startupListeners.add(listener);
	}
	
	private void notifyStartup(String server) {
		for(StartupListener listener : startupListeners){
			listener.serverSelected(server);
		}
	}
	
	private void notifyListeners() {
		for(FlipListener listener : listeners){
			listener.toggleFlip();
		}
	}
	
	
	// Inner Class Panels - Each being a tab on the main form
	
	private class BasicMakerPanel extends JPanel {
		
		public BasicMakerPanel(){
			this.setBackground(Theme.MAIN_BACKGROUND);
			this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
			worldName = new TextFieldWithPrompt("Cobble_World", "Name of your world");
			worldName.getDocument().addDocumentListener(new DocumentListener(){
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					check();
				}
				
				public void check() {
					if(!worldName.getText().isEmpty() && worldName.getText().length() < 50 && !ServerStorage.getInstance().getAllServers().containsKey(worldName.getText())){
						worldName.setAccepted(true);
					} else {
						worldName.setAccepted(false);
					}
				}
			});
			
			port = new TextFieldWithPrompt("25565", "Port #");
			playerCount = new TextFieldWithPrompt("5", "0-256");
			mtod = new TextFieldWithPrompt("", "Message that is displayed in the server list of the client below the name");
			serverLocation = new TextFieldWithPrompt("", "Ex: C:\\Servers\\MyWorld\\MinecraftServer.jar");

			mtod.getDocument().addDocumentListener(new DocumentListener(){
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					check();
				}
				
				public void check() {
					if(!mtod.getText().isEmpty() && mtod.getText().length() < 59){
						mtod.setAccepted(true);
					} else {
						mtod.setAccepted(false);
					}
				}
			});
			
			port.getDocument().addDocumentListener(new DocumentListener(){
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void insertUpdate(DocumentEvent arg0) {
					check();
				}
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					check();
				}
				
				public void check() {
					if(!port.getText().isEmpty() && Pattern.matches(".*[a-zA-Z].*+", port.getText()) == false && port.getText().length() <= 5){
						if(Integer.parseInt(port.getText()) > 0 &&  Integer.parseInt(port.getText()) <= 65534) {
							port.setAccepted(true);
						} else {
							port.setAccepted(false);
						}
					} else {
						port.setAccepted(false);
					}
				}
			});
			
			
			// Set defaults to accepted
			worldName.setAccepted(true);
			port.setAccepted(true);
			playerCount.setAccepted(true);
			mtod.setAccepted(true);
			
			worldName.setPreferredSize(new Dimension(350, 35));
			port.setPreferredSize(new Dimension(150, 35));
			playerCount.setPreferredSize(new Dimension(80,35));
			hardCoreMode = new BasicSwitch("Normal", "Hardcore", 150,25, 2);
			hardCoreMode.setBackground(Theme.MAIN_FOREGROUND);
			hardCoreMode.setForeground(Theme.MAIN_BACKGROUND);
			mtod.setPreferredSize(new Dimension(480, 35));
			serverLocation.setPreferredSize(new Dimension(490, 35));
			
			
			this.add(formLabel("Server Name: "));
			this.add(worldName);
			this.add(formLabel("Port #: "));
			this.add(port);
			this.add(formLabel("Maximum Number of Players: "));
			this.add(playerCount);
			this.add(formLabel("Game Mode: "));
			this.add(hardCoreMode);
			this.add(formLabel("Server Message (MTOD): "));
			this.add(mtod);
			this.add(formLabel("Server Executable Path: "));
			this.add(serverLocation);
			
		}
		
		private JLabel formLabel(String text){
			JLabel label = new JLabel(text);
			label.setFont(font.deriveFont(18F));
			return label;
		}
	}
}
