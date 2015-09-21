package com.hexotic.cobble.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jnbt.CompoundTag;
import org.jnbt.IntTag;
import org.jnbt.LongTag;
import org.jnbt.NBTInputStream;

public class LevelDat {

	public static final String[] GAME_TYPES = { "Survival", "Creative", "Adventure", "Spectator" };
	public static final String UNKNOWN = "uknown";

	private File datFile;
	
	private TimerTask task;
	private Timer updater;
	private long currentTime = 0;
	private String seed = LevelDat.UNKNOWN;
	private int gameType = -1;
	
	private List<LevelDatListener> listeners;

	public LevelDat() {
		listeners = new ArrayList<LevelDatListener>();
	}

	public void setDatFile(File datFile) {
		this.datFile = datFile;
	}
	
	public void addLevelDatListener(LevelDatListener listener){
		listeners.add(listener);
	}
	
	private void notifyListeners() {
		for(LevelDatListener listener : listeners){
			listener.updated();
		}
	}
	public void start() {
		task = new TimerTask() {
			@Override
			public void run() {
				try {
					loadLevelData();
					notifyListeners();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		updater = new Timer(true);
		updater.scheduleAtFixedRate(task, 0, 10 * 1000);
	}
	
	private void loadLevelData() throws IOException {
		NBTInputStream ns = new NBTInputStream(new FileInputStream(datFile));
		CompoundTag master = (CompoundTag) ns.readTag();
		CompoundTag data = (CompoundTag) master.getValue().get("Data");

		LongTag time = (LongTag) data.getValue().get("DayTime");

		currentTime = time.getValue() % 24000;
		seed = (((LongTag) data.getValue().get("RandomSeed")).getValue()).toString();
		
		gameType = ((IntTag) data.getValue().get("GameType")).getValue();
		
		ns.close();
	}
	
	public String getCurrentTime(){
		return timeConvert(currentTime);
	}
	
	public String getSeed(){
		return seed;
	}
	
	public String getGameType() {
		return decodeGameTypeType(gameType);
	}

	private String timeConvert(long time) {
		String sTime = "unknown";
		if(time >= 18000){
			time = time / 1000 - 18;
		} else {
			time = time / 1000 + 6;
		}
		sTime = time + ":00";

		return sTime;
	}

	private String decodeGameTypeType(int t) {
		String type = UNKNOWN;
		if (t >= 0 && t < GAME_TYPES.length) {
			type = GAME_TYPES[t];
		}

		return type;
	}

}
