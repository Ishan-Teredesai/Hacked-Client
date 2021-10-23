package net.spookysquad.spookster.render;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex3d;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.opengl.GL11;

public class GuiUtil extends FontUtil {

	public static void drawGradientRect(double x1, double y1, double x2, double y2, int color1, int color2) {
		float var7 = (color1 >> 24 & 255) / 255.0F;
		float var8 = (color1 >> 16 & 255) / 255.0F;
		float var9 = (color1 >> 8 & 255) / 255.0F;
		float var10 = (color1 & 255) / 255.0F;
		float var11 = (color2 >> 24 & 255) / 255.0F;
		float var12 = (color2 >> 16 & 255) / 255.0F;
		float var13 = (color2 >> 8 & 255) / 255.0F;
		float var14 = (color2 & 255) / 255.0F;
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glDisable(GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glShadeModel(GL_SMOOTH);
		glBegin(GL_QUADS);
		glColor4d(var8, var9, var10, var7);
		glVertex3d(x2, y1, 0.0);
		glVertex3d(x1, y1, 0.0);
		glColor4d(var12, var13, var14, var11);
		glVertex3d(x1, y2, 0.0);
		glVertex3d(x2, y2, 0.0);
		glEnd();
		glShadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_TEXTURE_2D);
	}

	public static void drawRect(double par0, double par1, double par2, double par3, int par4) {
		double var5;
		if (par0 < par2) {
			var5 = par0;
			par0 = par2;
			par2 = var5;
		}
		if (par1 < par3) {
			var5 = par1;
			par1 = par3;
			par3 = var5;
		}
		float var10 = (par4 >> 24 & 255) / 255.0F;
		float var6 = (par4 >> 16 & 255) / 255.0F;
		float var7 = (par4 >> 8 & 255) / 255.0F;
		float var8 = (par4 & 255) / 255.0F;
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		glColor4f(var6, var7, var8, var10);
		glBegin(GL_QUADS);
		glVertex3d(par0, par3, 0.0D);
		glVertex3d(par2, par3, 0.0D);
		glVertex3d(par2, par1, 0.0D);
		glVertex3d(par0, par1, 0.0D);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}

	public static void drawBorderedRect(double x, double y, double x1, double y1, double size, int borderC, int insideC) {
		drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
		drawRect(x + size, y + size, x1, y, borderC);
		drawRect(x, y, x + size, y1, borderC);
		drawRect(x1, y1, x1 - size, y + size, borderC);
		drawRect(x + size, y1 - size, x1 - size, y1, borderC);
	}

	public static void drawTexturedRectangle(ResourceLocation resourceLocation, double posX, double posY, float width, float height, int color) {
		float u = 1, v = 1, uWidth = 1, vHeight = 1, textureWidth = 1, textureHeight = 1;
		glEnable(GL_BLEND);
		float red = (color >> 16 & 0xFF) / 255F;
		float green = (color >> 8 & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;
		float alpha = (color >> 24 & 0xFF) / 255F;
		Wrapper.getMinecraft().getTextureManager().bindTexture(resourceLocation);
		glColor4f(red, green, blue, alpha);
		glBegin(GL_QUADS);
		glTexCoord2d(u / textureWidth, v / textureHeight);
		glVertex2d(posX, posY);
		glTexCoord2d(u / textureWidth, (v + vHeight) / textureHeight);
		glVertex2d(posX, posY + height);
		glTexCoord2d((u + uWidth) / textureWidth, (v + vHeight) / textureHeight);
		glVertex2d(posX + width, posY + height);
		glTexCoord2d((u + uWidth) / textureWidth, v / textureHeight);
		glVertex2d(posX + width, posY);
		glEnd();
		glDisable(GL_BLEND);
	}

	public static void drawSexyRect(double posX, double posY, double posX2, double posY2, int col1, int col2) {
		drawRect(posX, posY, posX2, posY2, col2);
		float alpha = (col1 >> 24 & 0xFF) / 255F;
		float red = (col1 >> 16 & 0xFF) / 255F;
		float green = (col1 >> 8 & 0xFF) / 255F;
		float blue = (col1 & 0xFF) / 255F;
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_LINE_SMOOTH);
		glPushMatrix();
		glColor4f(red, green, blue, alpha);
		glLineWidth(2);
		glBegin(GL_LINES);
		glVertex2d(posX, posY);
		glVertex2d(posX, posY2);
		glVertex2d(posX2, posY2);
		glVertex2d(posX2, posY);
		glVertex2d(posX, posY);
		glVertex2d(posX2, posY);
		glVertex2d(posX, posY2);
		glVertex2d(posX2, posY2);
		glEnd();
		glPopMatrix();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDisable(GL_LINE_SMOOTH);
	}

	public static void drawCircle(double cx, double cy, float r, int num_segments) {
		double theta = (2 * 3.1415926 / num_segments), c = Math.cos(theta), s = Math.sin(theta), t, x = r, y = 0;
		GL11.glColor4d(1, 1, 1, 1);
		glBegin(GL11.GL_LINE_LOOP);
		for (int ii = 0; ii < num_segments; ii++) {
			GL11.glVertex2d(x + cx, y + cy);
			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		}
		glEnd();
	}

    public static void enableScissoring() {
        glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void scissor(final double x, final double y, final double w, final double h) {
        final ScaledResolution sr = getSRes();
        final int factor = sr.getScaleFactor();

        final double x2 = x + w, y2 = y + h;

        GL11.glScissor((int) (x * factor), (int) ((sr.getScaledHeight() - y2) * factor),
                (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void disableScissoring() {
        glDisable(GL11.GL_SCISSOR_TEST);
    }
	
}
