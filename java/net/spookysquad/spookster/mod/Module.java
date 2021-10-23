package net.spookysquad.spookster.mod;

import java.util.ArrayList;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.event.Listener;
import net.spookysquad.spookster.utils.Wrapper;

public abstract class Module extends Wrapper implements Listener {
	
	private String[] name;
	private int selectedAlias;
	
	private Type type;
	private String desc;
	private int color;
	private int keyCode;
	private boolean state = false;
	private boolean visible = true;

	public Module(String[] name, String desc, Type type, int keybind, int color) {
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.keyCode = keybind;
		this.color = color;
	}

	public Module(String name, String desc, Type type, int keybind, int color) {
		this.name = new String[] {name};
		this.desc = desc;
		this.type = type;
		this.keyCode = keybind;
		this.color = color;
	}

	public boolean isEnabled() {
		return state;
	}
	
	public void setEnabled(boolean state) {
		this.state = state;
	}
	
	public String[] getName() {
		return this.name;
	}
	
	public String getDisplay() {
		if(getName()[selectedAlias] != null) {
			return getName()[selectedAlias];
		}
		return getClass().getSimpleName().replaceAll("Module", "");
	}
	
	public int getSelected() {
		return selectedAlias;
	}

	public Type getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public int getColor() {
		return color;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean isVisible() {
		return visible;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String[] moduleName) {
		this.name = moduleName;
	}

	public void setName(ArrayList<String> name) {
		String[] alias = new String[name.size()];
		alias = name.toArray(alias);
		
		this.name = alias;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void toggle(boolean safeToFile) {
		this.state = !this.state;
		if (this.state) {
			if (onEnable()) {
				Spookster.instance.eventManager.registerListener(this);
			} else {
				this.state = false;
			}
		} else { 
			if(onDisable()) {
				Spookster.instance.eventManager.unregisterListener(this);
			} else {
				this.state = true;
			}
		}
	}

	public boolean onEnable() {
		return true;
	}

	public boolean onDisable() {
		return true;
	}

}
