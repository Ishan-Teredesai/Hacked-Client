package net.spookysquad.spookster.mod.mods.projectiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class SplashPotion implements Throwable {

	public boolean checkItem(ItemStack item) {
		if (item.getItem() != Items.potionitem) { return false; }
		return ItemPotion.isSplash(item.getMetadata());
	}

	public float yOffset() {
		return -20.0F;
	}

	public float getPower(EntityPlayer player) {
		return 0.5F;
	}

	public float getGravity() {
		return 0.05F;
	}
}
