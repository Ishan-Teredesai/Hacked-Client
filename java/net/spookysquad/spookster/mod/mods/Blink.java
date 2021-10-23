package net.spookysquad.spookster.mod.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPacketSend;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.utils.PacketUtil;

import org.lwjgl.input.Keyboard;

public class Blink extends Module implements HasValues {

	private ArrayList<Packet> packets = new ArrayList<Packet>();
	
	public Blink() {
		super(new String[] { "Blink" }, "POOF! You're gone", Type.MOVEMENT, Keyboard.KEY_LBRACKET, 0xFFaa676a);
	}
	
	public boolean onEnable() {
		if(speedBlink) {
			if(Spookster.instance.moduleManager.getModule(Speed.class).isEnabled()) {
				speedEnabled = true;
			} else {
				Spookster.instance.moduleManager.getModule(Speed.class).toggle(false);
			}
		}
		
		return super.onEnable();
	}
	
	public boolean onDisable() {
		for(Packet packet: packets) {
			PacketUtil.sendPacket(packet);
		}
		
		if(speedBlink) {
			if(!speedEnabled) {
				Spookster.instance.moduleManager.getModule(Speed.class).toggle(false);
			}
		}
		
		packets.clear();
		speedEnabled = false;
		return super.onDisable();
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventPacketSend) {
			EventPacketSend packetSend = (EventPacketSend) event;
			if(packetSend.getPacket() instanceof C03PacketPlayer || packetSend.getPacket() instanceof C0BPacketEntityAction) {
				packets.add(packetSend.getPacket());
				event.cancel();
			}
		}
	}
	
	
	private boolean speedBlink = false;
	private boolean speedEnabled = false;
	
	private String SPEEDBLINK = "Speed Blink";
	private List<Value> values = Arrays.asList(new Value[] { new Value(SPEEDBLINK, false, true) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(SPEEDBLINK)) return speedBlink;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(SPEEDBLINK)) speedBlink = (Boolean) v;
	}

}
