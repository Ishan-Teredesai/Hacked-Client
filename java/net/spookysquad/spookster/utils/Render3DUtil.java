package net.spookysquad.spookster.utils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

public class Render3DUtil extends Wrapper {

	public static void orientCamera(float particalTick) {
		EntityLivingBase renderViewEntity = getMinecraft().renderViewEntity;
		float entityHeight = renderViewEntity.yOffset - 1.62F;
		double posX = renderViewEntity.prevPosX + (renderViewEntity.posX - renderViewEntity.prevPosX) * (double) particalTick;
		double posY = renderViewEntity.prevPosY + (renderViewEntity.posY - renderViewEntity.prevPosY) * (double) particalTick - (double) entityHeight;
		double posZ = renderViewEntity.prevPosZ + (renderViewEntity.posZ - renderViewEntity.prevPosZ) * (double) particalTick;
		if (renderViewEntity.isPlayerSleeping()) {
			entityHeight = (float) ((double) entityHeight + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			if (!getMinecraft().gameSettings.debugCamEnable) {
				Block block = getMinecraft().theWorld.getBlock(MathHelper.floor_double(renderViewEntity.posX), MathHelper.floor_double(renderViewEntity.posY), MathHelper.floor_double(renderViewEntity.posZ));
				if (block == Blocks.bed) {
					int metaData = getMinecraft().theWorld.getBlockMetadata(MathHelper.floor_double(renderViewEntity.posX), MathHelper.floor_double(renderViewEntity.posY), MathHelper.floor_double(renderViewEntity.posZ));
					int facing = metaData & 3;
					GL11.glRotatef((float) (facing * 90), 0.0F, 1.0F, 0.0F);
				}
				GL11.glRotatef(renderViewEntity.prevRotationYaw + (renderViewEntity.rotationYaw - renderViewEntity.prevRotationYaw) * particalTick + 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(renderViewEntity.prevRotationPitch + (renderViewEntity.rotationPitch - renderViewEntity.prevRotationPitch) * particalTick, -1.0F, 0.0F, 0.0F);
			}
		} else if (getMinecraft().gameSettings.thirdPersonView > 0) {
			double thridPersonDistance = 4;
			float rotationYaw = renderViewEntity.rotationYaw;
			float rotationPitch = renderViewEntity.rotationPitch;
			if (getMinecraft().gameSettings.thirdPersonView == 2) rotationPitch += 180.0F;
			double var14 = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI)) * thridPersonDistance;
			double var16 = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI)) * thridPersonDistance;
			double var18 = (double) (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI)) * thridPersonDistance;
			for (int renderer0 = 0; renderer0 < 8; ++renderer0) {
				float renderer1 = (float) ((renderer0 & 1) * 2 - 1);
				float renderer2 = (float) ((renderer0 >> 1 & 1) * 2 - 1);
				float renderer3 = (float) ((renderer0 >> 2 & 1) * 2 - 1);
				renderer1 *= 0.1F;
				renderer2 *= 0.1F;
				renderer3 *= 0.1F;
				MovingObjectPosition renderer4 = getMinecraft().theWorld.rayTraceBlocks(Vec3.createVectorHelper(posX + (double) renderer1, posY + (double) renderer2, posZ + (double) renderer3),
						Vec3.createVectorHelper(posX - var14 + (double) renderer1 + (double) renderer3, posY - var18 + (double) renderer2, posZ - var16 + (double) renderer3));
				if (renderer4 != null) {
					double renderer5 = renderer4.hitVec.distanceTo(Vec3.createVectorHelper(posX, posY, posZ));
					if (renderer5 < thridPersonDistance) {
						thridPersonDistance = renderer5;
					}
				}
			}
			if (getMinecraft().gameSettings.thirdPersonView == 2) GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(renderViewEntity.rotationPitch - rotationPitch, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(renderViewEntity.rotationYaw - rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0.0F, (float) (-thridPersonDistance));
			GL11.glRotatef(rotationYaw - renderViewEntity.rotationYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(rotationPitch - renderViewEntity.rotationPitch, 1.0F, 0.0F, 0.0F);
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}
		if (!getMinecraft().gameSettings.debugCamEnable) {
			GL11.glRotatef(renderViewEntity.prevRotationPitch + (renderViewEntity.rotationPitch - renderViewEntity.prevRotationPitch) * particalTick, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(renderViewEntity.prevRotationYaw + (renderViewEntity.rotationYaw - renderViewEntity.prevRotationYaw) * particalTick + 180.0F, 0.0F, 1.0F, 0.0F);
		}
	}

	public static void drawBox(AxisAlignedBB bb) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		GL11.glTranslated(0.0D, 0.1D, 0.0D);
		AxisAlignedBB aabb = bb.copy().offset(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
		final Tessellator renderer = Tessellator.instance;
		renderer.startDrawing(GL11.GL_QUAD_STRIP);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		renderer.draw();

		renderer.startDrawing(GL11.GL_QUAD_STRIP);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		renderer.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawOutlineBox(AxisAlignedBB bb) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDepthMask(false);
		GL11.glTranslated(0.0D, 0.1D, 0.0D);
		AxisAlignedBB aabb = bb.copy().offset(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
		final Tessellator renderer = Tessellator.instance;
		renderer.startDrawing(GL11.GL_LINE_STRIP);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		renderer.draw();
		renderer.startDrawing(GL11.GL_LINE_STRIP);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		renderer.draw();
		renderer.startDrawing(GL11.GL_LINES);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
		renderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
		renderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
		renderer.draw();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
