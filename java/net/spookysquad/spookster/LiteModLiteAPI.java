package net.spookysquad.spookster;

import java.io.File;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.OutboundChatFilter;
import com.mumfrey.liteloader.PacketHandler;
import com.mumfrey.liteloader.PostRenderListener;
import com.mumfrey.liteloader.Tickable;

/**
 * We'll use this as a template to paste into "hiding" the mod into another mod
 * i.e TabbyChat, WorldEditWrapper, PotionColoriezer ;)
 * 
 * @author TehNeon
 */
public class LiteModLiteAPI implements Tickable, PacketHandler, HUDRenderListener, OutboundChatFilter, PostRenderListener {

	private Spookster spookster = new Spookster();

	public String getVersion() {
		return spookster.getVersion();
	}

	public String getName() {
		return spookster.getName();
	}

	public void init(File configPath) {
		spookster.init(configPath);
	}

	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		spookster.onTick(minecraft, partialTicks, inGame, clock);
	}

	public List<Class<? extends Packet>> getHandledPackets() {
		return spookster.getHandledPackets();
	}

	public boolean handlePacket(INetHandler netHandler, Packet packet) {
		return spookster.handlePacket(netHandler, packet);
	}

	public void onPreRenderHUD(int screenWidth, int screenHeight) {
		spookster.onPreRenderHUD(screenWidth, screenHeight);
	}
	
	public void onPostRenderHUD(int screenWidth, int screenHeight) {
		spookster.onPostRenderHUD(screenWidth, screenHeight);
	}

	@Override
	public boolean onSendChatMessage(String message) {
		return spookster.onSendChatMessage(message);
	}

	@Override
	public void onPostRenderEntities(float partialTicks) {
		spookster.onPostRenderEntities(partialTicks);
	}

	@Override
	public void onPostRender(float partialTicks) {}


}
