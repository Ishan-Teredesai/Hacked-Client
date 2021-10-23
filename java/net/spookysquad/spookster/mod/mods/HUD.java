package net.spookysquad.spookster.mod.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPostHudRender;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.render.FontUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class HUD extends Module implements HasValues {

	public HUD() {
		super(new String[] { "HUD" }, "Displays the Heads up Display", Type.RENDER, Keyboard.KEY_H, -1);
	}

	public void onEvent(Event event) {
		if (event instanceof EventPostHudRender) {
			if (!Wrapper.getGameSettings().showDebugInfo) {
				FontUtil fontRenderer = new FontUtil();
				final FontRenderer fr = Wrapper.getFont();

				GL11.glPushMatrix();
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				fontRenderer.drawStringWithShadow("" + Spookster.clientName.charAt(0), 1, 1, 0xFFFFFFFF, 0.35F);
				GL11.glPopMatrix();
				fontRenderer.drawStringWithShadow(Spookster.clientName.substring(1, Spookster.clientName.length()), 10, 5, 0xFFFFFFFF, 0.7F);
				GL11.glPushMatrix();
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				fontRenderer.drawStringWithShadow("(§a" + Spookster.clientVersion + "§f)", 21, 3, 0xFFFFFFFF, 1.4F);
				GL11.glPopMatrix();

				ArrayList<Module> tempModList = new ArrayList<Module>();
				for (Module m : Spookster.instance.moduleManager.getModules()) {
					if (m.isEnabled() && m.getColor() != -1) {
						tempModList.add(m);
					}
				}

				if(sortList) {
					Comparator<Module> stringComparator = new Comparator<Module>() {
						public int compare(Module o1, Module o2) {
							try {
								if (fr.getStringWidth(o1.getDisplay()) > fr.getStringWidth(o2.getDisplay())) return -1;
								if (fr.getStringWidth(o2.getDisplay()) > fr.getStringWidth(o1.getDisplay())) return 1;
							} catch (Exception e) {
							}
	
							if (fr.getStringWidth(o1.getName()[0]) > fr.getStringWidth(o2.getName()[0])) return -1;
							if (fr.getStringWidth(o2.getName()[0]) > fr.getStringWidth(o1.getName()[0])) return 1;
							return 0;
						}
					};
					Collections.sort(tempModList, stringComparator);
				}

				if (!arrayRight) {
					int posY = 14;
					for (Module mod : tempModList) {
						if (mod.isEnabled()) {
							if (mod.getColor() != -1) {
								fontRenderer.drawStringWithShadow(mod.getDisplay(), 2, posY, mod.getColor(), 0.7F);
								posY += 9;
							}
						}
					}
				} else {
					int posY = 2;
					for (Module mod : tempModList) {
						if (mod.isEnabled()) {
							if (mod.getColor() != -1) {
								fontRenderer.drawStringWithShadow(mod.getDisplay(), ((EventPostHudRender) event).getScreenWidth() - fontRenderer.getFont().getStringWidth(mod.getDisplay()) - 2, posY, mod.getColor(), 0.7F);
								posY += 9;
							}
						}
					}
				}
			}
		}
	}

	public boolean arrayRight = false;
	public boolean sortList = true;
	
	private String ARRAY_POSITION = "Array Right", SORT_LIST = "Sort List";
	private List<Value> values = Arrays.asList(new Value[] { new Value(ARRAY_POSITION, false, true), new Value(SORT_LIST, false, true) });

	public List<Value> getValues() {
		return values;
	}

	public Object getValue(String n) {
		if (n.equals(ARRAY_POSITION)) return arrayRight;
		else if (n.equals(SORT_LIST)) return sortList;
		return null;
	}

	public void setValue(String n, Object v) {
		if (n.equals(ARRAY_POSITION)) arrayRight = Boolean.parseBoolean(v.toString());
		else if (n.equals(SORT_LIST)) sortList = Boolean.parseBoolean(v.toString());
	}

}
