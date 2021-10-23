package net.spookysquad.spookster.mod.mods;

import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventGameTick;
import net.spookysquad.spookster.event.events.EventPacketGet;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.utils.PlayerUtil;

import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Does sprinting for you", Type.MOVEMENT, Keyboard.KEY_V, 0xFFaaaa00);
	}

	public void onEvent(Event event) {
		if (event instanceof EventGameTick) {
			if (PlayerUtil.getPlayer() != null) {
				if (PlayerUtil.canSprint()) getPlayer().setSprinting(true);
			}
		}
	}

}
