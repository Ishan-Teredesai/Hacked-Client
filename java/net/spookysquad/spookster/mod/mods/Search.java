package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.Event3DRender;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.utils.Render3DUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Search extends Module implements HasValues {

	public Search() {
		super(new String[] { "SearchTile" }, "Lets you see blocks that you want to see or some shit.", Type.RENDER, Keyboard.KEY_NONE, 0xFFaa87da);
	}

	public void onEvent(Event event) {
		if (event instanceof Event3DRender) {
			Event3DRender render = (Event3DRender) event;

			for (Object o : getWorld().loadedTileEntityList) {
				if (o instanceof TileEntity) {
					TileEntity tile = (TileEntity) o;
					drawOn(tile, tile.xCoord, tile.yCoord - 0.1, tile.zCoord);
				}
			}

		}
	}

	public void drawOn(TileEntity entity, double x, double y, double z) {
		double r = 1, g = 1, b = 1;
		boolean shouldDraw = false;

		if (entity instanceof TileEntityChest && chest) {
			shouldDraw = true;
			r = 0.75D;
			g = 0.75D;
			b = 0.12D;
		}

		if (entity instanceof TileEntityFurnace && furnace) {
			shouldDraw = true;
			r = 0.25D;
			g = 0.75D;
			b = 0.75D;
		}

		if (entity instanceof TileEntityDispenser && dispenser) {
			shouldDraw = true;
			r = 0.75D;
			g = 0.25D;
			b = 0.25D;
		}

		if (entity instanceof TileEntityHopper && hopper) {
			shouldDraw = true;
			r = 0.5D;
			g = 0.5D;
			b = 0.5D;
		}

		if (shouldDraw) {
			if(getPlayer().getDistance(x, y, z) <= renderDistance) {
				drawESP(x, y, z, r, g, b);
			}
		}
	}

	public void drawESP(double x, double y, double z, double r, double g, double b) {

		GL11.glPushMatrix();

		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);

		GL11.glDepthMask(false);
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
		GL11.glColor4d(r, g, b, innerOpacity);
		Render3DUtil.drawBox(boundingBox);
		GL11.glColor4d(r, g, b, outterOpacity);
		GL11.glLineWidth(1.0F);
		Render3DUtil.drawOutlineBox(boundingBox);
		GL11.glDepthMask(true);

		GL11.glDisable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glEnable(2929);

		GL11.glPopMatrix();

	}

	private double innerOpacity = 0.2D;
	private double outterOpacity = 1D;
	private int renderDistance = 32;
	private boolean chest = true;
	private boolean enderchest = true;
	private boolean furnace = true;
	private boolean dispenser = true;
	private boolean hopper = true;

	private String INNEROPACITY = "Inner Opacity", OUTTEROPACITY = "Outter Opacity", RENDERDISTANCE = "Render Distance", CHEST = "Show Chests", ENDERCHEST = "Show Enderchests", FURNACE = "Show Furnaces", DISPENSER = "Show Dispensers",
			HOPPER = "Show Hoppers";
	private List<Value> values = Arrays.asList(new Value[] { new Value(INNEROPACITY, 0D, 1D, 0.01F), new Value(OUTTEROPACITY, 0D, 1D, 0.01F), new Value(RENDERDISTANCE, 0, 256, 1), new Value(CHEST, false, true), new Value(ENDERCHEST, false, true),
			new Value(FURNACE, false, true), new Value(DISPENSER, false, true), new Value(HOPPER, false, true) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(INNEROPACITY)) return innerOpacity;
		else if (n.equals(OUTTEROPACITY)) return outterOpacity;
		else if (n.equals(RENDERDISTANCE)) return renderDistance;

		else if (n.equals(CHEST)) return chest;
		else if (n.equals(ENDERCHEST)) return enderchest;
		else if (n.equals(FURNACE)) return furnace;
		else if (n.equals(DISPENSER)) return dispenser;
		else if (n.equals(HOPPER)) return hopper;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(INNEROPACITY)) innerOpacity = (Math.round((Double) v * 100) / 100.0D);
		else if (n.equals(OUTTEROPACITY)) outterOpacity = (Math.round((Double) v * 100) / 100.0D);
		else if (n.equals(RENDERDISTANCE)) renderDistance = (Integer) v;

		else if (n.equals(CHEST)) chest = (Boolean) v;
		else if (n.equals(ENDERCHEST)) enderchest = (Boolean) v;
		else if (n.equals(FURNACE)) furnace = (Boolean) v;
		else if (n.equals(DISPENSER)) dispenser = (Boolean) v;
		else if (n.equals(HOPPER)) hopper = (Boolean) v;
	}

}
