package net.spookysquad.spookster.utils;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.injection.ObfuscationTable;
import net.spookysquad.spookster.mod.mods.Notifications;
import net.spookysquad.spookster.render.FontUtil;
import net.spookysquad.spookster.render.external.MainWindow;
import net.spookysquad.spookster.render.external.console.MessageType;

public class Wrapper {

	private static Minecraft mc = Minecraft.getMinecraft();

	/**
	 * TODO: Make an instance of a rapper, possibly force him to sing using
	 * nigerian folk tunes and then create a new class using his lyrics.
	 */

	public static Minecraft getMinecraft() {
		return mc;
	}

	public static EntityPlayerSP getPlayer() {
		return mc.thePlayer;
	}

	public static WorldClient getWorld() {
		return mc.theWorld;
	}

	public static GameSettings getGameSettings() {
		return mc.gameSettings;
	}

	public static PlayerControllerMP getController() {
		return mc.playerController;
	}

	public static FontRenderer getFont() {
		return mc.fontRendererObj;
	}

	public static ScaledResolution getSRes() {
		return new ScaledResolution(getMinecraft(), getMinecraft().displayWidth, getMinecraft().displayHeight);
	}

	public static void logChat(MessageType type, String text) {
		Spookster.logger.info("Notification: " + type.toString().toUpperCase() + ": " + text);
		MainWindow.mainConsole.addMessage(text, type);
		
		for(String msg: FontUtil.formatString(text, 500)) {
			Notifications.notifications.add((Entry<String, Long>) getEntry(msg, (long) (System.nanoTime() / 1E6)));
		}
	}

	public static Entry getEntry(Object o, Object o2) {
		SimpleEntry entry = new SimpleEntry(o, o2);
		return entry;
	}
}
