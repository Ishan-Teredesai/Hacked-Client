package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventBoundingBox;
import net.spookysquad.spookster.event.events.EventInOpaqueBlock;
import net.spookysquad.spookster.event.events.EventPacketGet;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.event.events.EventPushOutOfBlocks;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.mod.values.Value.ValueType;
import net.spookysquad.spookster.utils.PacketUtil;
import net.spookysquad.spookster.utils.PlayerUtil;
import net.spookysquad.spookster.utils.TimeUtil;

import org.lwjgl.input.Keyboard;

public class Phase extends Module implements HasValues {

	public Phase() {
		super(new String[] { "Phase" }, "Collide with blocks and pass through them.", Type.EXPLOITS, Keyboard.KEY_P, 0xFFA4A4A4);
	}

	TimeUtil time = TimeUtil.getTime();
	TimeUtil timeInBlock = TimeUtil.getTime();

	public boolean onDisable() {
		PlayerUtil.getPlayer().noClip = false;
		return super.onDisable();
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventPreMotion) {
			if (noClip) {
				if (Spookster.instance.moduleManager.getModule(Fly.class).isEnabled()) {
					PlayerUtil.getPlayer().noClip = true;
				} else {
					PlayerUtil.getPlayer().noClip = false;
				}
			} else if (normal) {
				if (getPlayer().isCollidedHorizontally && time.hasDelayRun(100)) {
					getPlayer().motionX *= -1.0;
					getPlayer().motionZ *= -1.0;
					float dir = getPlayer().rotationYaw;
					if (getPlayer().moveForward < 0) {
						dir += 180;
					}
					if (getPlayer().moveStrafing > 0) {
						dir -= 90 * (getPlayer().moveForward > 0 ? 0.5F : getPlayer().moveForward < 0 ? -0.5F : 1);
					} else if (getPlayer().moveStrafing < 0) {
						dir += 90 * (getPlayer().moveForward > 0 ? 0.5F : getPlayer().moveForward < 0 ? -0.5F : 1);
					}
					double hOff = 0.221;
					double vOff = 0.2;
					double xD = (float) Math.cos((dir + 90) * Math.PI / 180) * hOff;
					double yD = vOff;
					double zD = (float) Math.sin((dir + 90) * Math.PI / 180) * hOff;

					double[] offY = new double[] { -0.025, -0.028, -0.033, -0.04, -0.04, -0.033, -0.028, -0.025 };

					for (int i = 0; i < offY.length; i++) {
						PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().boundingBox.minY + offY[i], getPlayer().posY + offY[i], getPlayer().posZ, getPlayer().onGround));
						PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX + (xD * i), getPlayer().boundingBox.minY, getPlayer().posY, getPlayer().posZ + (zD * i), getPlayer().onGround));
					}
				} else if (!getPlayer().isCollidedHorizontally) {
					time.reset();
				}
			}
		} else if (e instanceof EventBoundingBox) {
			EventBoundingBox event = (EventBoundingBox) e;
			if (PlayerUtil.isInsideBlock()) {
				if (event.getY() > getPlayer().boundingBox.minY - 0.001F && timeInBlock.hasDelayRun(100)) {
					event.setBoundingBox(null);
				}
			} else {
				timeInBlock.reset();
			}
		} else if (e instanceof EventPushOutOfBlocks) e.cancel();
		else if (e instanceof EventInOpaqueBlock) e.cancel();
		else if (e instanceof EventPacketGet && ((EventPacketGet) e).getPacket() instanceof S12PacketEntityVelocity && timeInBlock.hasDelayRun(100)) e.cancel();

	}

	public boolean normal = true;
	public boolean noClip = false;
	private String PHASEMODE = "Phase Mode", NOCLIP = "Noclip", NORMAL = "Normal";
	private List<Value> values = Arrays.asList(new Value[] { new Value(PHASEMODE, false, Arrays.asList(new Value(NORMAL, false, true), new Value(NOCLIP, false, true)), ValueType.MODE) });

	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(NORMAL)) return normal;
		else if (n.equals(NOCLIP)) return noClip;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(NORMAL)) normal = (Boolean) v;
		else if (n.equals(NOCLIP)) noClip = (Boolean) v;
	}
}
