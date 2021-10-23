package net.spookysquad.spookster.render.clickgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClickScreen extends GuiScreen {

											
	public static int[] colors = new int[] {/*Background1*/0xFF34495E, /*enabled*/ 0xFF2ECC71, /*disabled*/ 0xFF2C3E50};
	public static boolean justSwitched = false;
	private static String showInfoString = "";
	protected static ClickScreen screen;
	private Module keyChangeModule;

	public ClickScreen() {
		screen = this;
	}

	public static ClickScreen getInstance() {
		if (screen == null) screen = new ClickScreen();
		return screen;
	}

	private static CopyOnWriteArrayList<ClickPanel> list = new CopyOnWriteArrayList<ClickPanel>();

	public void addPanel(ClickPanel panel) {
		list.add(panel);
	}

	public List<ClickPanel> getPanels() {
		return list;
	}

	public boolean isFirst(ClickPanel panel) {
		return panel.equals(list.get(0));
	}

	private void moveToFront(ClickPanel e) {
		list.remove(e);
		list.add(0, e);
	}

	@Override
	public void initGui() {
		int x = 2, x2 = 2;
		if (getPanels().isEmpty()) {
			for (Type type : Type.values()) {
				if (!type.getName().equals("") && !Spookster.instance.moduleManager.getModulesWithType(type).isEmpty()) {
					if (Wrapper.getSRes().getScaledWidth() - x < 0) {
						ClickPanel panel = new ClickPanel(this, type, x2, 100);
						addPanel(panel);
						x2 += panel.panelWidth + 10;
					} else {
						ClickPanel panel = new ClickPanel(this, type, x, 1);
						addPanel(panel);
						x += panel.panelWidth + 10;
					}
				}
			}
		}
	}

	@Override
	protected void keyTyped(final char c, final int i) {
		if (keyChangeModule != null) {
			Wrapper.logChat(MessageType.NOTIFCATION, "Changed binding of " + keyChangeModule.getDisplay() + " to " + (i == 1 ? "NONE" : Keyboard.getKeyName(i)));
			keyChangeModule.setKeyCode(i == 1 ? -1 : i);
			keyChangeModule = null;
		}else{
			if (i == 1) {
				mc.displayGuiScreen(null);
				return;
			}
		}
	}
	
	@Override
	public void onGuiClosed() {	
		Spookster.instance.safeClientToFile();
	}

	@Override
	protected void mouseClicked(int posX, int posY, int type) {
		if (keyChangeModule != null) {
			Wrapper.logChat(MessageType.NOTIFCATION, "Changed binding of " + keyChangeModule.getDisplay() + " to " + (type == 0 ? "NONE" : Mouse.getButtonName(type)));
			keyChangeModule.setKeyCode(type == 0 ? -1 : type + 256);
			keyChangeModule = null;
		}

		if (type == 0) {
			ClickPanel mtf = null;
			for (ClickPanel e : getPanels()) {
				e.firstClick(posX, posY);
				if (e.isTopBeingClicked(posX, posY)) {
					e.setBeingDragged(false);
					mtf = e;
					break;
				}
			}
			if (null != mtf) {
				moveToFront(mtf);
			}
		}
		super.mouseClicked(posX, posY, type);
	}

	@Override
	protected void mouseReleased(int posX, int posY, int type) {
		if (type == 0) {
			ClickPanel mtf = null;
			for (ClickPanel e : getPanels()) {
				boolean done = e.leftClickPanel(posX, posY);
				if (e.isBeingDragged()) {
					e.setBeingDragged(false);
				}
				if (done) {
					mtf = e;
					break;
				}
			}
			if (null != mtf) {
				moveToFront(mtf);
				return;
			}
		} else if (type == 1) {
			for (ClickPanel e : getPanels()) {
				e.rightClickPanel(posX, posY);
			}
		}
		super.mouseReleased(posX, posY, type);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (ClickPanel e : getPanels()) {
			if (e.dragPanel(mouseX, mouseY)) {
				break;
			}
		}

		List<ClickPanel> reversed = new ArrayList<ClickPanel>(getPanels());
		Collections.reverse(reversed);
		int x = mouseX * this.width / Wrapper.getMinecraft().displayWidth;
		int y = this.height - mouseY * this.height / Wrapper.getMinecraft().displayHeight - 1;
		drawPanels(reversed, x, y);
	}

	public void drawPanels(List<ClickPanel> reversed, int x, int y) {
		for (ClickPanel e : reversed) {
			if (e.getX() + ClickPanel.getWidth() / 2 < 0 || e.getX() > width) {
				if (!e.isBeingDragged()) {
					e.setPosition(100, e.getY());
				}
			}
			if (e.getY() < 0 || e.getY() > height) {
				if (!e.isBeingDragged()) {
					e.setPosition(e.getX(), 100);
				}
			}
			e.drawPanel(x, y);
		}
	}

	public void setModuleToChangeKey(Module module) {
		this.keyChangeModule = module;
	}

	public Module getModuleToChangeKey() {
		return this.keyChangeModule;
	}

}
