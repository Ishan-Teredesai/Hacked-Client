package net.spookysquad.spookster.render.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.render.FontUtil;
import net.spookysquad.spookster.render.GuiUtil;
import net.spookysquad.spookster.render.clickgui.ClickPanelItemFactory.EditablePanelItem;
import net.spookysquad.spookster.render.clickgui.ClickPanelItemFactory.NormalPanelItem;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Mouse;

public class ClickPanel extends FontUtil {

	protected String name;
	protected Type type;
	protected boolean state;
	protected double posX, posY, fposX, fposY, difX, difY;

	public int topHeight = 12;
	public int panelHeight = 15;
	public int panelWidth = 105;

	protected int timer = 0;
	protected boolean beingDragged = false;
	protected boolean BeingOpen = false;

	protected List<NormalPanelItem> panelItems = new ArrayList<NormalPanelItem>();
	protected ClickScreen screen;
	protected EditablePanelItem editablePanelItem;

	public ClickPanel(ClickScreen clickScreen, Type type, int posX, int posY) {
		this.screen = clickScreen;
		this.name = type.getName();
		this.type = type;
		this.posX = posX;
		this.posY = posY;
		for (Module e : Spookster.instance.moduleManager.getModulesWithType(type)) {
			NormalPanelItem item = ClickPanelItemFactory.createPanelItem(this, e);
			item.xWidth = panelWidth;
			panelItems.add(item);
		}
		setState(true);
	}

	public Type getType() {
		return type;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean b1) {
		this.state = b1;
	}

	public boolean isBeingDragged() {
		return beingDragged;
	}

	public void setBeingDragged(boolean beingDragged) {
		this.beingDragged = beingDragged;
	}

	public boolean isGoingToOpen() {
		return BeingOpen;
	}

	public void setGoingOpen(boolean beingOpen) {
		BeingOpen = beingOpen;
	}

	public void onToggle() {
		this.state = !this.state;
	}

	public double getDist(double x, double y) {
		double distX = x - fposX;
		double distY = y - fposY;
		return MathHelper.sqrt_double(distX * distX + distY * distY);
	}

	public void setPosition(double x, double y) {
		this.posX = x;
		this.posY = y;
	}

	public void drawPanel(int x, int y) {
		int width = Wrapper.getFont().getStringWidth(name) + 10;
		GuiUtil.drawSexyRect(posX, posY, posX + panelWidth, posY + panelHeight - 1, 0xaa808080, 0x00000000);
		GuiUtil.drawRect(posX, posY + topHeight, posX + panelWidth, posY + panelHeight - 1, ClickScreen.colors[0]);
		GuiUtil.drawRect(posX, posY, posX + panelWidth, posY + topHeight, ClickScreen.colors[0]);
		GuiUtil.drawRect(posX + panelWidth - topHeight, posY, posX + panelWidth, posY + topHeight, this.getState() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
		FontUtil.drawStringWithShadow(name, (float) posX + 1, (float) posY + 2F, 0xFFFFFFFF, 0.70F, 1.15F);

		if (this.getState()) {
			int h = topHeight + 4;
			for (NormalPanelItem i : this.getPanelItems()) {
				i.drawPanelItem(posX, posY + h, x, y);
				h += i.yHeight;
			}
			panelHeight = h - 3;
		} else {
			panelHeight = topHeight + 1;
		}
	}

	public boolean leftClickPanel(int x, int y) {
		int width = Wrapper.getFont().getStringWidth(name) + 19;
		timer = 0;
		if (y >= posY && y <= (posY + 10) && (x >= posX && x <= (posX + 78))) {
			if (screen.isFirst(this) && getDist(x, y) <= 10 && !isBeingDragged()) {
				this.onToggle();
			}
			return true;
		}
		if (getState()) {
			int h = topHeight + 4;
			for (NormalPanelItem item : panelItems) {
				if (item.handleClick(posX - 2, posY + h, x, y, getDist(x, y) <= 2)) {
					return true;
				}
				h += item.yHeight;
			}
		}
		return false;
	}
	
	public void rightClickPanel(int x, int y) {
		if (getState()) {
			int h = topHeight + 4;
			for (NormalPanelItem item : panelItems) {
				if (item.setListingKey(posX - 2, posY + h, x, y)) {
					return;
				}
				h += item.yHeight;
			}
		}
	}

	

	private int preX, preY;

	public boolean dragPanel(int x, int y) {
		if(!Mouse.isButtonDown(0)) {
			this.setBeingDragged(false);
			return false;
		}
			
		if (getState()) {
			int total = topHeight + 4;
			for (NormalPanelItem p : getPanelItems()) {
				if (p instanceof EditablePanelItem) {
					if (p instanceof EditablePanelItem) {
						EditablePanelItem se = (EditablePanelItem) p;
						if (p.handleDrag(posX - 2, posY + total, x, y))
							return true;
					}
				}
				total += p.yHeight;
			}
		}

		if (getDist(x, y) >= 2 || isBeingDragged()) {
			if ((y >= posY && y <= posY + topHeight && x >= posX && x <= posX + panelWidth)
					&& (fposY >= posY && fposY <= posY + topHeight && fposX >= posX && fposX <= posX + panelWidth)) {
				this.setBeingDragged(true);
				if (timer == 0) {
					difX = fposX - posX;
					difY = fposY - posY;
					timer++;
				}
			}
			if (isBeingDragged()) {
				this.setGoingOpen(false);
				this.setPosition(x - difX, y - difY);
				return true;
			}
		} else {
			this.setGoingOpen(true);
			return false;
		}
		return false;
	}

	public boolean isTopBeingClicked(int x, int y) {
		if (x >= posX && x <= posX + panelWidth && y >= posY && y <= posY + topHeight) {
			return true;
		}
		return false;
	}

	public void firstClick(int x, int y) {
		fposX = x;
		fposY = y;
	}

	public ClickScreen getScreen() {
		return screen;
	}

	public int getX() {
		return (int) posX;
	}

	public int getY() {
		return (int) posY;
	}

	public List<NormalPanelItem> getPanelItems() {
		return panelItems;
	}

	public static int getWidth() {
		double posX = Wrapper.getSRes().getScaledWidth() / 4;
		double posX2 = Wrapper.getSRes().getScaledWidth() / 4 * 3;
		int width = MathHelper.floor_double(posX2 - posX) / 2;
		return width - 5;
	}
}
