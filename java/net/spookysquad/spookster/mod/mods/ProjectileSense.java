package net.spookysquad.spookster.mod.mods;

import java.util.List;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.Event3DRender;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.mods.projectiles.Arrow;
import net.spookysquad.spookster.mod.mods.projectiles.Basic;
import net.spookysquad.spookster.mod.mods.projectiles.SplashPotion;
import net.spookysquad.spookster.mod.mods.projectiles.Throwable;
import net.spookysquad.spookster.utils.Render3DUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ProjectileSense extends Module {

	public ProjectileSense() {
		super(new String[] { "Projectile-Sense" }, "Show where a projectile is going to land.", Type.RENDER, Keyboard.KEY_NONE, 0xFFA06FA3);
	}

	private Throwable[] throwables = { new Arrow(), new SplashPotion(), new Basic() };

	double red = 0.6D, green = 0.6D, blue = 0.6D;

	public void onEvent(Event event) {
		if (event instanceof Event3DRender) {
			Event3DRender render = (Event3DRender) event;
			for (Entity e : (List<Entity>) getWorld().loadedEntityList) {
				if (e instanceof IProjectile) {
					if (isAllowed(e)) {
						renderProjectile(e);
					}
				} else if (e instanceof EntityPlayer) {
					WorldClient world = getWorld();
					EntityPlayer player = (EntityPlayer) e;
					if (player != getPlayer() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBow && player.isUsingItem()) {
						Throwable throwable = getThrowable(player, player.getCurrentEquippedItem());
						if (throwable == null) {
							return;
						} else if (throwable instanceof SplashPotion) {
							ItemPotion potion = (ItemPotion) player.getCurrentEquippedItem().getItem();
							int color = PotionHelper.calcPotionLiquidColor(potion.getEffects(player.getCurrentEquippedItem()));
							this.red = (float) (color >> 16 & 255) / 255.0F;
							this.green = (float) (color >> 8 & 255) / 255.0F;
							this.blue = (float) (color & 255) / 255.0F;
						} else if (throwable instanceof Arrow) {
							red = 1.0D;
							green = 0.9647D;
							blue = 0.5607D;
						} else {
							red = 0.6D;
							green = 0.0D;
							blue = 0.6D;
						}

						GL11.glPushMatrix();
						GL11.glLineWidth(2.0F);
						GL11.glColor3d(red, green, blue);
						GL11.glEnable(GL11.GL_LINE_SMOOTH);
						GL11.glEnable(GL11.GL_BLEND);
						GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glBegin(GL11.GL_LINE_STRIP);

						double x = (player.prevPosX + (player.posX - player.prevPosX) * render.getPartialTicks()) - Math.cos(getPlayer().rotationYaw / 180.0F * Math.PI) * 0.16D;
						double y = (player.prevPosY + (player.posY - player.prevPosY) * render.getPartialTicks()) + (player.isSneaking() ? 1.5395D : 1.6195D);
						double z = (player.prevPosZ + (player.posZ - player.prevPosZ) * render.getPartialTicks()) - Math.sin(getPlayer().rotationYaw / 180.0F * Math.PI) * 0.16D;
						double motionX = -Math.sin(player.rotationYaw / 180.0F * Math.PI) * Math.cos(player.rotationPitch / 180.0F * Math.PI);
						double motionZ = Math.cos(player.rotationYaw / 180.0F * Math.PI) * Math.cos(player.rotationPitch / 180.0F * Math.PI);
						double motionY = -Math.sin((player.rotationPitch + throwable.yOffset()) / 180.0F * Math.PI);
						vertex(x, y, z);
						float sqrt = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
						motionX /= sqrt;
						motionY /= sqrt;
						motionZ /= sqrt;
						motionX *= throwable.getPower(player);
						motionY *= throwable.getPower(player);
						motionZ *= throwable.getPower(player);
						MovingObjectPosition movingObjectPosition;

						for (;;) {
							float width = ((throwable instanceof Arrow) ? 0.5F : 0.25F) / 2.0F;
							float height = (throwable instanceof Arrow) ? 0.5F : 0.25F;
							AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x - width, y, z - width, x + width, y + height, z + width);
							Vec3 velocity = isInWater(boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D));
							boolean inWater = velocity != null;
							if (inWater) {
								velocity = velocity.normalize();
								motionX += velocity.xCoord * 0.014D;
								motionY += velocity.yCoord * 0.014D;
								motionZ += velocity.zCoord * 0.014D;
								boundingBox = boundingBox.addCoord(velocity.xCoord * 0.014D, velocity.yCoord * 0.014D, velocity.zCoord * 0.014D);
							}
							Vec3 present = Vec3.createVectorHelper(x, y, z);
							Vec3 future = Vec3.createVectorHelper(x + motionX, y + motionY, z + motionZ);
							movingObjectPosition = world.rayTraceBlocks(present, future, false, (throwable instanceof Arrow), false);
							present = Vec3.createVectorHelper(x, y, z);
							future = Vec3.createVectorHelper(x + motionX, y + motionY, z + motionZ);
							if (movingObjectPosition != null) {
								future = Vec3.createVectorHelper(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
							}
							List entities = world.getEntitiesWithinAABBExcludingEntity(player, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
							double var6 = 0.0D;
							for (int index = 0; index < entities.size(); index++) {
								Entity entity = (Entity) entities.get(index);
								if (entity instanceof EntityLivingBase && !(entity instanceof EntityEnderman) && entity.canBeCollidedWith() && entity != player) {
									boundingBox = entity.boundingBox.expand(0.3D, 0.3D, 0.3D);
									MovingObjectPosition hit = boundingBox.calculateIntercept(present, future);
									if (hit != null) {
										double distance = present.distanceTo(hit.hitVec);
										if ((distance < var6) || (var6 == 0.0D)) {
											var6 = distance;
											hit.entityHit = entity;
											movingObjectPosition = hit;
										}
									}
								}
							}
							x += motionX;
							y += motionY;
							z += motionZ;
							if (movingObjectPosition != null) {
								x = movingObjectPosition.hitVec.xCoord;
								y = movingObjectPosition.hitVec.yCoord;
								z = movingObjectPosition.hitVec.zCoord;
								break;
							}
							if (y <= -64.0D) { break; }
							float resistance = inWater ? 0.8F : .99F;
							float gravity = throwable.getGravity();
							motionY -= gravity;
							motionX *= resistance;
							motionY *= resistance;
							motionZ *= resistance;
							vertex(x, y, z);
						}
						vertex(x, y, z);
						GL11.glEnd();
						GL11.glDisable(GL11.GL_LINE_SMOOTH);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glEnable(GL11.GL_TEXTURE_2D);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glPopMatrix();

						if (movingObjectPosition != null) {
							if (movingObjectPosition.entityHit != null) {
								AxisAlignedBB boundingBox = movingObjectPosition.entityHit.boundingBox.expand(0.5D, 0.5D, 0.5D);
								GL11.glColor3d(red, green, blue);
								Render3DUtil.drawOutlineBox(boundingBox);
								GL11.glColor4d(red, green, blue, 0.125D);
								Render3DUtil.drawBox(boundingBox);
							} else {
								GL11.glPushMatrix();
								GL11.glEnable(GL11.GL_LINE_SMOOTH);
								GL11.glEnable(GL11.GL_BLEND);
								GL11.glDisable(GL11.GL_TEXTURE_2D);
								GL11.glDisable(GL11.GL_DEPTH_TEST);
								GL11.glDisable(GL11.GL_CULL_FACE);

								GL11.glTranslated(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);
								boolean isOntop = false;
								switch (movingObjectPosition.sideHit) {
								case 2:// east
								case 3:// west
									GL11.glDisable(GL11.GL_LIGHTING);
									GL11.glRotatef(90F, 1, 0, 0);
									break;
								case 4:// north
								case 5:// south
									GL11.glDisable(GL11.GL_LIGHTING);
									GL11.glRotatef(90F, 0, 0, 1);
									break;
								default:
									GL11.glRotatef(-(player.prevRotationYawHead + (player.rotationYawHead - player.rotationYawHead) * render.getPartialTicks()), 0.0F, 1.0F, 0.0F);
								case 1:
									if (player.getHeldItem().getItem() instanceof ItemEnderPearl) isOntop = true;
								}
								GL11.glTranslated(-(x - RenderManager.renderPosX), -(y - RenderManager.renderPosY), -(z - RenderManager.renderPosZ));

								if (isOntop) GL11.glColor3d(red, green + 0.5, blue + 0.3);
								else GL11.glColor3d(red, green, blue);
								GL11.glBegin(GL11.GL_LINE_LOOP);
								vertex(x + 0.5D, y, z - 0.5D);
								vertex(x + 0.5D, y, z + 0.5D);
								vertex(x - 0.5D, y, z + 0.5D);
								vertex(x - 0.5D, y, z - 0.5D);
								GL11.glEnd();

								GL11.glColor4d(red, green, blue, 0.125D);
								GL11.glBegin(GL11.GL_QUADS);
								vertex(x + 0.5D, y, z - 0.5D);
								vertex(x + 0.5D, y, z + 0.5D);
								vertex(x - 0.5D, y, z + 0.5D);
								vertex(x - 0.5D, y, z - 0.5D);
								GL11.glEnd();
								GL11.glEnable(GL11.GL_CULL_FACE);
								GL11.glDisable(GL11.GL_LINE_SMOOTH);
								GL11.glDisable(GL11.GL_BLEND);
								GL11.glEnable(GL11.GL_TEXTURE_2D);
								GL11.glEnable(GL11.GL_DEPTH_TEST);
								GL11.glPopMatrix();
							}
						}
					}
				}
			}
		}
	}

	private void renderProjectile(Entity projectile) {
		if (projectile instanceof EntityArrow) {
			EntityArrow arrow = (EntityArrow) projectile;
			red = 1.0D;
			green = 0.9647D;
			blue = 0.5607D;
		} else if (projectile instanceof EntityEnderPearl) {
			red = 0.6D;
			green = 0.0D;
			blue = 0.6D;
		} else if (projectile instanceof EntityPotion) {
			EntityPotion ePotion = (EntityPotion) projectile;
			int color = PotionHelper.calcPotionLiquidColor(Items.potionitem.getEffects(ePotion.getPotionDamage()));
			this.red = (float) (color >> 16 & 255) / 255.0F;
			this.green = (float) (color >> 8 & 255) / 255.0F;
			this.blue = (float) (color & 255) / 255.0F;
		}
		GL11.glPushMatrix();
		GL11.glLineWidth(1.5F);
		GL11.glColor3d(red, green, blue);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		double x = projectile.posX, y = projectile.posY, z = projectile.posZ;
		double motionX = projectile.motionX, motionY = projectile.motionY, motionZ = projectile.motionZ;
		MovingObjectPosition movingObjectPosition;
		vertex(x, y, z);
		for (;;) {
			float width = ((projectile instanceof EntityArrow) ? 0.5F : 0.25F) / 2.0F;
			float height = (projectile instanceof EntityArrow) ? 0.5F : 0.25F;
			AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(x - width, y, z - width, x + width, y + height, z + width);
			Vec3 velocity = isInWater(boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D));
			boolean inWater = velocity != null;
			if (inWater) {
				velocity = velocity.normalize();
				motionX += velocity.xCoord * 0.014D;
				motionY += velocity.yCoord * 0.014D;
				motionZ += velocity.zCoord * 0.014D;
				boundingBox = boundingBox.addCoord(velocity.xCoord * 0.014D, velocity.yCoord * 0.014D, velocity.zCoord * 0.014D);
			}

			Vec3 present = Vec3.createVectorHelper(x, y, z);
			Vec3 future = Vec3.createVectorHelper(x + motionX, y + motionY, z + motionZ);
			movingObjectPosition = getWorld().rayTraceBlocks(present, future, false, (projectile instanceof EntityArrow), false);
			if (movingObjectPosition != null) {
				future = Vec3.createVectorHelper(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
			}
			List entities = getWorld().getEntitiesWithinAABBExcludingEntity(getPlayer(), boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;
			for (int index = 0; index < entities.size(); index++) {
				Entity entity = (Entity) entities.get(index);
				if (entity instanceof EntityLivingBase && !(entity instanceof EntityEnderman) && entity.canBeCollidedWith() && entity != getPlayer()) {
					boundingBox = entity.boundingBox.expand(0.3D, 0.3D, 0.3D);
					MovingObjectPosition hit = boundingBox.calculateIntercept(present, future);
					if (hit != null) {
						double distance = present.distanceTo(hit.hitVec);
						if ((distance < var6) || (var6 == 0.0D)) {
							var6 = distance;
							hit.entityHit = entity;
							movingObjectPosition = hit;
						}
					}
				}
			}

			x += motionX;
			y += motionY;
			z += motionZ;

			if (movingObjectPosition != null) {
				x = movingObjectPosition.hitVec.xCoord;
				y = movingObjectPosition.hitVec.yCoord;
				z = movingObjectPosition.hitVec.zCoord;
				break;
			}
			if (y < -128) break;

			motionX *= projectile.isInWater() ? 0.8D : 0.99D;
			motionY *= projectile.isInWater() ? 0.8D : 0.99D;
			motionZ *= projectile.isInWater() ? 0.8D : 0.99D;
			motionY -= 0.05D;
			vertex(x + motionX, y + motionY, z + motionZ);
		}

		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}

	private void vertex(double x, double y, double z) {
		GL11.glVertex3d(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ);
	}

	private boolean isAllowed(Entity ent) {
		return ent != null && !ent.onGround && !ent.isCollided && !ent.isDead && getWorld().loadedEntityList.contains(ent) && !ent.isCollidedHorizontally && !ent.isCollidedVertically;
	}

	private Vec3 isInWater(AxisAlignedBB boundingBox) {
		int minX = MathHelper.floor_double(boundingBox.minX);
		int maxX = MathHelper.floor_double(boundingBox.maxX + 1.0D);
		int minY = MathHelper.floor_double(boundingBox.minY);
		int maxY = MathHelper.floor_double(boundingBox.maxY + 1.0D);
		int minZ = MathHelper.floor_double(boundingBox.minZ);
		int maxZ = MathHelper.floor_double(boundingBox.maxZ + 1.0D);
		if (!Minecraft.getMinecraft().theWorld.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ)) { return null; }
		Vec3 velocity = null;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					if ((Wrapper.getWorld().getBlock(x, y, z).getMaterial() == Material.water) && (maxY >= y + 1 - BlockLiquid.getLiquidHeightPercent(Wrapper.getWorld().getBlockMetadata(x, y, z)))) {
						if (velocity == null) {
							velocity = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
						}
						Wrapper.getWorld().getBlock(x, y, z).velocityToAddToEntity(Minecraft.getMinecraft().theWorld, x, y, z, null, velocity);
					}
				}
			}
		}
		return velocity;
	}

	private Throwable getThrowable(EntityPlayer player, ItemStack item) {
		if ((item == null) || (item.getItem() == null)) { return null; }
		for (Throwable throwable : this.throwables) {
			if (throwable.checkItem(item)) { return throwable; }
		}
		return null;
	}

}
