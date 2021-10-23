package net.spookysquad.spookster.event;

public abstract interface Listener {

	/**
	 * Receives an Event when its called by the EventManager when the listener is registered
	 * @param event
	 */
	public abstract void onEvent(Event event);
}
