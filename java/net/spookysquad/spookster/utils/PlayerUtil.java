package net.spookysquad.spookster.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.spookysquad.spookster.mod.mods.Friends;

public class PlayerUtil extends Wrapper {

	public static void inflictDamage(int blocks) {
		double amount = 1.0D;
		double fallDistance = 0;
		do {
			getPlayer().swingItem();
			PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX, getPlayer().boundingBox.minY
					+ amount, getPlayer().posY + amount, getPlayer().posZ, false));
			PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getPlayer().posX,
					getPlayer().boundingBox.minY, getPlayer().posY, getPlayer().posZ, false));
			if ((fallDistance += amount) >= blocks) break;
		} while (true);
	}

	public static Entity getEntityOnMouseCurser(double range) {
		Entity tempEntity = null;
		MovingObjectPosition object = getPlayer().rayTrace(range, 1.0F);
		double tempRange = range;
		Vec3 var6 = getPlayer().getPosition(1.0F);

		if (object != null) {
			tempRange = object.hitVec.distanceTo(var6);
		}

		Vec3 playerVec = getPlayer().getLook(1.0F);
		Vec3 extendedVec = var6.addVector(playerVec.xCoord * range, playerVec.yCoord * range, playerVec.zCoord * range);
		Vec3 var9 = null;
		List entities = getWorld().getEntitiesWithinAABBExcludingEntity(
				getPlayer(),
				getPlayer().boundingBox.addCoord(playerVec.xCoord * range, playerVec.yCoord * range,
						playerVec.zCoord * range).expand(1, 1, 1));
		double rangeAgain = tempRange;
		for (int var14 = 0; var14 < entities.size(); ++var14) {
			Entity var15 = (Entity) entities.get(var14);
			if (var15.canBeCollidedWith()) {
				float var16 = var15.getCollisionBorderSize();
				AxisAlignedBB var17 = var15.boundingBox.expand((double) var16, (double) var16, (double) var16);
				MovingObjectPosition var18 = var17.calculateIntercept(var6, extendedVec);
				if (var17.isVecInside(var6)) {
					if (0.0D < rangeAgain || rangeAgain == 0.0D) {
						tempEntity = var15;
						var9 = var18 == null ? var6 : var18.hitVec;
						rangeAgain = 0.0D;
					}
				} else if (var18 != null) {
					double var19 = var6.distanceTo(var18.hitVec);
					if (var19 < rangeAgain || rangeAgain == 0.0D) {
						if (var15 == getPlayer().ridingEntity) {
							if (rangeAgain == 0.0D) {
								tempEntity = var15;
								var9 = var18.hitVec;
							}
						} else {
							tempEntity = var15;
							var9 = var18.hitVec;
							rangeAgain = var19;
						}
					}
				}
			}
		}
		return tempEntity;
	}

	public static void attackEffectOnEntity(Entity livingbase) {
		if (livingbase.canAttackWithItem()) {
			if (!livingbase.hitByEntity(getPlayer())) {
				float attackDamage = (float) getPlayer().getEntityAttribute(SharedMonsterAttributes.attackDamage)
						.getAttributeValue();
				int sprinterInt = 0;
				float damageInt = 0.0F;
				if (livingbase instanceof EntityLivingBase) {
					damageInt = EnchantmentHelper.getEnchantmentModifierLiving(getPlayer(), (EntityLivingBase) livingbase);
					sprinterInt += EnchantmentHelper.getKnockbackModifier(getPlayer(), (EntityLivingBase) livingbase);
				}
				if (getPlayer().isSprinting()) {
					++sprinterInt;
				}
				if (attackDamage > 0.0F || damageInt > 0.0F) {
					boolean var5 = getPlayer().fallDistance > 0.0F && !getPlayer().onGround && !getPlayer().isOnLadder()
							&& !getPlayer().isInWater() && !getPlayer().isPotionActive(Potion.blindness)
							&& getPlayer().ridingEntity == null && livingbase instanceof EntityLivingBase;
					if (var5) {
						getPlayer().onCriticalHit(livingbase);
					}
					if (damageInt > 0.0F && new Random().nextInt(3) == 2) {
						getPlayer().onEnchantmentCritical(livingbase);
					}
					if (livingbase instanceof EntityLivingBase) {
						getPlayer().addStat(StatList.damageDealtStat, Math.round(attackDamage * 10.0F));
					}
				}
			}
		}
	}

	public static boolean canAttack(EntityPlayer targetplayer, double range, boolean swordOnly, boolean blockHit) {
		boolean hasSword = getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemSword;
		
		if (range == 0) {
			return targetplayer != null && targetplayer.getUniqueID() != null && targetplayer.getHealth() != 0
					&& !targetplayer.equals(getPlayer()) && !targetplayer.isPlayerSleeping()
					&& (swordOnly ? hasSword : true) && (blockHit ? true : !getPlayer().isUsingItem())
					&& getMinecraft().inGameHasFocus && !Friends.isFriend(targetplayer.getCommandSenderName()) && !Friends.sameTeam(targetplayer);
		} else {
			return targetplayer != null && getEntityOnMouseCurser(range) != null && targetplayer.getUniqueID() != null
					&& getEntityOnMouseCurser(range).equals(targetplayer) && targetplayer.getHealth() != 0
					&& !targetplayer.equals(getPlayer()) && !targetplayer.isPlayerSleeping()
					&& (swordOnly ? hasSword : true) && (blockHit ? true : !getPlayer().isUsingItem())
					&& getMinecraft().inGameHasFocus && !Friends.isFriend(targetplayer.getCommandSenderName()) && !Friends.sameTeam(targetplayer);
		}
	}

	public static boolean isMoving() {
		return getMinecraft().gameSettings.keyBindLeft.getIsKeyPressed()
				|| getMinecraft().gameSettings.keyBindRight.getIsKeyPressed()
				|| getMinecraft().gameSettings.keyBindForward.getIsKeyPressed()
				|| getMinecraft().gameSettings.keyBindBack.getIsKeyPressed();
	}

	public static Block getBlock(double offset) {
		return BlockUtil.getBlock(getPlayer().boundingBox.copy().offset(0.0D, offset, 0.0D));
	}

	public static boolean isInsideBlock() {
		if(getPlayer() != null) {
			AxisAlignedBB bb = getPlayer().boundingBox.copy();
	
			for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++) {
				for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; y++) {
					for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++) {
						Block block = getWorld().getBlock(x, y, z);
						if (block != null) {
							AxisAlignedBB boundingBox = block.getCollisionBoundingBoxFromPool(getWorld(), x, y, z);
							if (boundingBox != null && bb.intersectsWith(boundingBox)) { return true; }
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean canSprint() {
		return getPlayer().moveForward > 0 && !getPlayer().isSneaking() && !getPlayer().isCollidedHorizontally
				&& !getPlayer().isUsingItem() && !getPlayer().isSprinting() && !getPlayer().isPotionActive(Potion.blindness);
	}
}
