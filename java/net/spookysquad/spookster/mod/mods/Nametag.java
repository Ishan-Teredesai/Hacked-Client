package net.spookysquad.spookster.mod.mods;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.Event3DRender;
import net.spookysquad.spookster.event.events.EventRenderNameTag;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.mods.Friends.Friend;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.render.FontUtil;
import net.spookysquad.spookster.render.GuiUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Nametag extends Module implements HasValues {

	private static final ResourceLocation ICON = new ResourceLocation("spookster", "textures/GExtra_Small_Ghost.png");
	
	public Nametag() {
		super(new String[] { "Nametag" }, "Enlarge nametags and other options.", Type.RENDER, Keyboard.KEY_M, 0xFFCD00CD);
	}

	public void onEvent(Event event) {
		if (event instanceof EventRenderNameTag) {
			EventRenderNameTag ev = (EventRenderNameTag) event;
			if (ev.getEntity() instanceof EntityPlayer) {
				ev.cancel();
			}
		} else if (event instanceof Event3DRender) {
			Event3DRender render = (Event3DRender) event;
			for (EntityPlayer player : (List<EntityPlayer>) getWorld().playerEntities) {
				if (player.getHealth() > 0 && !player.isDead && !player.getCommandSenderName().equals(getPlayer().getCommandSenderName())) {
					double eposX = player.lastTickPosX + (player.posX - player.lastTickPosX) * render.getPartialTicks();
					double eposY = player.lastTickPosY + (player.posY - player.lastTickPosY) * render.getPartialTicks();
					double eposZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * render.getPartialTicks();
					double x = eposX - RenderManager.renderPosX;
					double y = eposY - RenderManager.renderPosY;
					double z = eposZ - RenderManager.renderPosZ;
					drawTags(player, player.func_145748_c_().getFormattedText(), x, y, z);
				}
			}
		}
	}

	public boolean sneak = true;
	public boolean health = true;
	public boolean friends = true;
	public boolean invisibles = true;
	public double scaleFactor = 2.0D;
	public int scaleDistance = 10;

	public void drawTags(EntityLivingBase entity, String name, double x, double y, double z) {

		Friend fr = Friends.getFriend(entity.getCommandSenderName());

		if (fr != null && friends) {
			name = name.replaceAll(fr.getName(), fr.getAlias());
		}
		
		String othercrap = "";

		if (entity.isSneaking() && sneak) {
			othercrap = "\247c[S] ";
		}

		double theScale = scaleFactor;
		FontRenderer fontRenderer = Wrapper.getMinecraft().fontRendererObj;
		if (entity.getDistanceToEntity(Wrapper.getPlayer()) > scaleDistance) {
			theScale *= entity.getDistanceToEntity(Wrapper.getPlayer()) / scaleDistance;
		}
		double currentScale = 0.016666668F * theScale;
		GL11.glPushMatrix();
		RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glTranslated(x, y + entity.height + 0.5F, z);
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(getGameSettings().thirdPersonView == 2 ? -RenderManager.instance.playerViewX : RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScaled(-currentScale, -currentScale, currentScale);
		Tessellator var15 = Tessellator.instance;
		int var17 = (fontRenderer.getStringWidth(name + (health ? " " + getHealth(entity) : "")) / 2);
		var15.startDrawingQuads();
		var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, (float) opacity);
		if (entity.isInvisible() && invisibles) var15.setColorRGBA_F(0.5434F, 0.3379F, 0.1187F, (float) opacity);
		var15.addVertex((double) (-var17 - 1), (double) -1, 0.0D);
		var15.addVertex((double) (-var17 - 1), (double) 8, 0.0D);
		var15.addVertex((double) (var17 + 1), (double) 8, 0.0D);
		var15.addVertex((double) (var17 + 1), (double) -1, 0.0D);
		var15.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if(friends && fr != null) {
			float size = 8F;
			GuiUtil.drawTexturedRectangle(ICON, -var17 - size - 2, -1, size, size, 0xFFFFFFFF);
		}

		FontUtil.drawString(name, -(fontRenderer.getStringWidth(name + (health ? " " + getHealth(entity) : "")) / 2), 0, fr != null ? 0x00AAFF : 0xffffff);
		FontUtil.drawString(othercrap, (fontRenderer.getStringWidth(othercrap + name + (health ? " " + getHealth(entity) : "")) / 2) - 6, 0, fr != null ? 0x00AAFF : 0xffffff);
		if (health) FontUtil.drawString(getHealth(entity), (fontRenderer.getStringWidth(name + " " + getHealth(entity)) / 2) - fontRenderer.getStringWidth(getHealth(entity)), 0, getHealthColor(entity));
		
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	private int getHealthColor(EntityLivingBase entity) {
		int var8 = (int) Math.round(255.0D - (Double.valueOf(getHealth(entity)) * 2.0) * 255.0D / (double) entity.getMaxHealth());
		int var10 = 255 - var8 << 8 | var8 << 16;
		return var10;
	}

	private static String getHealth(EntityLivingBase entity) {
		int health = (int) Math.ceil(entity.getHealth());
		float maxHealth = entity.getMaxHealth();
		int nrhealth = (int) (health + 0.5F);
		float rhealth = (float) nrhealth / 2;
		String ihealth = String.valueOf(rhealth).replace(".0", "");
		return ihealth;
	}

	private double opacity = 0.6f;
	private String OPACITY = "Opacity", SNEAK = "Show Sneak", FRIENDS = "Show Friends", HEALTH = "Show Health", INVISIBLE = "Show Invisible", SCALEFACTOR = "Scale Factor", SCALEDISTANCE = "Scale Distance";
	private List<Value> values = Arrays.asList(new Value[] { new Value(OPACITY, 0.1D, 1D, 0.01F),new Value(SCALEFACTOR, 0.1D, 4D, 0.1F), new Value(SCALEDISTANCE, 0, 32, 1), new Value(SNEAK, false, true), new Value(FRIENDS, false, true), new Value(HEALTH, false, true),
			new Value(INVISIBLE, false, true) });

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(SCALEFACTOR)) return scaleFactor;
		else if (n.equals(SCALEDISTANCE)) return scaleDistance;
		else if (n.equals(SNEAK)) return sneak;
		else if (n.equals(FRIENDS)) return friends;
		else if (n.equals(HEALTH)) return health;
		else if (n.equals(INVISIBLE)) return invisibles;
		else if (n.equals(OPACITY)) return opacity;

		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(SNEAK)) sneak = (Boolean) v;
		else if (n.equals(FRIENDS)) friends = (Boolean) v;
		else if (n.equals(HEALTH)) health = (Boolean) v;
		else if (n.equals(INVISIBLE)) invisibles = (Boolean) v;
		else if (n.equals(SCALEFACTOR)) scaleFactor = (Math.round((Double) v * 10) / 10.0D);
		else if (n.equals(OPACITY)) opacity = (Math.round((Double) v * 100) / 100.0D);
		else if (n.equals(SCALEDISTANCE)) scaleDistance = (Integer) v;
	}
}
