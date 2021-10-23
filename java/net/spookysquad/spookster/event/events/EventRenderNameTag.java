package net.spookysquad.spookster.event.events;

import net.minecraft.entity.EntityLivingBase;
import net.spookysquad.spookster.event.Event;

public class EventRenderNameTag extends Event {

	private EntityLivingBase entity;
	private double x;
	private double y;
	private double z;

	public EventRenderNameTag(EntityLivingBase entity, double x, double y, double z) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EntityLivingBase getEntity() {
		return entity;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
}
