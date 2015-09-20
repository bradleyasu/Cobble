package com.hexotic.cobble.ui.components.panels.server;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;

import com.hexotic.cobble.constants.Theme;
import com.hexotic.cobble.utils.LevelDat;

public class ServerHomePanel extends JPanel {

	private TimerTask task;
	private Timer updater;
	private long currentTime = 0;
	private String seed = LevelDat.UNKNOWN;
	private int gameType = -1;

	public ServerHomePanel() {
		this.setBackground(Theme.MAIN_BACKGROUND);

		task = new TimerTask() {
			@Override
			public void run() {
				try {
					loadLevelData();
					refresh();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		updater = new Timer(true);
		updater.scheduleAtFixedRate(task, 0, 10 * 1000);

	}

	private void refresh() {
		this.revalidate();
		this.repaint();
	}
	
	private void loadLevelData() throws IOException {
		File f = new File("D:\\Games\\minecraft_server\\BaSingSe\\level.dat");
		NBTInputStream ns = new NBTInputStream(new FileInputStream(f));
		CompoundTag master = (CompoundTag) ns.readTag();
		CompoundTag data = (CompoundTag) master.getValue().get("Data");

		LongTag time = (LongTag) data.getValue().get("DayTime");

		currentTime = time.getValue() % 24000;
		seed = (((LongTag) data.getValue().get("RandomSeed")).getValue()).toString();
		
		gameType = ((IntTag) data.getValue().get("GameType")).getValue();
		
		ns.close();
	}
	

	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.drawString("Current Time: "+LevelDat.timeConvert(currentTime), 20,20);
		g2d.drawString("Seed: "+seed, 20,40);
		g2d.drawString("Game Type: "+LevelDat.getGameType(gameType), 20,60);
		
	}
}
