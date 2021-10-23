package net.spookysquad.spookster.event.events;

import net.spookysquad.spookster.event.Event;

public class Event3DRender extends Event {

	private float partialTicks;
	private int xOffset;

	public Event3DRender(float partialTicks, int xOffset) {
		this.partialTicks = partialTicks;
		this.xOffset = xOffset;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
}
