package net.spookysquad.spookster.event.events;

import net.spookysquad.spookster.event.Event;

public class EventDestroyBlock extends Event {

	private int posX, posY, posZ, side;

	public EventDestroyBlock(int posX, int posY, int posZ, int side) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.side = side;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getPosZ() {
		return posZ;
	}

	public int getSide() {
		return side;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public void setSide(int side) {
		this.side = side;
	}
}
