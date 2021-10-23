package net.spookysquad.spookster.mod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;

import net.minecraft.client.gui.GuiMainMenu;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.Listener;
import net.spookysquad.spookster.event.events.EventKeyPressed;
import net.spookysquad.spookster.event.events.EventMouseClicked;
import net.spookysquad.spookster.gui.accountmanager.GuiAccountManager;
import net.spookysquad.spookster.manager.Manager;
import net.spookysquad.spookster.mod.mods.ArmorSwitch;
import net.spookysquad.spookster.mod.mods.Blink;
import net.spookysquad.spookster.mod.mods.ClickGUI;
import net.spookysquad.spookster.mod.mods.ExternalGUI;
import net.spookysquad.spookster.mod.mods.FastUse;
import net.spookysquad.spookster.mod.mods.Fly;
import net.spookysquad.spookster.mod.mods.Freecam;
import net.spookysquad.spookster.mod.mods.Friends;
import net.spookysquad.spookster.mod.mods.Fullbright;
import net.spookysquad.spookster.mod.mods.GangsterWalk;
import net.spookysquad.spookster.mod.mods.HUD;
import net.spookysquad.spookster.mod.mods.InventoryMove;
import net.spookysquad.spookster.mod.mods.Jesus;
import net.spookysquad.spookster.mod.mods.MobFarm;
import net.spookysquad.spookster.mod.mods.Nametag;
import net.spookysquad.spookster.mod.mods.NoFall;
import net.spookysquad.spookster.mod.mods.Notifications;
import net.spookysquad.spookster.mod.mods.Phase;
import net.spookysquad.spookster.mod.mods.PotionThrower;
import net.spookysquad.spookster.mod.mods.ProjectileSense;
import net.spookysquad.spookster.mod.mods.Projectiles;
import net.spookysquad.spookster.mod.mods.Search;
import net.spookysquad.spookster.mod.mods.Speed;
import net.spookysquad.spookster.mod.mods.Speedmine;
import net.spookysquad.spookster.mod.mods.Sprint;
import net.spookysquad.spookster.mod.mods.Step;
import net.spookysquad.spookster.mod.mods.Title;
import net.spookysquad.spookster.mod.mods.Tracers;
import net.spookysquad.spookster.mod.mods.Triggerbot;
import net.spookysquad.spookster.mod.mods.Velocity;
import net.spookysquad.spookster.mod.mods.XRay;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.mod.values.Value.ValueType;
import net.spookysquad.spookster.utils.ValueUtil;
import net.spookysquad.spookster.utils.Wrapper;

