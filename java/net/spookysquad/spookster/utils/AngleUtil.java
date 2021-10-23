package net.spookysquad.spookster.utils;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class AngleUtil extends Wrapper {

	public static float[] getAngles(double posX, double posY, double posZ, Entity player) {
		double x = posX - player.posX;
		double z = posZ - player.posZ;
		double y = posY - player.posY;
		double dist = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);
		return (new float[] { yaw, pitch });
	}

	public static float[] getAngles(final Entity targetentity, final Entity forentity) {
		double x = targetentity.posX - forentity.posX;
		double z = targetentity.posZ - forentity.posZ;
		double y = targetentity.posY - forentity.posY;
		double dist = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(y, dist) * 180.0D / Math.PI);
		return (new float[] { yaw, pitch });
	}

	public static float getDistanceBetweenAngle(float clientpitch, float otherpitch) {
		float angle = Math.abs(otherpitch - clientpitch) % 360.0F;
		if (angle > 180.0F) {
			angle = 360.0F - angle;
		}
		return Math.abs(angle);
	}

	public static void smoothAim(EntityLivingBase e, int rate, boolean alsoPitch) {
		double xDist = e.posX - getPlayer().posX;
		double yDist = (e.posY + e.getEyeHeight() + 0.2) - getPlayer().posY;
		double zDist = e.posZ - getPlayer().posZ;
		double dist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);

		float yaw = (float) (Math.atan2(zDist, xDist) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(yDist, dist) * 180.0D / Math.PI);
		if (alsoPitch) getPlayer().rotationPitch = updateRotation(getPlayer().rotationPitch, pitch, rate);
		getPlayer().rotationYaw = updateRotation(getPlayer().rotationYaw, yaw, rate);
	}

	public static float updateRotation(float current, float newangle, float rate) {
		float var4 = MathHelper.wrapAngleTo180_float(newangle - current);
		if (var4 > rate) {
			var4 = rate;
		}
		if (var4 < -rate) {
			var4 = -rate;
		}
		return current + var4;
	}

	public static boolean shouldAim(int degree, EntityLivingBase livingbase) {
		float[] angles = getAngles(livingbase, getPlayer());
		float yawDist = getDistanceBetweenAngle(getPlayer().rotationYaw, angles[0]);
		if (yawDist > degree) { return true; }
		return false;
	}

}
