package net.spookysquad.spookster.mod.mods;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;

public class ExternalGUI extends Module {

	public ExternalGUI() {
		super(new String[] {"ExternalGUI"}, "Opens an external GUI", Type.MISC, -1, -1);
	}

	@Override
	public boolean onEnable() {
		Spookster.FRAME.setVisible(true);
		return super.onEnable();
	}
	
	@Override
	public boolean onDisable() {
		Spookster.FRAME.setVisible(false);
		return super.onDisable();
	}
	
	@Override
	public void onEvent(Event event) {
		this.setEnabled(Spookster.FRAME.isVisible());
	}

}