import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ModuleManager extends Manager implements Listener {

	private Spookster spookster;
	private ArrayList<Module> modules = new ArrayList<Module>();

	public void init(Spookster spookster) {
		this.spookster = spookster;
		spookster.eventManager.registerListener(this);
		this.modules.addAll(Arrays.asList(new ArmorSwitch(), new Blink(), new ClickGUI(), new ExternalGUI(), new FastUse(), new Fly(), new Freecam(), new Friends(), new Fullbright(), new GangsterWalk(), new HUD(), new InventoryMove(), new Jesus(),
				new MobFarm(), new Nametag(), new Notifications(), new NoFall(), new Phase(), new PotionThrower(), new Projectiles(), new ProjectileSense(), new Search(), new Speed(), new Speedmine(), new Sprint(), new Step(), new Title(), new Tracers(),
				new Triggerbot(), new XRay(), new Velocity()));
		
		Collections.sort(modules, new Comparator<Module>() {
	        @Override
	        public int compare(Module s1, Module s2) {
	            return s1.getName()[0].compareToIgnoreCase(s2.getName()[0]);
	        }
	    });
	}

	public String toString() {
		return "ModuleManager[mods=\'" + modules.size() + "\']";
	}

	public void deinit(Spookster spookster) {
		spookster.eventManager.unregisterListener(this);
	}

	/**
	 * Returns the modules ArrayList
	 * 
	 * @return
	 */
	public ArrayList<Module> getModules() {
		return modules;
	}

	/**
	 * Returns a module by class
	 * 
	 * @param moduleClass
	 * @return
	 */
	public Module getModule(Class<? extends Module> moduleClass) {
		for (Module mod : modules) {
			if (mod.getClass() == moduleClass) { return mod; }
		}

		return null;
	}

	public ArrayList<Module> getModulesWithType(Type type) {
		ArrayList<Module> tempList = new ArrayList<Module>();
		for (Module m : getModules()) {
			if (m.getType() == type) tempList.add(m);
		}
		return tempList;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventKeyPressed) {
			EventKeyPressed pressed = (EventKeyPressed) event;
			if (pressed.isInGame() && Wrapper.getMinecraft().inGameHasFocus) {

				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && pressed.getKey() == Keyboard.KEY_UP) {
					Spookster.clientEnabled = !Spookster.clientEnabled;
					if (Spookster.clientEnabled) {
						Spookster.instance.loadClientFromFile();
					} else {
						Spookster.instance.disableAndSafeClient();
					}
					return;
				}

				if (Spookster.clientEnabled) {

					for (Module m : getModules()) {
						if (m.getKeyCode() == pressed.getKey()) {
							m.toggle(true);
						}
					}
				}
			} else {
				if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) && pressed.getKey() == Keyboard.KEY_UP) {
					Spookster.clientEnabled = !Spookster.clientEnabled;
					if (Spookster.clientEnabled) {
						Spookster.instance.loadClientFromFile();
					} else {
						Spookster.instance.disableAndSafeClient();
					}
					return;
				}

				if (Spookster.clientEnabled) {
					if (Wrapper.getMinecraft().currentScreen instanceof GuiMainMenu) {
						if (pressed.getKey() == Keyboard.KEY_DOWN) {
							Wrapper.getMinecraft().displayGuiScreen(new GuiAccountManager(null));
						}
					}
				}
			}

		} else if (event instanceof EventMouseClicked) {
			EventMouseClicked pressed = (EventMouseClicked) event;
			if (pressed.isInGame()) {
				if (Wrapper.getMinecraft().inGameHasFocus) {
					if (Spookster.clientEnabled) {
						for (Module m : getModules()) {
							if (m.getKeyCode() - 256 == pressed.getButton()) {
								m.toggle(true);
							}
						}
					}
				}
			}
		}
	}

	public void saveModules() {
		Spookster.logger.log(Level.INFO, "Saving module data");
		for (Module mod : getModules()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Spookster.MODULES_FOLDER, mod.getName()[0].toLowerCase() + ".json")));
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject root = new JsonObject();

				root.addProperty("NAME", mod.getName()[0]);
				root.addProperty("KEY", mod.getKeyCode());
				root.addProperty("STATE", mod.isEnabled());
				root.addProperty("COLOR", mod.getColor());
				root.addProperty("DESC", mod.getDesc());
				root.addProperty("TYPE", mod.getType().getName());
				root.addProperty("VISIBLE", mod.isVisible());

				if (mod instanceof HasValues) {
					HasValues hep = (HasValues) mod;
					JsonObject newDataObject = new JsonObject();
					for (Value v : hep.getValues()) {
						try {
							if (v.getType() == ValueType.NORMAL || v.getType() == ValueType.SAVING) {
								newDataObject.addProperty(v.getName(), String.valueOf(hep.getValue(v.getName())));
							} else if (v.getType() == ValueType.MODE) {
								for (Value extraV : v.getOtherValues()) {
									newDataObject.addProperty(extraV.getName(), String.valueOf(hep.getValue(extraV.getName())));
								}
							}
						} catch (Exception e) {
							Spookster.logger.info("Saving " + mod.getName()[0].toLowerCase() + ".json value | Exception: " + e.getMessage());
						}
					}
					root.add("VALUES", newDataObject);
				}

				writer.write(gson.toJson(root));
				writer.close();
			} catch (Exception e) {
				Spookster.logger.info("Saving " + mod.getName()[0].toLowerCase() + ".json | Exception: " + e.getMessage());
			}
		}
	}

	public void loadModules() {
		Spookster.logger.log(Level.INFO, "Loading module data");
		for (Module mod : getModules()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File(Spookster.MODULES_FOLDER, mod.getName()[0].toLowerCase() + ".json")));

				Gson gson = new Gson();
				JsonObject root = gson.fromJson(reader, JsonObject.class);

				for (Map.Entry<String, JsonElement> setting : root.entrySet()) {
					try {
						if (setting.getKey().equals("KEY")) {
							mod.setKeyCode(Integer.valueOf(setting.getValue().getAsString()));
						} else if (setting.getKey().equals("STATE")) {
							if (setting.getValue().getAsBoolean()) mod.toggle(true);
						} else if (setting.getKey().equals("VISIBLE")) {
							mod.setVisible(setting.getValue().getAsBoolean());
						} else if (setting.getKey().equals("COLOR")) {
							mod.setColor(setting.getValue().getAsInt());
						} else if (setting.getKey().equals("VALUES")) {
							if (mod instanceof HasValues) {
								HasValues hep = (HasValues) mod;
								JsonObject values = setting.getValue().getAsJsonObject();
								for (Map.Entry<String, JsonElement> value : values.entrySet()) {
									try {
										hep.setValue(value.getKey(), ValueUtil.getValue(value.getValue()));
									} catch (Exception ez) {
										Spookster.logger.info("Loading " + mod.getName()[0].toLowerCase() + ".json value | Exception: " + ez.getMessage());
									}
								}
							}
						}
					} catch (Exception e) {
						Spookster.logger.info("Loading " + mod.getName()[0].toLowerCase() + ".json | Exception: " + e.getMessage());
					}
				}

				reader.close();
			} catch (Exception e) {
				Spookster.logger.info("Loading module " + mod.getName() + " file | Exception: " + e.getMessage());
			}
		}
	}

}
