package net.spookysquad.spookster.event.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.spookysquad.spookster.event.Event;

public class EventBoundingBox extends Event {

	private AxisAlignedBB boundingbox;
	private Block block;
	private int x;
	private int y;
	private int z;

	public EventBoundingBox(AxisAlignedBB bb, Block block, int x, int y, int z) {
		this.boundingbox = bb;
		this.block = block;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setBoundingBox(AxisAlignedBB alignedBB) {
		this.boundingbox = alignedBB;
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingbox;
	}

	public Block getBlock() {
		return block;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
