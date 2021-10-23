package net.spookysquad.spookster.command;

import java.util.ArrayList;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.command.commands.Help;
import net.spookysquad.spookster.command.commands.VClip;
import net.spookysquad.spookster.manager.Manager;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.Wrapper;

public class CommandManager extends Manager {

	private ArrayList<Command> commands = new ArrayList<Command>();

	@Override
	public void init(Spookster spookster) {
		commands.add(new Help());
		commands.add(new VClip());
	}
	
	public String toString() {
		return "CommandManager[cmds=\'" + commands.size() + "\']";
	}

	@Override
	public void deinit(Spookster spookster) {
	}

	public boolean onCommand(String message) {
		if(message.startsWith(Spookster.clientPrefix)) {
			message = message.replaceFirst(Spookster.clientPrefix, "");
			String[] args = message.split(" ");
			
			for (Command command : commands) {
				if (command.onCommand(message, args[0], args)) {
					return false;
				}
			}
			
			Wrapper.logChat(MessageType.NOTIFCATION, "Invalid Command: " + message);
			
			return false;
		}
		return true;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<Command> commands) {
		this.commands = commands;
	}

}
