package net.spookysquad.spookster.command;

import net.spookysquad.spookster.Spookster;

public class Command {

	private String[] names;
	private String description;
	private String name;

	public Command(String[] names, String description) {
		this.name = names[0];
		if(names.length > 1) {
			if(names[0].contains(" ")) {
				this.name = names[1];
			}
		}
		this.names = names;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public String getDesc() {
		return description;
	}
	
	public boolean onCommand(String text, String cmd, String[] args) {
		return false;
	}

	public String getCommand() {
		return Spookster.clientPrefix + this.getName();
	}
}
