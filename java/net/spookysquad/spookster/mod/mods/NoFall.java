package net.spookysquad.spookster.mod.mods;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPacketSend;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.utils.PacketUtil;
import net.spookysquad.spookster.utils.PlayerUtil;

import org.lwjgl.input.Keyboard;

public class NoFall extends Module {

	private float fallDistance = 0.0F;
	private int land = -1;

	public NoFall() {
		super(new String[] { "NoFall" }, "Prevent taking fall damage when falling off of a high distance.", Type.EXPLOITS,
				Keyboard.KEY_N, 0xFF13C422);
	}

	@Override
	public boolean onEnable() {
		PlayerUtil.inflictDamage(4);
		fallDistance = 0;
		land = -1;

		return true;
	}

	@Override
	public boolean onDisable() {
		return true;
	}

	public boolean isSafe() {
		return getPlayer().isOnLadder() || getPlayer().isInWater();
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventPreMotion) {
			EventPreMotion event = (EventPreMotion) e;
			
			fallDistance += getPlayer().fallDistance;
			
			if (fallDistance > 3 && isSafe()) {
				land = 0;
			}
		} else if (e instanceof EventPacketSend) {
			EventPacketSend event = (EventPacketSend) e;
			if (event.getPacket() instanceof C03PacketPlayer) {
				C06PacketPlayerPosLook packet = PacketUtil.forcedC06Packet((C03PacketPlayer) event.getPacket());
				if (land < 1) {
					event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getPositionX(),
							getPlayer().boundingBox.minY + (land == -1 ? 0.6F : 0.3F), getPlayer().posY
									+ (land == -1 ? 0.6F : 0.3F), packet.getPositionZ(), packet.getYaw(), packet.getPitch(), false));
					
					if (land == 0) {
						toggle(true);
					}
					
					if (!isEnabled()) {
						Spookster.instance.eventManager.unregisterListener(this);
					}
				}
			}
		}
	}
}
