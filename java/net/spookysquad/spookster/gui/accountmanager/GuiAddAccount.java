package net.spookysquad.spookster.gui.accountmanager;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiAddAccount extends GuiScreen {
	private final GuiScreen parent;
	private GuiTextField username;
	private GuiPasswordField password;

	public GuiAddAccount(final GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(final GuiButton button) {
		switch (button.id) {
		case 1: // add
			AccountManager.minecraftAccounts.add(new MinecraftAccount(
					this.username.getText().trim(), this.password.getText()
					.trim()));
			this.mc.displayGuiScreen(this.parent);
			break;
		case 2: // back
			this.mc.displayGuiScreen(this.parent);
			break;
		default:
			break;
		}
	}

	@Override
	public void drawScreen(final int x, final int y, final float f) {
		final int components = 4;
		int y1 = this.height / 2 - 24 * components / 2 - 4;
		this.drawDefaultBackground();
		this.drawString(mc.fontRendererObj, "Username", this.width / 2 - 200 / 2
				- mc.fontRendererObj.getStringWidth("Username") - 4, y1 + 10
				- mc.fontRendererObj.FONT_HEIGHT / 2, 0xa0a0a0);
		y1 += 24;
		this.drawString(mc.fontRendererObj, "Password", this.width / 2 - 200 / 2
				- mc.fontRendererObj.getStringWidth("Password") - 4, y1 + 10
				- mc.fontRendererObj.FONT_HEIGHT / 2, 0xa0a0a0);
		this.username.drawTextBox();
		this.password.drawTextBox();
		super.drawScreen(x, y, f);
	}

	@Override
	public void initGui() {
		final int components = 4;
		int y = this.height / 2 - 24 * components / 2 - 4;
		this.username = new GuiTextField(Minecraft.getMinecraft().fontRendererObj,
				this.width / 2 - 200 / 2, y, 200, 20);
		this.username.setMaxStringLength(256);
		y += 24;
		this.password = new GuiPasswordField(Minecraft.getMinecraft().fontRendererObj,
				this.width / 2 - 200 / 2, y, 200, 20);
		this.password.setMaxStringLength(128);
		y += 24;
		this.buttonList
		.add(new GuiButton(1, this.width / 2 - 200 / 2, y, "Add"));
		y += 24;
		this.buttonList.add(new GuiButton(2, this.width / 2 - 200 / 2, y,
				"Back"));
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(final char c, final int i) {
		this.username.textboxKeyTyped(c, i);
		this.password.textboxKeyTyped(c, i);
		if (c == '\t') {
			if (this.password.isFocused()) {
				this.password.setFocused(false);
				this.username.setFocused(true);
			} else {
				this.password.setFocused(true);
				this.username.setFocused(false);
			}
		}
		if (c == '\r') {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}

	public void mouseClicked(final int x, final int y, final int b) {
		this.username.mouseClicked(x, y, b);
		this.password.mouseClicked(x, y, b);
		super.mouseClicked(x, y, b);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}
}
