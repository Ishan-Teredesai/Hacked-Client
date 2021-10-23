package net.spookysquad.spookster.gui.accountmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField {
	
	public GuiPasswordField(FontRenderer par1FontRenderer, int par2, int par3,
			int par4, int par5) {
		super(par1FontRenderer, par2, par3, par4, par5);
	}

	/**
	 * Draws the textbox
	 */
	@Override
	public void drawTextBox() {
		String prevText = this.getText();
		this.setText(this.getText().replaceAll(".", "*"));
		super.drawTextBox();
		this.setText(prevText);
	}
	
}