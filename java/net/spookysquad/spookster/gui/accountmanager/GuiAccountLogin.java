package net.spookysquad.spookster.gui.accountmanager;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.spookysquad.spookster.Spookster;

import org.lwjgl.input.Keyboard;

public class GuiAccountLogin extends GuiScreen implements Messageable {

	private GuiTextField usernameField;
	private GuiPasswordField passwordField;

	private final GuiScreen parent;

	String statMessage;

	GuiAccountLogin(final GuiScreen paramScreen) {
		this.parent = paramScreen;
		this.mc = Minecraft.getMinecraft();

		if (this.mc == null) {
			this.statMessage = "\247dUnknown status";
		}
		if (this.mc.getSession() != null) {
			this.statMessage = "\247aCurrently signed in as "
					+ this.mc.getSession().getUsername();
		} else {
			this.statMessage = "\247dUnknown status";
		}
	}

	@Override
	public void actionPerformed(final GuiButton button) {
		if (button.id == 1) {
			if (!this.usernameField.getText().trim().isEmpty()) {
				this.login(this.usernameField.getText().trim(),
						this.passwordField.getText().trim());
			}
		} else if (button.id == 2) {
			this.mc.displayGuiScreen(this.parent);
		}
	}

	@Override
	public void drawScreen(final int x, final int y, final float f) {
		this.drawDefaultBackground();
		this.mc.fontRendererObj.drawStringWithShadow(this.statMessage, 2, 2,
				0xffffff);
		this.drawString(mc.fontRendererObj, "Username", this.width / 2 - 100,
				63 - 25, 0xA0A0A0);
		this.drawString(mc.fontRendererObj, "\2474*", this.width / 2 - 106,
				63 - 25, 0xA0A0A0);
		this.drawString(mc.fontRendererObj, "Password", this.width / 2 - 100,
				104 - 25, 0xA0A0A0);
		this.drawString(mc.fontRendererObj, "Alt Database Password",
				this.width / 2 - 100, 144 - 25, 0xA0A0A0);
		try {
			this.usernameField.updateCursorCounter();
			this.passwordField.drawTextBox();
		} catch (final Exception e) {
			Spookster.logger.info("Failed drawing & updating in accountlogin | Exception: " + e.getMessage());
		}
		super.drawScreen(x, y, f);
	}

	@Override
	public String getMessage() {
		return this.statMessage;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		this.buttonList.add(new GuiButton(1, this.width / 2 - 100,
				this.height / 4 + 64 + 40, "Login"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height
				/ 4 + 64 + 24 + 40, "Back"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height
				/ 4 + 64 + 48 + 40, "Random Alt"));
		this.usernameField = new GuiTextField(mc.fontRendererObj,
				this.width / 2 - 100, 76 - 25, 200, 20);
		this.passwordField = new GuiPasswordField(mc.fontRendererObj,
				this.width / 2 - 100, 116 - 25, 200, 20);
		this.passwordField.setText("");
	}

	@Override
	protected void keyTyped(final char c, final int i) {
		this.usernameField.textboxKeyTyped(c, i);
		this.passwordField.textboxKeyTyped(c, i);
		if (c == '\t') {
			if (this.usernameField.isFocused()) {
				this.usernameField.setFocused(false);
				this.passwordField.setFocused(true);
			} else if (this.passwordField.isFocused()) {
				this.usernameField.setFocused(false);
				this.passwordField.setFocused(false);
			}
		}
		if (c == '\r') {
			this.actionPerformed((GuiButton) this.buttonList.get(0));
		}
	}

	private void login(final String username, final String password) {
		final Thread loginThread = new Thread(new ThreadLogin(this, username,
				password));
		loginThread.start();
	}

	@Override
	public void mouseClicked(final int x, final int y, final int b) {
		this.usernameField.mouseClicked(x, y, b);
		this.passwordField.mouseClicked(x, y, b);
		super.mouseClicked(x, y, b);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void setMessage(final String message) {
		this.statMessage = message;
	}

	@Override
	public void updateScreen() {
		this.usernameField.updateCursorCounter();
		this.passwordField.updateCursorCounter();
	}

}
