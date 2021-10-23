package net.spookysquad.spookster.injection;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.events.Event3DRender;
import net.spookysquad.spookster.event.events.EventAttackEntity;
import net.spookysquad.spookster.event.events.EventBoundingBox;
import net.spookysquad.spookster.event.events.EventDestroyBlock;
import net.spookysquad.spookster.event.events.EventHardnessBlock;
import net.spookysquad.spookster.event.events.EventInOpaqueBlock;
import net.spookysquad.spookster.event.events.EventPacketSend;
import net.spookysquad.spookster.event.events.EventPostMotion;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.event.events.EventPushOutOfBlocks;
import net.spookysquad.spookster.event.events.EventRenderNameTag;
import net.spookysquad.spookster.event.events.EventShutdown;
import net.spookysquad.spookster.mod.mods.XRay;
import net.spookysquad.spookster.utils.PacketUtil;

import com.mumfrey.liteloader.transformers.event.EventInfo;
import com.mumfrey.liteloader.transformers.event.ReturnEventInfo;

public class GameEvents {

	public static void preMotionUpdateEvent(final EventInfo<EntityClientPlayerMP> e) {
		final EventPreMotion event = new EventPreMotion();
		Spookster.instance.eventManager.callEvent(event);
		if (event.isCancelled()) {
			e.cancel();
		}
	}

	public static void postMotionUpdateEvent(final EventInfo<EntityClientPlayerMP> e) {
		final EventPostMotion event = new EventPostMotion();
		Spookster.instance.eventManager.callEvent(event);
	}

	public static void isEntityInsideOpaqueBlockEvent(final ReturnEventInfo<Entity, Boolean> e) {
		final EventInOpaqueBlock event = new EventInOpaqueBlock();
		Spookster.instance.eventManager.callEvent(event);
		if (event.isCancelled()) {
			e.setReturnValue(false);
		}
	}

	public static void onPushOutOfBlocksEvent(final ReturnEventInfo<EntityPlayerSP, Boolean> e, final double arg1, final double arg2, final double arg3) {
		final EventPushOutOfBlocks event = new EventPushOutOfBlocks();
		Spookster.instance.eventManager.callEvent(event);
		if (event.isCancelled()) {
			e.setReturnValue(false);
		}
	}

	public static void onRenderBlockByRenderTypeEvent(final ReturnEventInfo<RenderBlocks, Boolean> e, final Block block, final int x, final int y, final int z) {
		e.getSource().setRenderAllFaces(false);
		if (Spookster.instance.moduleManager.getModule(XRay.class).isEnabled()) {
			if (!XRay.blocks.contains(block)) {
				e.setReturnValue(false);
				return;
			}
			e.getSource().setRenderAllFaces(true);
		}
	}

	public static void onGetPlayerRelativeBlockHardnessEvent(final ReturnEventInfo<Block, Float> e, final EntityPlayer thePlayer, final World theWorld, final int x, final int y, final int z) {
		final Block block = theWorld.getBlock(x, y, z);
		float hardness = block.getBlockHardness(theWorld, x, y, z);
		final EventHardnessBlock event = new EventHardnessBlock(block, hardness);
		Spookster.instance.eventManager.callEvent(event);
		hardness = event.getHardness();
		e.setReturnValue((((hardness < 0.0f) ? 0.0f : (thePlayer.canHarvestBlock(block) ? (thePlayer.getBreakSpeed(block, true) / hardness / 30.0f) : (thePlayer.getBreakSpeed(block, false) / hardness / 100.0f)))));
	}

	public static void onPassSpecialRenderEvent(final EventInfo<RendererLivingEntity> e, final EntityLivingBase arg1, final double arg2, final double arg3, final double arg4) {

		final EventRenderNameTag event = new EventRenderNameTag(arg1, arg2, arg3, arg4);
		Spookster.instance.eventManager.callEvent(event);
		if (event.isCancelled()) {
			e.cancel();
		}
	}

	public static void onRenderLivingLabelEvent(final EventInfo<Render> e, final Entity arg1, final String arg2, final double arg3, final double arg4, final double arg5, final int arg6) {
	}

	public static void onAddToSendQueueEvent(EventInfo<NetHandlerPlayClient> e, Packet packet) {
		final EventPacketSend event = new EventPacketSend(packet);
		Spookster.instance.eventManager.callEvent(event);
		if (!event.isCancelled()) PacketUtil.sendPacket(event.getPacket());
		e.cancel();
	}

	public static void onAddCollisionEvent(EventInfo<Block> e, World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity) {

		AxisAlignedBB var8 = e.getSource().getCollisionBoundingBoxFromPool(world, x, y, z);
		if (entity == Minecraft.getMinecraft().thePlayer) {
			EventBoundingBox event = new EventBoundingBox(var8, e.getSource(), x, y, z);
			Spookster.instance.eventManager.callEvent(event);
			var8 = event.getBoundingBox();
			e.cancel();
		}
		if (var8 != null && bb.intersectsWith(var8)) {
			list.add(var8);
		}
	}

	public static void onAttackEntityEvent(EventInfo<PlayerControllerMP> e, EntityPlayer arg1, Entity arg2) {
		if (arg1.equals(Minecraft.getMinecraft().thePlayer)) {
			EventAttackEntity event = new EventAttackEntity(arg1, arg2);
			Spookster.instance.eventManager.callEvent(event);
			if (event.isCancelled()) e.cancel();
		}
	}

	public static void renderHand(EventInfo<EntityRenderer> e, float arg1, int arg2) {
		Event3DRender event = new Event3DRender(arg1, arg2);
		event.call();

		if (event.isCancelled()) e.cancel();
	}

	public static void onShutdownMinecraftApplet(EventInfo<Minecraft> e) {
		EventShutdown event = new EventShutdown();
		event.call();
	}

	public static void onPlayerDestroyBlock(ReturnEventInfo<PlayerControllerMP, ?> e, int posX, int posY, int posZ, int side) {
		EventDestroyBlock event = new EventDestroyBlock(posX, posY, posZ, side);
		event.call();
	}

}
