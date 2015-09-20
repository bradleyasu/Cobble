package com.hexotic.cobble.ui.startPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.ui.components.FlipListener;
import com.hexotic.cobble.ui.components.TextFieldWithPrompt;
import com.hexotic.cobble.ui.components.menupanel.MenuItem;
import com.hexotic.cobble.ui.components.menupanel.MenuItemListener;
import com.hexotic.cobble.ui.components.menupanel.MenuPanel;
import com.hexotic.cobble.utils.Log;
import com.hexotic.lib.resource.Resources;
import com.hexotic.lib.switches.BasicSwitch;
import com.hexotic.lib.ui.buttons.SoftButton;
import com.hexotic.lib.ui.windows.components.FileExplorer;

public class ServerMaker extends JPanel{

	private List<FlipListener> listeners;
	private Font font;
	private JLabel header = new JLabel("New World Setup");
	private JPanel pagesPanel;
	private CardLayout layout;
	private Map<MenuItem, JPanel> pages;
	private MenuPanel menu;
	
	public ServerMaker() {
		this.setBackground(Theme.MAIN_BACKGROUND);
		this.setLayout(new BorderLayout());
		this.pages = new TreeMap<MenuItem, JPanel>();
		listeners = new ArrayList<FlipListener>();
		
		
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
		
		footer.add(cancel);
		footer.add(create);
		
		return footer;
	}
	
	public void addFlipListener(FlipListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for(FlipListener listener : listeners){
			listener.toggleFlip();
		}
	}
	
	
	// Inner Class Panels - Each being a tab on the main form
	
	private class BasicMakerPanel extends JPanel {
		
		private TextFieldWithPrompt worldName;
		private TextFieldWithPrompt port;
		private TextFieldWithPrompt playerCount;
		private BasicSwitch hardCoreMode;
		private TextFieldWithPrompt mtod;
		private TextFieldWithPrompt serverLocation;
		
		public BasicMakerPanel(){
			this.setBackground(Theme.MAIN_BACKGROUND);
			this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 20));
			worldName = new TextFieldWithPrompt("Cobble_World", "Name of your world");
			port = new TextFieldWithPrompt("25565", "Port #");
			playerCount = new TextFieldWithPrompt("5", "0-256");
			mtod = new TextFieldWithPrompt("", "Message that is displayed in the server list of the client below the name");
			serverLocation = new TextFieldWithPrompt("", "Ex: C:\\Servers\\MyWorld\\MinecraftServer.jar");
			
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
			this.add(formLabel("                            Game Mode: "));
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
