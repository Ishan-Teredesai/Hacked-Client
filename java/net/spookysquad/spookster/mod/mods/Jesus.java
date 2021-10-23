package net.spookysquad.spookster.mod.mods;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventBoundingBox;
import net.spookysquad.spookster.event.events.EventPacketSend;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.utils.PlayerUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class Jesus extends Module {

	private boolean shouldJesus = false;

	public Jesus() {
		super("Jesus", "Allows you to walk on the fucking water niggas", Type.MOVEMENT, Keyboard.KEY_J, 0xFF28E3FF);
	}

	int movingUpValue = 0;

	public void onEvent(Event e) {
		if (e instanceof EventBoundingBox) {
			EventBoundingBox event = (EventBoundingBox) e;
			if (event.getBlock() instanceof BlockLiquid) {
				float height = BlockLiquid.getLiquidHeightPercent(Wrapper.getWorld().getBlockMetadata(event.getX(), event.getY(), event.getZ()));
				shouldJesus = height < 0.6F;

				if (!getPlayer().isInsideOfMaterial(Material.water) && !getPlayer().isInsideOfMaterial(Material.lava) && PlayerUtil.getBlock(0.1D) instanceof BlockLiquid && !getPlayer().isSneaking() && getPlayer().fallDistance < 3.0F
						&& !getGameSettings().keyBindJump.getIsKeyPressed()) {
					getPlayer().motionY = 0.1D;
				}

				if (shouldJesus && !getPlayer().isSneaking() && !getPlayer().isInWater() && getPlayer().posY > (double) event.getY() - 0.05D)
					event.setBoundingBox(AxisAlignedBB.getBoundingBox((double) event.getX(), (double) event.getY(), (double) event.getZ(), (double) ((float) event.getX() + 1.0F),
							(double) ((float) event.getY() + (Wrapper.getPlayer().fallDistance >= 3.0F ? 0.55F : 0.99F)), (double) ((float) event.getZ() + 1.0F)));
			}

		} else if (e instanceof EventPacketSend) {
			EventPacketSend send = (EventPacketSend) e;

			if (send.getPacket() instanceof C03PacketPlayer) {
				
				C03PacketPlayer origPacket = (C03PacketPlayer) send.getPacket();
				
				if (shouldJesus && PlayerUtil.getBlock(0.0D) instanceof BlockLiquid && !getPlayer().isSneaking() && !(PlayerUtil.getBlock(0.1D) instanceof BlockLiquid) && getPlayer().ticksExisted % 2 == 0 && (getPlayer().motionX != 0.0D || getPlayer().motionZ != 0.0D))
                {
                    send.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().boundingBox.minY + 0.1D, getPlayer().posY + 0.1D, getPlayer().posZ, false));
                }

			}

			/*
			 * if (event1.getState() == EventManager.EventState.SEND &&
			 * event1.getPacket() instanceof C03PacketPlayer) { C03PacketPlayer
			 * player1 = (C03PacketPlayer)event1.getPacket();
			 * 
			 * if (this.allow && this.getBlock(0.0D) instanceof BlockLiquid &&
			 * !this.mc.thePlayer.isSneaking() && !(this.getBlock(0.1D)
			 * instanceof BlockLiquid) && this.mc.thePlayer.ticksExisted % 2 ==
			 * 0 && (this.mc.thePlayer.motionX != 0.0D ||
			 * this.mc.thePlayer.motionZ != 0.0D)) { event1.cancel();
			 * this.forcePacket(new
			 * C03PacketPlayer.C04PacketPlayerPosition(player1.getPositionX(),
			 * player1.getPositionY() + 0.1D, player1.getPositionZ(), false)); }
			 * }
			 */
		}
	}

	public static boolean isOnLiquid(EntityPlayer player) {
		boolean onLiquid = false;
		int y = (int) player.boundingBox.copy().offset(0, -0.01, 0).minY;
		for (int x = MathHelper.floor_double(player.boundingBox.minX); x < MathHelper.floor_double(player.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(player.boundingBox.minZ); z < MathHelper.floor_double(player.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.getMinecraft().theWorld.getBlock(x, y, z);
				if (block == null || block instanceof BlockAir) {
					continue;
				}

				if (!(block instanceof BlockLiquid)) { return false; }

				onLiquid = true;
			}
		}
		return onLiquid;
	}

}
