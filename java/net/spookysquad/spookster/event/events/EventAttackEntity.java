package net.spookysquad.spookster.event.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.spookysquad.spookster.event.Event;

public class EventAttackEntity extends Event {

	private EntityPlayer attacker;
	private Entity target;

	public EventAttackEntity(EntityPlayer attacker, Entity target) {
		this.attacker = attacker;
		this.target = target;
	}

	public EntityPlayer getAttacker() {
		return attacker;
	}
	
	public Entity getTarget() {
		return target;
	}
	
}
