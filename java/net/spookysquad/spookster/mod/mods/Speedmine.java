package net.spookysquad.spookster.mod.mods;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventGameTick;
import net.spookysquad.spookster.event.events.EventHardnessBlock;
import net.spookysquad.spookster.injection.ObfuscationTable;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.util.ModUtilities;

public class Speedmine extends Module implements HasValues {

	private Field blockHitDelay;
	private Field curBlockDamageMP;

	public Speedmine() {
		super("Speedmine", "Allows you to mine faster", Type.WORLD, Keyboard.KEY_NONE, 0xFFaaaa00);

		try {
			blockHitDelay = PlayerControllerMP.class.getDeclaredField(ModUtilities.getObfuscatedFieldName(ObfuscationTable.blockHitDelay));
			blockHitDelay.setAccessible(true);
			
			curBlockDamageMP = PlayerControllerMP.class.getDeclaredField(ModUtilities.getObfuscatedFieldName(ObfuscationTable.curBlockDamageMP));
			curBlockDamageMP.setAccessible(true);
		} catch (Exception e) {
		}
	}

	public void onEvent(Event event) {
		if (event instanceof EventGameTick) {
			if(Wrapper.getPlayer() != null) {
				try {
					blockHitDelay.setAccessible(true);
					if(blockHitDelay.getInt(Wrapper.getMinecraft().playerController) < hitDelay) {
						blockHitDelay.set(Wrapper.getMinecraft().playerController, hitDelay);
					}
				} catch (Exception e) {
					Wrapper.logChat(MessageType.NOTIFCATION, "Speedmine Hitdelay error!");
				}
				
				try {
					blockHitDelay.setAccessible(true);
					if(curBlockDamageMP.getFloat(Wrapper.getMinecraft().playerController) < start) {
						curBlockDamageMP.set(Wrapper.getMinecraft().playerController, (float) start);
					}
				} catch (Exception e) {
					Wrapper.logChat(MessageType.NOTIFCATION, "Speedmine curBlockDamage error!");
				}
			}
		}
		if (event instanceof EventHardnessBlock) {
			EventHardnessBlock ev = (EventHardnessBlock) event;
			ev.setHardness(ev.getHardness() - (float) speed);
		}
	}

	private int hitDelay = 0;
	private double start = 0.2;
	private double speed = 0.05;

	private String HITDELAY = "Hit Delay", START = "Start Damage", SPEED = "Speed";
	private List<Value> values = Arrays.asList(new Value[] { new Value(HITDELAY, 0, 5, 1F), new Value(START, 0d, 1d, 0.01F), new Value(SPEED, 0d, 1d, 0.01F) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(HITDELAY)) return hitDelay;
		else if(n.equals(START)) return start;
		else if(n.equals(SPEED)) return speed;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(HITDELAY)) hitDelay = (Integer) v;
		else if(n.equals(START)) start = (Math.round((Double) v * 100) / 100.0D);
		else if(n.equals(SPEED)) speed = (Math.round((Double) v * 100) / 100.0D);
	}
}
