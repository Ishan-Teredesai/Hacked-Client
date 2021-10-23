package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.Event3DRender;
import net.spookysquad.spookster.event.events.EventBoundingBox;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.mod.values.Value.ValueType;
import net.spookysquad.spookster.utils.TimeUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Step extends Module implements HasValues {

	public Step() {
		super(new String[] { "Step" }, "Lets you Step up blocks", Type.MOVEMENT, Keyboard.KEY_PERIOD, 0xFF8BFFA1);
	}

	TimeUtil timeBypass = TimeUtil.getTime();

	public void onEvent(Event event) {
		double size = 8;
		if (event instanceof EventPreMotion) {
			if (jumpStep) {
				if (getPlayer().motionX != 0) mX = getPlayer().motionX;
				if (getPlayer().motionZ != 0) mZ = getPlayer().motionZ;

				AxisAlignedBB bb = getPlayer().boundingBox.copy().offset(mX * size, 0, mZ * size).contract(0, 0.5, 0);
				bb.maxY -= 1;
				if ((canJumpOnBlock(bb) || getPlayer().isCollidedHorizontally) && getPlayer().onGround) {
					getPlayer().jump();
				}
			} else if (vanillaStep) {
				Wrapper.getPlayer().stepHeight = (float) stepHeight;
			} else {
				Wrapper.getPlayer().stepHeight = 0.6F;
			}
		} else if (event instanceof EventBoundingBox && bypassStep) {
			EventBoundingBox e = (EventBoundingBox) event;
			if (e.getBoundingBox() == null) return;
			if (!getPlayer().onGround) return;
			if (e.getBlock() instanceof BlockStairs) {
				BlockStairs stairs = (BlockStairs) e.getBlock();
				if (MathHelper.floor_double(getPlayer().boundingBox.minY) == e.getY() && String.valueOf(getPlayer().posY).contains(".5")) return;
			}
			if (MathHelper.floor_double(getPlayer().boundingBox.minY) - 1D >= e.getY()) return;
			if (MathHelper.floor_double(getPlayer().boundingBox.minY) != e.getY()) return;
			if (e.getBlock() == Blocks.air || e.getBlock() == Blocks.ladder || e.getBlock() == Blocks.vine || e.getBlock() instanceof BlockSlab) return;
			if (getPlayer().handleLavaMovement() || getPlayer().isInWater()) return;
			if (!getPlayer().isCollidedVertically) return;
			if (e.getBoundingBox().maxY - getPlayer().boundingBox.minY < 0.6) return;
			if (!getGameSettings().keyBindJump.isPressed()) {
				getPlayer().motionX /= 1.5;
				getPlayer().motionZ /= 1.5;
				getPlayer().stepHeight = 1.25F;
				double offset = e.getBoundingBox().maxY + 0.065;
				e.setBoundingBox(AxisAlignedBB.getBoundingBox(e.getBoundingBox().minX, e.getBoundingBox().minY, e.getBoundingBox().minZ, e.getBoundingBox().maxX, offset, e.getBoundingBox().maxZ));
			}
		}
	}

	public boolean onDisable() {
		Wrapper.getPlayer().stepHeight = 0.6F;
		return super.onDisable();
	}

	private double mX, mZ;

	public boolean canJumpOnBlock(AxisAlignedBB bb) {
		int minX = MathHelper.floor_double(bb.minX);
		int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
		int minY = MathHelper.floor_double(bb.minY);
		int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
		int minZ = MathHelper.floor_double(bb.minZ);
		int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z) {
					if (getWorld().getBlock(x, y + 2, z).getCollisionBoundingBoxFromPool(getWorld(), x, y + 2, z) == null && getWorld().getBlock(x, y + 1, z).getCollisionBoundingBoxFromPool(getWorld(), x, y + 1, z) == null
							&& getWorld().getBlock(x, y, z).getCollisionBoundingBoxFromPool(getWorld(), x, y, z) != null && (getWorld().getBlock(x, y, z).getBlockBoundsMaxY() - getWorld().getBlock(x, y, z).getBlockBoundsMinY() > 0.5)) { return true; }
				}
			}
		}

		return false;
	}

	public boolean vanillaStep = true;
	public boolean jumpStep = false;
	public boolean bypassStep = false;
	public double stepHeight = 0.5;

	private String STEPHEIGHT = "Step Height", VANILLA = "Vanilla Step", JUMP = "Jump step", BYPASS = "Bypass step", STEPMODE = "Step Mode";
	private List<Value> values = Arrays.asList(new Value[] { new Value(STEPHEIGHT, 0.5D, 10D, 0.1F),
			new Value(STEPMODE, false, Arrays.asList(new Value(VANILLA, false, true), new Value(JUMP, false, true), new Value(BYPASS, false, true)), ValueType.MODE) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(STEPHEIGHT)) return stepHeight;
		else if (n.equals(VANILLA)) return vanillaStep;
		else if (n.equals(JUMP)) return jumpStep;
		else if (n.equals(BYPASS)) return bypassStep;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(STEPHEIGHT)) stepHeight = (Math.round((Double) v * 10) / 10.0D);
		else if (n.equals(VANILLA)) vanillaStep = (Boolean) v;
		else if (n.equals(JUMP)) jumpStep = (Boolean) v;
		else if (n.equals(BYPASS)) bypassStep = (Boolean) v;
	}

}
