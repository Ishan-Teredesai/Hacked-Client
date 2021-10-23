package net.spookysquad.spookster.render.clickgui;

import net.minecraft.util.ResourceLocation;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.mod.values.Value.ValueType;
import net.spookysquad.spookster.render.FontUtil;
import net.spookysquad.spookster.render.GuiUtil;
import net.spookysquad.spookster.utils.ValueUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClickPanelItemFactory {

	public interface PanelItem {

		public void drawPanelItem(double posX, double posY, int x, int y);

		public boolean handleClick(double posX, double posY, int x, int y, boolean DistBelow2);
	}

	public static class NormalPanelItem extends GuiUtil implements PanelItem {

		public NormalPanelItem(Module module) {
			super();
			this.module = module;
		}

		public int xOffset = 0;
		public int yOffset = -4;

		public int xWidth = 105;
		public int yHeight = 12;

		protected long resetMS = -1L;
		static int timer = 0;
		static int xClickw, yClickw;
		private final Module module;

		@Override
		public void drawPanelItem(double posX, double posY, int x, int y) {
			drawNormalPanelItem(posX, posY);
			if (y >= (posY + yOffset) && y <= (posY + yHeight + yOffset) && (x >= (posX + xOffset) && x <= (posX + xWidth + xOffset))) {
				drawPanelItemHover(posX, posY, x, y);
				if (timer < 5) {
					if (timer == 0) {
						resetMS = System.currentTimeMillis();
						xClickw = (int) posX;
						yClickw = (int) posY;
					}
					timer++;
				}
			} else if (x < xClickw + xOffset || x > xClickw + xWidth + xOffset || y > yClickw + yHeight + yOffset || y < yClickw + yOffset) {
				timer = 0;
			}
			if (x < posX + xOffset || x > posX + xWidth + xOffset || y > posY + yHeight + yOffset || y < posY + yOffset) {
				resetPanelItemHover();
			}
		}

		public void resetPanelItemHover() {

		}

		public void drawNormalPanelItem(double posX, double posY) {
			drawRect(posX + xOffset, posY + yOffset, posX + xWidth + xOffset, posY + yHeight + yOffset, getModule().isEnabled() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
			String name = getModule().getDisplay();
			drawStringWithShadow(name, (float) posX + xOffset + 1, (float) posY, 0xFFFFFFFF, 0.70F, 0.85F);
			drawStringWithShadow(" [" + (getModule().getKeyCode() == -1 ? "-" : getModule().getKeyCode() > 256 ? Mouse.getButtonName(getModule().getKeyCode() - 256) : Keyboard.getKeyName(getModule().getKeyCode())) + "]", (float) posX + xOffset + 1
					+ (getFont().getStringWidth(name) * 0.85F), (float) posY + 1.05F, 0xFFFFFFFF, 0.70F, 0.75F);
		}

		public void drawPanelItemHover(double posX, double posY, int x, int y) {
			drawRect(posX + xOffset, posY + yOffset, posX + xWidth + xOffset, posY + yHeight + yOffset, getModule().isEnabled() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
			String name = getModule().getDisplay();
			drawStringWithShadow(name, (float) posX + xOffset + 1, (float) posY, 0xFFFFFFFF, 0.70F, 0.85F);
			drawStringWithShadow(" [" + (getModule().getKeyCode() == -1 ? "-" : getModule().getKeyCode() > 256 ? Mouse.getButtonName(getModule().getKeyCode() - 256) : Keyboard.getKeyName(getModule().getKeyCode())) + "]", (float) posX + xOffset + 1
					+ (getFont().getStringWidth(name) * 0.85F), (float) posY + 1.05F, 0xFFFFFFFF, 0.70F, 0.75F);
		}

		@Override
		public boolean handleClick(double posX, double posY, int x, int y, boolean distBelow2) {
			if (y >= (posY + yOffset - 1) && y <= (posY + yHeight + yOffset + 1) && (x >= (posX + xOffset) && x <= (posX + xWidth + xOffset)) && distBelow2) {
				module.toggle(true);
				return true;
			}
			return false;
		}

		public boolean setListingKey(double posX, double posY, int x, int y) {
			if (y >= (posY + yOffset - 1) && y <= (posY + yHeight + yOffset + 1) && (x >= (posX + xOffset) && x <= (posX + xWidth + xOffset))) {
				ClickScreen.getInstance().setModuleToChangeKey(this.module);
				return true;
			}
			return false;
		}

		public boolean handleDrag(double posX, double posY, int x, int y) {
			return false;
		}

		public static double getWidth(double width, double min, double max, double d) {
			if (d >= max) {
				d = max;
			} else if (d <= min) {
				d = min;
			}
			double w = width * (d - min);
			double f = w / (max - min);
			return f;
		}

		public Module getModule() {
			return module;
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class EditablePanelItem extends NormalPanelItem {
		public boolean editProperties;
		protected ClickPanel panel;
		protected double fX, fY, ticky;
		private static final ResourceLocation ICON = new ResourceLocation("spookster", "textures/GExtra_Small_Ghost.png");

		public EditablePanelItem(ClickPanel panel, Module module) {
			super(module);
			this.panel = panel;
			editProperties = false;
		}

		@Override
		public void drawNormalPanelItem(double posX, double posY) {
			yHeight = 12;
			drawRect(posX + xOffset, posY + yOffset, posX + xWidth + xOffset - 12, posY + yHeight + yOffset, getModule().isEnabled() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
			drawRect(posX + xOffset + xWidth - 15, posY + yOffset, posX + xWidth + xOffset, posY + yHeight + yOffset, ClickScreen.colors[2]);
			String name = getModule().getDisplay();
			drawStringWithShadow(name, (float) posX + xOffset + 1, (float) posY, 0xFFFFFFFF, 0.70F, 0.85F);
			drawStringWithShadow(" [" + (ClickScreen.getInstance().getModuleToChangeKey() == this.getModule() ? "?" : (getModule().getKeyCode() == -1 ? "-" : getModule().getKeyCode() > 256 ? Mouse.getButtonName(getModule().getKeyCode() - 256) : Keyboard.getKeyName(getModule().getKeyCode()))) + "]", (float) posX + xOffset + 1
					+ (getFont().getStringWidth(name) * 0.85F), (float) posY + 1.05F, 0xFFFFFFFF, 0.70F, 0.75F);
			if (this.editProperties) {
				int total = 2;
				HasValues hep = (HasValues) getModule();
				for (Value ps : hep.getValues()) {
					double width = getWidth(posX + xOffset + 1.5, posX + xWidth + xOffset - 1.5);
					if(ps.getType() == ValueType.DISPLAYLIST) {
						int he = 10;
						int off = -4;
						total += 12;
						drawRect(posX + 1.5, posY + total + off, posX + width, posY + off + total + he, ClickScreen.colors[2]);
						drawRect(posX + width - 10, posY + total + off, posX + width, posY + off + total + he, ps.isOpen() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
						drawStringWithShadow(ps.getName(), (float) posX + 2, (float) posY + off + total + (he / 4), 0xFFFFFFFF, 0.75F, 0.85F);
						if (ps.isOpen()) {
							for (Value v : ps.getOtherValues()) {
								Object currentValue = hep.getValue(v.getName());
								if (currentValue instanceof Boolean && v.getType() == ValueType.NORMAL) {
									boolean currentBoolean = (Boolean) currentValue;
									if(currentBoolean) continue;
									int newBool = 8;
									int otherOffset = -2;
									total += 10;
									drawRect(posX + 2, posY + total + otherOffset, posX + width - 0.5, posY + otherOffset + total + newBool, ClickScreen.colors[2]);
									drawStringWithShadow(v.getName(), (float) posX + 3, (float) posY + otherOffset + total + (newBool / 4) - 1F, 0xFFFFFFFF, 0.75F, 0.75F);
								}
							}
						}
					} else if (ps.getType() == ValueType.MODE) {
						int he = 10;
						int off = -4;
						total += 12;
						drawRect(posX + 1.5, posY + total + off, posX + width, posY + off + total + he, ps.isOpen() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
						drawRect(posX + width - 10, posY + total + off, posX + width, posY + off + total + he, ps.isOpen() ? ClickScreen.colors[1] : ClickScreen.colors[2]);
						String Vname = ps.getOtherValues().get(0).getName();
						for(Value v : ps.getOtherValues()) {
							Object currentValue = hep.getValue(v.getName());
							if (currentValue instanceof Boolean && v.getType() == ValueType.NORMAL) {
								boolean currentBoolean = (Boolean) currentValue;
								if(currentBoolean) Vname = v.getName();
							}
						}
						drawStringWithShadow(Vname, (float) posX + 2, (float) posY + off + total + (he / 4), 0xFFFFFFFF, 0.75F, 0.85F);
						if (ps.isOpen()) {
							for (Value v : ps.getOtherValues()) {
								Object currentValue = hep.getValue(v.getName());
								if (currentValue instanceof Boolean && v.getType() == ValueType.NORMAL) {
									boolean currentBoolean = (Boolean) currentValue;
									if(currentBoolean) continue;
									int newBool = 8;
									int otherOffset = -2;
									total += 10;
									drawRect(posX + 2, posY + total + otherOffset, posX + width - 0.5, posY + otherOffset + total + newBool, ClickScreen.colors[2]);
									drawStringWithShadow(v.getName(), (float) posX + 3, (float) posY + otherOffset + total + (newBool / 4) - 1F, 0xFFFFFFFF, 0.75F, 0.75F);
								}
							}
						}
					} else {
						Object currentValue = hep.getValue(ps.getName());
						if (currentValue instanceof Boolean && ps.getType() == ValueType.NORMAL) {
							boolean d = (Boolean) currentValue;
							int he = 10;
							int off = -4;
							total += 12;
							drawRect(posX + 1.5, posY + total + off, posX + width, posY + off + total + he, d ? ClickScreen.colors[1] : ClickScreen.colors[2]);
							drawStringWithShadow(ps.getName(), (float) posX + 2, (float) posY + off + total + (he / 4), 0xFFFFFFFF, 0.75F, 0.85F);
						} else if (currentValue != null && ps.getType() == ValueType.NORMAL) {
							double min = ValueUtil.toDouble(ps.getMin());
							double max = ValueUtil.toDouble(ps.getMax());
							total += FontUtil.getFontHeight() * 0.8;
							drawStringWithShadow(ps.getName() + ": " + currentValue.toString(), (float) posX + 1, (float) posY + total, 0xFFFFFFFF, 0.7F, 0.8F);
							total += FontUtil.getFontHeight() * 0.8 + 1;
							drawRect(posX + 0.5, posY + total - 0.5, posX + width + 0.5, posY + 5 + total + 0.5, ClickScreen.colors[2]);
							drawRect(posX + 1.5, posY + total, posX + getWidth(width, min, max, ValueUtil.toDouble(currentValue)), posY + 5 + total, ClickScreen.colors[1]);
							drawRect(posX + getWidth(width, min, max, ValueUtil.toDouble(currentValue)) - (ValueUtil.toDouble(currentValue) <= min + 0.1 ? 0 : 0.5), posY + total, posX + getWidth(width, min, max, ValueUtil.toDouble(currentValue))
									+ (ValueUtil.toDouble(currentValue) >= max - 0.1 ? 0 : 0.5), posY + 5 + total, 0xFF00FF7F);
						}
					}
				}
				yHeight += total;
			}
			drawEditableIcon(posX, posY);
		}

		@Override
		public void drawPanelItemHover(double posX, double posY, int x, int y) {
			drawNormalPanelItem(posX, posY);
		}

		public void drawEditableIcon(double posX, double posY) {
			float size = 11F;
			drawTexturedRectangle(ICON, posX + xWidth - size - 1, posY - 3.5, size, size, this.getModule().isEnabled() ? ClickScreen.colors[1] : 0xFFFFFFFF);
		}

		@Override
		public boolean handleClick(double posX, double posY, int x, int y, boolean distBelow2) {
			if (ticky == 1) {
				fX = 0;
				fY = 0;
				ticky = 0;
			}
			if (!distBelow2) return false;

			if (y >= (posY + yOffset) && y <= (posY + 10 + yOffset) && (x >= posX + xOffset + xWidth - 15 && x <= posX + xWidth + xOffset)) {
				editProperties = !editProperties;
				for (NormalPanelItem e : this.panel.getPanelItems()) {
					if (e instanceof EditablePanelItem) {
						EditablePanelItem ed = (EditablePanelItem) e;
						if (!e.getModule().getDisplay().equalsIgnoreCase(this.getModule().getDisplay())) ed.editProperties = false;
					}
				}
				return true;
			}

			if (y >= (posY + yOffset - 1) && y <= (posY + 10 + yOffset + 1) && (x >= (posX + xOffset) && x <= (posX + xWidth + xOffset))) {
				getModule().toggle(true);
				return true;
			}
			if (editProperties) {
				int total = 2;
				HasValues hep = (HasValues) getModule();
				for (Value ps : hep.getValues()) {
					double width = getWidth(posX + xOffset, posX + xWidth + xOffset - 1.5);
					if(ps.getType() == ValueType.DISPLAYLIST) {
						int he = 10;
						int off = -4;
						total += 12;
						if (x >= posX + 1.5 && y >= posY + total + off && x <= posX + width && y <= posY + off + total + he) {
							ps.setOpen(!ps.isOpen());
							return true;
						}
					} else if (ps.getType() == ValueType.MODE) {
						int he = 10;
						int off = -4;
						total += 12;
						if (x >= posX + 1.5 && y >= posY + total + off && x <= posX + width && y <= posY + off + total + he) {
							ps.setOpen(!ps.isOpen());
							return true;
						}
						if (ps.isOpen()) {
							for (int i = 0; i < ps.getOtherValues().size(); i++) {
								if (i == 0) continue;
								Value v = ps.getOtherValues().get(i);
								Object currentValue = hep.getValue(v.getName());
								if (currentValue instanceof Boolean && v.getType() == ValueType.NORMAL) {
									int newBool = 8;
									int otherOffset = -2;
									total += 10;
									if (x >= posX + 2 && y >= posY + total + otherOffset && x <= posX + width - 0.5 && y <= posY + otherOffset + total + newBool) {
										ps.getOtherValues().add(0, v);
										ps.getOtherValues().remove(i + 1);
										for (Value vz : ps.getOtherValues()) {
											hep.setValue(vz.getName(), (Boolean) (ps.getOtherValues().get(0) == vz));
										}
										ps.setOpen(false);
										return true;
									}
								}
							}
						}
					} else {
						Object currentValue = hep.getValue(ps.getName());
						if (currentValue instanceof Boolean && ps.getType() == ValueType.NORMAL) {
							boolean value = (Boolean) currentValue;
							int he = 10;
							int off = -4;
							total += 12;
							if (x >= posX + 1.5 && y >= posY + total + off && x <= posX + width && y <= posY + off + total + he) {
								hep.setValue(ps.getName(), !value);
								return true;
							}
						} else if (currentValue != null && ps.getType() == ValueType.NORMAL) {
							double min = ValueUtil.toDouble(ps.getMin());
							double max = ValueUtil.toDouble(ps.getMax());
							double newValue = x - posX;
							Object value = ValueUtil.getValueForClickGUI(max - min, min, width, newValue, ps.getVClass());
							total += FontUtil.getFontHeight() * 1.6 + 1;
							if (x >= posX + 1.5 && x <= posX + width && y >= posY + total && y <= posY + 5 + total) {
								hep.setValue(ps.getName(), value);
								return true;
							}
						}
					}
				}
			}
			return false;
		}

		@Override
		public boolean handleDrag(double posX, double posY, int x, int y) {
			if (!this.editProperties || panel.isBeingDragged()) return false;

			if (ticky == 0) {
				fX = x;
				fY = y;
				ticky++;
			}

			if (editProperties) {
				int total = 2;
				HasValues hep = (HasValues) getModule();
				for (Value ps : hep.getValues()) {
					double width = getWidth(posX + xOffset, posX + xWidth + xOffset - 1.5);
					if(ps.getType() == ValueType.DISPLAYLIST) {
						total += 12;
					} else if (ps.getType() == ValueType.MODE) {
						total += 12;
						if (ps.isOpen()) {
							for (int i = 0; i < ps.getOtherValues().size(); i++) {
								if (i == 0) continue;
								Value v = ps.getOtherValues().get(i);
								Object currentValue = hep.getValue(v.getName());
								if (currentValue instanceof Boolean && v.getType() == ValueType.NORMAL) {
									total += 10;
								}
							}
						}
					} else {
						Object currentValue = hep.getValue(ps.getName());
						if (currentValue instanceof Boolean && ps.getType() == ValueType.NORMAL) {
							total += 12;
						} else if (currentValue != null && ps.getType() == ValueType.NORMAL) {
							double min = ValueUtil.toDouble(ps.getMin());
							double max = ValueUtil.toDouble(ps.getMax());
							double newValue = x - posX;
							Object value = ValueUtil.getValueForClickGUI(max - min, min, width, newValue, ps.getVClass());
							total += FontUtil.getFontHeight() * 1.6 + 1;
							if (fX >= posX + 1.5 && fX <= posX + width && fY >= posY + total && fY <= posY + 5 + total) {
								hep.setValue(ps.getName(), value);
								return true;
							}
						}
					}
				}
			}
			return super.handleDrag(posX, posY, x, y);
		}

		public double getWidth(double posX, double posX2) {
			double width = posX2 - posX;
			return width;
		}
	}

	public static NormalPanelItem createPanelItem(ClickPanel panel, Module module) {
		NormalPanelItem item;
		if (module instanceof HasValues) {
			item = new EditablePanelItem(panel, module);
		} else {
			item = new NormalPanelItem(module);
		}
		return item;

	}

}
