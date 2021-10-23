package net.spookysquad.spookster.gui.accountmanager;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.spookysquad.spookster.Spookster;

import org.lwjgl.input.Keyboard;

public class GuiAccountManager extends GuiScreen implements Messageable {
	
	private GuiScreen parent;

	private String statMessage;
	private AccountSlot accountSlots;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiAccountManager(GuiScreen parent) {
		this.parent = parent;
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
		
		AccountManager.loadAccounts();
	}
	
	protected void keyTyped(char typedChar, int keyCode)
    {
		if(keyCode == Keyboard.KEY_RETURN) {
			if (this.accountSlots.getAccount() == null) {
				return;
			}
			this.login(this.accountSlots.getAccount().username,
					this.accountSlots.getAccount().password);
		}
		
		if(keyCode == Keyboard.KEY_UP) {
			if(accountSlots.getSelected() > 0)
				accountSlots.setSelected(accountSlots.getSelected() - 1);
		}
		
		if(keyCode == Keyboard.KEY_DOWN) {
			if(accountSlots.getSelected() < AccountManager.minecraftAccounts.size() - 1)
				accountSlots.setSelected(accountSlots.getSelected() + 1);
		}
    }

	@Override
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		switch (button.id) {
		case 3: // login
			if (this.accountSlots.getAccount() == null) {
				return;
			}
			this.login(this.accountSlots.getAccount().username,
					this.accountSlots.getAccount().password);
			break;
		case 4: // exit
			this.mc.displayGuiScreen(this.parent);
			break;
		case 5: // delete
			if (this.accountSlots.getAccount() == null) {
				break;
			}
			AccountManager.minecraftAccounts.remove(this.accountSlots
					.getAccount());
			break;
		case 6: // direct login
			Minecraft.getMinecraft()
			.displayGuiScreen(new GuiAccountLogin(this));
			break;
		case 7: // add
			Minecraft.getMinecraft().displayGuiScreen(new GuiAddAccount(this));
			break;
		default:
			break;
		}
	}

	@Override
	public void drawScreen(final int i, final int j, final float f) {
		this.accountSlots.drawScreen(i, j, f);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
				"Account Manager",
				this.width
				/ 2
				- Minecraft.getMinecraft().fontRendererObj
				.getStringWidth("Account Manager") / 2, 2,
				0xFFFFFFFF);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
				this.statMessage,
				this.width
				/ 2
				- Minecraft.getMinecraft().fontRendererObj
				.getStringWidth(this.statMessage) / 2,
				this.height - 4 - 20 - 4 - this.mc.fontRendererObj.FONT_HEIGHT,
				0xFFFFFFFF);
		super.drawScreen(i, j, f);
	}

	@Override
	public String getMessage() {
		return this.statMessage;
	}

	@Override
	public void initGui() {
		this.accountSlots = new AccountSlot(this);
		this.accountSlots.registerScrollButtons(1, 2);
		int x = this.width
				/ 2
				- (this.mc.fontRendererObj.getStringWidth("Login") * 2 + 4
						+ this.mc.fontRendererObj.getStringWidth("Back") * 2 + 4
						
						+ this.mc.fontRendererObj.getStringWidth("Add")
						* 2 + 4 + this.mc.fontRendererObj.getStringWidth("Delete") * 2)
						/ 2 - 60;
		this.buttonList.add(new GuiButton(4, x, this.height - 24,
				this.mc.fontRendererObj.getStringWidth("Back") * 2, 20, "Back"));
		x += this.mc.fontRendererObj.getStringWidth("Back") * 2 + 4;
		this.buttonList.add(new GuiButton(3, x, this.height - 24,
				this.mc.fontRendererObj.getStringWidth("Login") * 2, 20, "Login"));
		x += this.mc.fontRendererObj.getStringWidth("Login") * 2 + 4;
		//this.buttonList.add(new GuiButton(6, x + 110, this.height - 24,
				//this.mc.fontRendererObj.getStringWidth("Direct Login") * 2, 20,
				//"Direct Login"));
		this.buttonList.add(new GuiButton(7, x, this.height - 24,
				this.mc.fontRendererObj.getStringWidth("Add") * 2, 20, "Add"));
		x += this.mc.fontRendererObj.getStringWidth("Add") * 2 + 4;
		this.buttonList
		.add(new GuiButton(5, x, this.height - 24, this.mc.fontRendererObj
				.getStringWidth("Delete") * 2, 20, "Delete"));
	}

	public void login(String username, String password) {
		Thread loginThread = new Thread(new ThreadLogin(this, username,
				password));
		loginThread.start();
	}
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		catch(Exception e) {
			Spookster.logger.info("Mouse Clicked accountmanager | Exception: " + e.getMessage());
		}
    }

	@Override
	public void onGuiClosed() {
		AccountManager.saveAccounts();
	}

	@Override
	public void setMessage(String message) {
		this.statMessage = message;
	}

}
