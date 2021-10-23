package net.spookysquad.spookster.gui.accountmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class AccountSlot extends GuiSlot {

	private int selectedIndex = 0;
	private final GuiAccountManager parent;

	public AccountSlot(GuiAccountManager parent) {
		super(Minecraft.getMinecraft(), parent.width, parent.height,
				4 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT,
				parent.height - 20 - 8 - 4
				- Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT,
				Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2 + 4);
		this.parent = parent;
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	public MinecraftAccount getAccount() {
		if (AccountManager.minecraftAccounts.size() < this.selectedIndex + 1
				|| this.selectedIndex < 0) {
			return null;
		}
		return AccountManager.minecraftAccounts.get(this.selectedIndex);
	}
	
	@Override
	protected int getSize() {
		return AccountManager.minecraftAccounts.size();
	}

	@Override
	protected boolean isSelected(int var1) {
		return var1 == this.selectedIndex;
	}
	
	public int getSelected() {
		return selectedIndex;
	}
	
	public void setSelected(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		this.selectedIndex = var1;
	}

	protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
		if (AccountManager.minecraftAccounts.size() < 1) {
			return;
		}
		MinecraftAccount currentAccount = AccountManager.minecraftAccounts.get(p_148126_1_);
		Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(currentAccount.username, p_148126_2_ + 2, p_148126_3_ + 1, 0xFFFFFFFF);
		
	}

}
