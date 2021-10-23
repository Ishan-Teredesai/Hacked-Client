package net.spookysquad.spookster.mod.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.utils.AngleUtil;
import net.spookysquad.spookster.utils.TimeUtil;

public class MobFarm extends Module implements HasValues {

	public MobFarm() {
		super(new String[] { "Mob Farm" }, "Farm mobs in your fieldview", Type.COMBAT, -1, 0xFF52c5ff);
	}

	private HashSet<EntityLivingBase> mobs = new HashSet<EntityLivingBase>();
	private TimeUtil time = TimeUtil.getTime();

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotion) {
			if (mobs.isEmpty()) {
				for (Entity e : (List<Entity>) getWorld().getLoadedEntityList()) {
					if (e instanceof EntityMob || e instanceof EntityAnimal) {
						EntityLivingBase base = (EntityLivingBase) e;
						if (isMobAttackable(base)) {
							mobs.add(base);
						}
					}
				}
			} else {
				for (EntityLivingBase mob : mobs) {
					if(isMobAttackable(mob) && time.hasDelayRun(100))
						if(attack(mob))
							break;
						
				}
				ArrayList<EntityLivingBase> temp = new ArrayList<EntityLivingBase>();
				for (EntityLivingBase e : mobs) {
					if (!isMobAttackable(e)) {
						temp.add(e);
					}
				}
				mobs.removeAll(temp);
			}
		}
	}

	private boolean attack(EntityLivingBase mob) {
		getPlayer().swingItem();
		getController().attackEntity(getPlayer(), mob);
		mobs.remove(mob);
		time.resetAndAdd(new Random().nextInt(randomHitDelay));
		return true;
	}

	private boolean isMobAttackable(EntityLivingBase mob) {

		if (getPlayer().getDistanceToEntity(mob) < reach && mob.hurtResistantTime <= 8 && mob.getHealth() > 0 && !mob.isDead) {
			float[] angles = AngleUtil.getAngles(mob, getPlayer());
			float yaw = AngleUtil.getDistanceBetweenAngle(getPlayer().rotationYaw, angles[0]);
			float pitch = AngleUtil.getDistanceBetweenAngle(getPlayer().rotationPitch, angles[1]);
			if (yaw < 60 && pitch < 90) return true;
		}
		return false;
	}
	
	public double reach = 4.0;
	public int randomHitDelay = 50;
	
	private String REACH = "Hit Reach", RANDOMDELAY = "Random Hit Delay";
	private List<Value> values = Arrays.asList(new Value[] { new Value(REACH, 1.0, 6.0, 0.1F), new Value(RANDOMDELAY, 1, 1000, 1F)});

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(REACH)) return reach;
		else if (n.equals(RANDOMDELAY)) return randomHitDelay;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(REACH)) reach = (Math.round((Double) v * 10) / 10.0D);
		else if (n.equals(RANDOMDELAY)) randomHitDelay = Math.round((Integer) v);
	}

}
