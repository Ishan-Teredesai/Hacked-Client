package net.spookysquad.spookster.mod.mods;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.utils.GameUtil;
import net.spookysquad.spookster.utils.PacketUtil;

public class FastUse extends Module {

	public FastUse() {
		super(new String[] { "FastUse" }, "Use items faster", Type.EXPLOITS, -1, -13845340);
	}
	
	public void onEvent(Event event) {
		if(event instanceof EventPreMotion) {
			if(getPlayer().getItemInUse() != null) {
				if (getPlayer().getItemInUseDuration() == 11) {
					for (int i = 0; i < 25; i++) PacketUtil.addPacket(new C03PacketPlayer(true));
					PacketUtil.sendPacket(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
					getPlayer().stopUsingItem();
				}
			}
		}
	}
	
	public boolean onEnable() {
		return super.onEnable();
	}
	
	public boolean onDisable() {
		return super.onDisable();
	}

}
