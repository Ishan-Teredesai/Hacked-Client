package net.spookysquad.spookster.utils;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

public class PacketUtil extends Wrapper {

	public static void addPacket(Packet packet) {
		Wrapper.getMinecraft().getNetHandler().addToSendQueue(packet);
	}

	public static void sendPacket(Packet packet) {
		Wrapper.getMinecraft().getNetHandler().getNetworkManager()
				.scheduleOutboundPacket(packet, new GenericFutureListener[0]);
	}

	public static void addPlayerLook(float yaw, float pitch, boolean onground) {
		addPacket(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, getPlayer().boundingBox.minY,
				getPlayer().posY, getPlayer().posZ, yaw, pitch, onground));
	}

	public static void sendPlayerLook(float yaw, float pitch, boolean onground) {
		sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX, getPlayer().boundingBox.minY,
				getPlayer().posY, getPlayer().posZ, yaw, pitch, onground));
	}

	public static void addPlayerOffset(double x, double y, double z, boolean onground) {
		addPacket(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX + x, getPlayer().boundingBox.minY + y,
				getPlayer().posY + y, getPlayer().posZ + z, getPlayer().rotationYaw, getPlayer().rotationPitch, onground));
	}

	public static void sendPlayerOffset(double x, double y, double z, boolean onground) {
		sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(getPlayer().posX + x, getPlayer().boundingBox.minY + y,
				getPlayer().posY + y, getPlayer().posZ + z, getPlayer().rotationYaw, getPlayer().rotationPitch, onground));
	}

	public static C06PacketPlayerPosLook forcedC06Packet(C03PacketPlayer packet) {
		if (packet instanceof C06PacketPlayerPosLook) {
			return (C06PacketPlayerPosLook) packet;
		} else if (packet instanceof C05PacketPlayerLook) {
			C05PacketPlayerLook packetC05 = (C05PacketPlayerLook) packet;
			return new C06PacketPlayerPosLook(getPlayer().posX, getPlayer().boundingBox.minY, getPlayer().posY,
					getPlayer().posZ, packetC05.getYaw(), packetC05.getPitch(), packetC05.func_149465_i());
		} else if (packet instanceof C04PacketPlayerPosition) {
			C04PacketPlayerPosition packetC04 = (C04PacketPlayerPosition) packet;
			return new C06PacketPlayerPosLook(packetC04.getPositionX(), packetC04.getPositionY(), packetC04.getStance(),
					packetC04.getPositionZ(), getPlayer().rotationYaw, getPlayer().rotationPitch, packetC04.func_149465_i());
		} else {
			return new C06PacketPlayerPosLook(getPlayer().posX, getPlayer().boundingBox.minY, getPlayer().posY,
					getPlayer().posZ, getPlayer().rotationYaw, getPlayer().rotationPitch, packet.func_149465_i());
		}
	}

}
