package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventGameTick;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class Fullbright extends Module implements HasValues {

	private final float[] tempBrightnessTable = new float[16];

	public Fullbright() {
		super(new String[] { "Fullbright" }, "", Type.RENDER, Keyboard.KEY_B, 0xFFE1E1C8);
	}

	public boolean onEnable() {
		if (getPlayer() == null) return false;

		System.arraycopy(Wrapper.getWorld().provider.lightBrightnessTable, 0, tempBrightnessTable, 0, 16);
		return true;
	}

	public boolean onDisable() {
		if (getPlayer() == null) return false;

		System.arraycopy(tempBrightnessTable, 0, Wrapper.getWorld().provider.lightBrightnessTable, 0, 16);
		return true;
	}

	public void onEvent(Event event) {
		if (event instanceof EventGameTick) {
			if (Wrapper.getWorld() != null) {
				for (int i = 0; i < 16; i++) {
					Wrapper.getWorld().provider.lightBrightnessTable[i] = brightness;
				}
			}
		}
	}

	public float brightness = 0.7F;

	private String BRIGHTNESS = "Brightness";
	private List<Value> values = Arrays.asList(new Value[] { new Value(BRIGHTNESS, 0d, 1d, 0.01F) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(BRIGHTNESS)) return brightness;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(BRIGHTNESS)) brightness = (float) (Math.round((Double) v * 100) / 100.0D);
	}

}
