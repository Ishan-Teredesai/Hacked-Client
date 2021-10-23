package net.spookysquad.spookster.mod.mods;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.command.Command;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.TimeUtil;
import net.spookysquad.spookster.utils.Wrapper;

public class ArmorSwitch extends Module {

	public ArmorSwitch() {
		super(new String[] { "Armor Switch", "switch", "as" }, "Switch your armor with a different set.", Type.COMBAT, -1, -1);
		Spookster.instance.commandManager.getCommands().add(new Command(this.getName(), this.getDesc()) {
			@Override
			public boolean onCommand(String text, String cmd, String[] args) {
				for (String name : this.getNames()) {
					if (cmd.equalsIgnoreCase(name)) {
						Wrapper.logChat(MessageType.NOTIFCATION, "Switching your armor!");
						Spookster.instance.moduleManager.getModule(ArmorSwitch.class).toggle(true);
						return true;
					}
				}
				return super.onCommand(text, cmd, args);
			}
		});
		this.setVisible(false);
	}

	int currentPiece = -1;
	int[] armorPieces = new int[] { -1, -1, -1, -1 };
	TimeUtil time = new TimeUtil();

	@Override
	public boolean onEnable() {
		boolean foundArmor = false;
		for (int o = 9; o <= 44; o++) {
			if (getPlayer().inventoryContainer.getSlot(o).getHasStack()) {
				ItemStack item = getPlayer().inventoryContainer.getSlot(o).getStack();
				if (item != null && item.getItem() != null && item.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) item.getItem();
					armorPieces[armor.armorType] = o;
					currentPiece = 0;
					foundArmor = true;
				}
			}
		}
		return foundArmor;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotion) {
			if (time.hasDelayRun(120) && currentPiece < 4) {
				int slot = armorPieces[currentPiece];
				putSlotInArmorSlot(slot, currentPiece);
				currentPiece++;
				time.reset();
			} else if (time.hasDelayRun(230) && currentPiece == 4) {
				clickSlot(armorPieces[0], 1, 0);
				this.toggle(true);
			}
		}
	}

	public static void putSlotInArmorSlot(int slot, int armorSlot) {
		clickSlot(slot, 0, 0);
		switch (armorSlot) {
		case 0:
			// helmet
			clickSlot(5, 0, 0);
			break;
		case 1:
			// chestplate
			clickSlot(6, 0, 0);
			break;
		case 2:
			// leggings
			clickSlot(7, 0, 0);
			break;
		case 3:
			// boots
			clickSlot(8, 0, 0);
			break;
		}
	}

	public static void clickSlot(int slot, int mouseButton, int mode) {
		getController().windowClick(getPlayer().openContainer.windowId, slot, mouseButton, mode, getPlayer());
	}

}
