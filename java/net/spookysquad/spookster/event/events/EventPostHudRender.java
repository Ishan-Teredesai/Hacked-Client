package net.spookysquad.spookster.event.events;

import net.spookysquad.spookster.event.Event;

public class EventPostHudRender extends Event {

	private int screenWidth;
	private int screenHeight;
	
	public EventPostHudRender(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}
