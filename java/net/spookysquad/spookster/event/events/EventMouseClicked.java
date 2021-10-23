package net.spookysquad.spookster.event.events;

import net.spookysquad.spookster.event.Event;

public class EventMouseClicked extends Event {

	private int button;
	private boolean ingame;

	public EventMouseClicked(int button, boolean ingame) {
		this.button = button;
		this.ingame = ingame;
	}

	public int getButton() {
		return button;
	}
	
	public boolean isInGame() {
		return ingame;
	}

}
