package net.spookysquad.spookster.mod.mods;

import net.minecraft.client.gui.GuiScreen;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.render.clickgui.ClickScreen;

import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {

	public ClickGUI() {
		super(new String[] { "ClickGUI" }, "Opens the clickableGUI", Type.CORE, Keyboard.KEY_RSHIFT, -1);
	}

	ClickScreen clickGUI;

	@Override
	public boolean onEnable() {
		if (getPlayer() == null) return false;
		getMinecraft().displayGuiScreen(getClickGui());
		return false;
	}

	private GuiScreen getClickGui() {
		if (clickGUI == null) clickGUI = new ClickScreen();
		return clickGUI;
	}

	@Override
	public void onEvent(Event event) {}

}
