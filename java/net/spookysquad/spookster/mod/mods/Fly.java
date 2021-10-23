package net.spookysquad.spookster.mod.mods;

import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.utils.PlayerUtil;

import org.lwjgl.input.Keyboard;

public class Fly extends Module {

	public Fly() {
		super(new String[] { "Fly" }, "Weeooooooo", Type.MOVEMENT, Keyboard.KEY_F, 0xC300C3);
	}
	
	// maybe bypass?!?!?!??!?

	public void onEvent(Event event) {
		if(event instanceof EventPreMotion) {
			PlayerUtil.getPlayer().capabilities.isFlying = true;
		}
	}
	
	public boolean onEnable() {
		PlayerUtil.getPlayer().capabilities.isFlying = true;
		return super.onEnable();
	}
	
	public boolean onDisable() {
		PlayerUtil.getPlayer().capabilities.isFlying = false;
		return super.onDisable();
	}

}
