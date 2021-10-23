package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;

public class GangsterWalk extends Module implements HasValues {

	public GangsterWalk() {
		super(new String[] { "GangsterWalk" }, "Look like a gangster", Type.RENDER, -1, -1);
	}

	public void onEvent(Event event) {
		if(event instanceof EventPreMotion) {
			if(cameraYaw) {
				getPlayer().cameraYaw -= cameraYawValue;
			}
			
			if(cameraPitch) {
				getPlayer().cameraPitch -= cameraPitchValue;
			}
		}
	}
	
	public boolean cameraYaw = true;
	public boolean cameraPitch = false;
	public double cameraYawValue = 0.5;
	public double cameraPitchValue = 0.5;
	private String CAMERAYAW = "Camera Yaw", CAMERAPITCH = "Camera Pitch", ALLOWYAW = "Allow Yaw", ALLOWPITCH = "Allow Pitch";
	private List<Value> values = Arrays.asList(new Value[] { new Value(CAMERAYAW, 0.1D, 5D, 0.1F), new Value(CAMERAPITCH, 0.1D, 5D, 0.1F),
			new Value(ALLOWYAW, false, true), new Value(ALLOWPITCH, false, true)});

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(CAMERAYAW)) return cameraYawValue;
		else if (n.equals(CAMERAPITCH)) return cameraPitchValue;
		else if (n.equals(ALLOWYAW)) return cameraYaw;
		else if (n.equals(ALLOWPITCH)) return cameraPitch;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(CAMERAYAW)) cameraYawValue = (Math.round((Double) v * 10) / 10.0D);
		else if (n.equals(CAMERAPITCH)) cameraPitchValue = (Math.round((Double) v * 10) / 10.0D);
		else if (n.equals(ALLOWYAW)) cameraYaw = (Boolean) v;
		else if (n.equals(ALLOWPITCH)) cameraPitch = (Boolean) v;
	}

}
