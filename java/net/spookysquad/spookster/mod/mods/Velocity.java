package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPacketGet;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;

import org.lwjgl.input.Keyboard;

public class Velocity extends Module implements HasValues {

	public Velocity() {
		super("Velocity", "Reduces velocity you get", Type.MOVEMENT, Keyboard.KEY_Y, 0xFFba76ea);
	}

	public void onEvent(Event event) {
		if (event instanceof EventPacketGet) {
			EventPacketGet e = (EventPacketGet) event;
			if (e.getPacket() instanceof S12PacketEntityVelocity) {
				S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
				
				Entity theEntity = getWorld().getEntityByID(packet.func_149412_c());

				if (theEntity != null && getPlayer() != null) {
					if (theEntity.equals(getPlayer())) {
						e.cancel();
						double velocityX = packet.func_149411_d();
						double velocityY = packet.func_149410_e();
						double velocityZ = packet.func_149409_f();

						getPlayer().motionX += velocityX * horizontal / 8000;
						getPlayer().motionY += velocityY * vertical / 8000;
						getPlayer().motionZ += velocityZ * horizontal / 8000;
					}
				}
			}
		}
	}

	public double horizontal = 0.5D;
	public double vertical = 0.5D;

	private String HORIZONTAL = "Horizontal", VERTICAL = "Vertical", BOUNDINGBOXOFFSET = "Offset";
	private List<Value> values = Arrays.asList(new Value[] { new Value(HORIZONTAL, -2.0D, 2D, 0.01F), new Value(VERTICAL, -2.0D, 2D, 0.01F) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(HORIZONTAL)) return horizontal;
		else if (n.equals(VERTICAL)) return vertical;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(HORIZONTAL)) horizontal = (Math.round((Double) v * 100) / 100.0D);
		else if (n.equals(VERTICAL)) vertical = (Math.round((Double) v * 100) / 100.0D);
	}

}
