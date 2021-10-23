package net.spookysquad.spookster.mod.mods;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventGameTick;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.mods.moveinventory.CustomGuiInventory;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {

	public InventoryMove() {
		super("InventoryMove", "Allows you to walk around with your inventory open", Type.MOVEMENT, Keyboard.KEY_NONE, 0xFFba0f1a);
	}

	public void onEvent(Event event) {
		if (event instanceof EventGameTick) {
			if (Wrapper.getPlayer() != null) {
				if (Wrapper.getMinecraft().currentScreen instanceof GuiInventory) {
					Wrapper.getMinecraft().displayGuiScreen(new CustomGuiInventory(Wrapper.getPlayer()));
				}
				if (!(Wrapper.getMinecraft().currentScreen instanceof GuiChat) && (Wrapper.getMinecraft().currentScreen != null)) {
					float changeAmount = 1;
					if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
						Wrapper.getPlayer().rotationPitch += changeAmount;
					}

					if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
						Wrapper.getPlayer().rotationPitch -= changeAmount;
					}

					if (Wrapper.getPlayer().rotationPitch > 90) {
						Wrapper.getPlayer().rotationPitch = 90;
					}

					if (Wrapper.getPlayer().rotationPitch < -90) {
						Wrapper.getPlayer().rotationPitch = -90;
					}

					if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
						Wrapper.getPlayer().rotationYaw -= changeAmount;
					}

					if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
						Wrapper.getPlayer().rotationYaw += changeAmount;
					}

					KeyBinding[] moveKeys = { Wrapper.getGameSettings().keyBindForward, Wrapper.getGameSettings().keyBindBack, Wrapper.getGameSettings().keyBindLeft, Wrapper.getGameSettings().keyBindRight, Wrapper.getGameSettings().keyBindJump,
							Wrapper.getGameSettings().keyBindSprint };

					for (KeyBinding bind : moveKeys)
						KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));
				}
			}
		}
	}

}
