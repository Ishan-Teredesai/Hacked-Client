package net.spookysquad.spookster.event.events;

import net.minecraft.block.Block;
import net.spookysquad.spookster.event.Event;

public class EventHardnessBlock extends Event {

	private float hardness;
	private Block block;

	public EventHardnessBlock(Block block, float hardness) {
		this.block = block;
		this.hardness = hardness;
	}

	public Block getBlock() {
		return block;
	}

	public float getHardness() {
		return hardness;
	}
	
	public void setHardness(float hardness) {
		this.hardness = hardness;
	}
}
