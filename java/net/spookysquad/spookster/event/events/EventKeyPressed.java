package net.spookysquad.spookster.event.events;

import net.spookysquad.spookster.event.Event;

public class EventKeyPressed extends Event {

	private int key;
	private boolean ingame;

	public EventKeyPressed(int key, boolean ingame) {
		this.key = key;
		this.ingame = ingame;
	}

	public int getKey() {
		return key;
	}
	
	public boolean isInGame() {
		return ingame;
	}
	
}
