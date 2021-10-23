package net.spookysquad.spookster.mod.mods.projectiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Basic implements Throwable {
	public boolean checkItem(ItemStack item) {
		return (item.getItem() == Items.snowball) || (item.getItem() == Items.egg) || (item.getItem() == Items.ender_pearl);
	}

	public float yOffset() {
		return 0.0F;
	}

	public float getPower(EntityPlayer player) {
		return 1.5F;
	}

	public float getGravity() {
		return 0.03F;
	}
}
