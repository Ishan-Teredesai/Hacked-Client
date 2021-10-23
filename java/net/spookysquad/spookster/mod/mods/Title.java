package net.spookysquad.spookster.mod.mods;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Title extends Module {

	public Title() {
		super(new String[] { "Title" }, "Shows active hacks in the title of the window", Type.MISC, Keyboard.KEY_GRAVE, -1);
	}

	public void onEvent(Event event) {
		if(event instanceof EventPreMotion) {
			String theTitle = "Minecraft 1.7.10 | ";
			for(Module mod: Spookster.instance.moduleManager.getModules()) {
				if(mod.isEnabled() && mod != this && mod.getColor() != -1 && mod.isVisible()) {
					theTitle = theTitle + mod.getDisplay() + ", ";
				}
			}
			
			Display.setTitle(theTitle.substring(0, theTitle.length() - 2));
		}
	}
	
	public boolean onDisable() {
		Display.setTitle("Minecraft 1.7.10");
		
		return super.onDisable();
	}

}
