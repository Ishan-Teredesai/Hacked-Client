package net.spookysquad.spookster.utils;

/**
 * Basic Timer class used for delays and such.
 * @author Halalaboos
 */
public class TimeUtil {

	private long resetMS = 0l;

	public void reset() {
		resetMS = getCurrentTime();
	}

	public void setReset(long setReset) {
		resetMS = setReset;
	}
	
	public void resetAndAdd(long reset) {
		resetMS = getCurrentTime() + reset;
	}

	public boolean hasDelayRun(long miliseconds) {
		return getCurrentTime() >= resetMS + miliseconds;
	}

	public static long getCurrentTime() {
		return (long) (System.nanoTime() / 1E6);
	}

	public static boolean hasDelayRun(long resetMS, int delay) {
		return getCurrentTime() >= resetMS + delay;
	}

	public static TimeUtil getTime() {
		return new TimeUtil();
	}
}