package com.hexotic.cobble.utils;

public class LevelDat {

	public static final String[] GAME_TYPES = { "Survival", "Creative", "Adventure", "Spectator" };
	public static final String UNKNOWN = "uknown";

	private static LevelDat instance = null;

	private LevelDat() {
	}

	public static String timeConvert(long time) {
		String sTime = "unknown";
		if(time >= 18000){
			time = time / 1000 - 18;
		} else {
			time = time / 1000 + 6;
		}
		sTime = time + ":00";

		return sTime;
	}

	public static String getGameType(int t) {
		String type = UNKNOWN;
		if (t >= 0 && t < GAME_TYPES.length) {
			type = GAME_TYPES[t];
		}

		return type;
	}

}
