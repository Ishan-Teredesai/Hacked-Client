package net.spookysquad.spookster.event.events;

import net.minecraft.network.Packet;
import net.spookysquad.spookster.event.Event;

public class EventPacketGet extends Event {

	private Packet packet;

	public EventPacketGet(Packet packet) {
		this.packet = packet;
	}

	public Packet getPacket() {
		return packet;
	}
	
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	
}
