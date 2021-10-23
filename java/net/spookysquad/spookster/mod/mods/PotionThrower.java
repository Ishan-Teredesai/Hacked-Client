package net.spookysquad.spookster.mod.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventPreMotion;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.utils.PacketUtil;
import net.spookysquad.spookster.utils.TimeUtil;

public class PotionThrower extends Module implements HasValues {

	public PotionThrower() {
		super("ThrowPotion", "Throws the right amout of pots when enabled.", Type.COMBAT, -1, 0xFF54ff9f);
	}

	private int currentItem = -1, potionPlace = -1;
	private float yaw, pitch, health;
	private TimeUtil time = TimeUtil.getTime();
	private boolean enemyinsight = false, triggerbot = false;
	private ArrayList<Integer> usedPots = new ArrayList<Integer>();
	private int potionDelay = 60;
	private int healthIncrease = 8;

	@Override
	public boolean onEnable() {
		currentItem = getPlayer().inventory.currentItem;
		health = getPlayer().getHealth();
		potionPlace = findHotbarPot(Potion.heal);
		if (potionPlace != -1 && getPlayer().getHealth() <= 16) {
			getPlayer().inventory.currentItem = potionPlace - 36;
			PacketUtil.sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
			time.reset();
			return super.onEnable();
		} else {
			return false;
		}
	}

	@Override
	public boolean onDisable() {
		usedPots.clear();
		getPlayer().inventory.currentItem = currentItem;
		PacketUtil.sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
		return super.onDisable();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotion) {
			if (potionPlace == -1 || getPlayer().getHealth() > 16 || health > 16) {
				this.toggle(true);
				return;
			}

			if (time.hasDelayRun(potionDelay)) {
				usedPots.add(Integer.valueOf(potionPlace));
				throwPot();

				potionPlace = findHotbarPot(Potion.heal);
				if (potionPlace != -1 && getPlayer().getHealth() <= 16 && health <= 16) {
					getPlayer().inventory.currentItem = potionPlace - 36;
					PacketUtil.sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
				} else {
					this.toggle(true);
				}
			}
		}
	}

	private void throwPot() {
		if (getPlayer().getHeldItem() != null && getPlayer().getHeldItem().getItem() instanceof ItemPotion) {
			ItemPotion potion = (ItemPotion) getPlayer().getHeldItem().getItem();
			for (PotionEffect pe : (List<PotionEffect>) potion.getEffects(getPlayer().getHeldItem())) {
				if (pe.getPotionID() == Potion.heal.getId() && pe.toString().contains(", Splash: true")) {
					health += healthIncrease * (pe.getAmplifier() == 0 ? 1 : pe.getAmplifier());
				}
			}
			getController().sendUseItem(getPlayer(), getWorld(), getPlayer().getHeldItem());
		}
		time.reset();
	}

	public int findHotbarPot(Potion pots) {
		for (int o = 36; o <= 44; o++) {
			if (usedPots.contains(Integer.valueOf(o))) continue;

			if (getPlayer().inventoryContainer.getSlot(o).getHasStack()) {
				ItemStack item = getPlayer().inventoryContainer.getSlot(o).getStack();
				if (item != null) {
					if (item.getItem() instanceof ItemPotion) {
						ItemPotion pot = (ItemPotion) item.getItem();
						for (PotionEffect pe : (List<PotionEffect>) pot.getEffects(item)) {
							if (pe.getPotionID() == pots.getId() && pe.toString().contains(", Splash: true")) { return o; }
						}
					}
				}
			}
		}

		return -1;
	}

	private String DELAY = "Throwing potion (ms)";
	private String HEALTHINCREASE = "Health increasement";
	List<Value> values = Arrays.asList(new Value[] { new Value(DELAY, 0, 300, 1.0F), new Value(HEALTHINCREASE, 0, 20, 1.0F)});

	@Override
	public List<Value> getValues() {
		return values;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(DELAY)) return potionDelay;
		if(n.equals(HEALTHINCREASE)) return healthIncrease;
		return null;
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(DELAY)) potionDelay = (Integer) v;
		if(n.equals(HEALTHINCREASE)) healthIncrease = (Integer) v;
	}

}
