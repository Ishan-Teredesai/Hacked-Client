package net.spookysquad.spookster.event;

import net.spookysquad.spookster.Spookster;

public class Event {

	private boolean isCancelled = false;
	
	public void cancel() {
		this.isCancelled = true;
	}
	
	public boolean isCancelled() {
		return isCancelled;
	}
	
	public Event call() {
		Spookster.instance.eventManager.callEvent(this);
		return this;
	}
}
