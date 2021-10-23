package net.spookysquad.spookster.mod.mods.projectiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.spookysquad.spookster.utils.Wrapper;

public class Arrow implements Throwable {

	public boolean checkItem(ItemStack item) {
		return item.getItem() == Items.bow;
	}
	
	public float yOffset() {
		return 0;
	}
	
	public float getPower(EntityPlayer player) {
		float power = (float) (72000 - player.getItemInUseCount()) / 20.0F;
		power = (power * power + power * 2) / 3;
		if(power < 0.1) {
			power = 1;
		}
		if(power > 1) {
			power = 1;
		}
		return power * 2 * 1.5F;
	}
	
	public float getGravity() {
		return 0.05f;
	}
}
