package net.spookysquad.spookster.command.commands;

import java.util.Iterator;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.command.Command;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.PlayerUtil;
import net.spookysquad.spookster.utils.Wrapper;

public class Help extends Command {

	public Help() {
		super(new String[] { "help", "?" }, "Help for stupid people.");
	}

	public boolean onCommand(String text, String cmd, String[] args) {
		for (String name : getNames()) {
			if (cmd.equalsIgnoreCase(name)) {

				int pageNumber = 1;
				int commandsPerPage = 5;
				int maxPages = Spookster.instance.commandManager.getCommands().size() / commandsPerPage + 1;

				if (args.length > 1) {
					try {
						pageNumber = Integer.parseInt(args[1]);
					} catch (Exception e) {
						Wrapper.logChat(MessageType.NOTIFCATION, "Invalid Syntax!");
						return true;
					}
				}

				if (pageNumber < 1) {
					pageNumber = 1;
				}

				if (pageNumber > maxPages) {
					pageNumber = maxPages;
				}

				Wrapper.logChat(MessageType.NOTIFCATION, "--== Help(" + pageNumber + "/" + maxPages + ") ==--");

				int count = 1;

				Object theObject = null;
				Command theCmd = null;
				for (theObject = Spookster.instance.commandManager.getCommands().iterator(); ((Iterator) theObject).hasNext();) {
					theCmd = (Command) ((Iterator) theObject).next();
					if ((count > commandsPerPage * pageNumber - commandsPerPage) && (count < commandsPerPage * pageNumber + 1)) {
						Wrapper.logChat(MessageType.NOTIFCATION, theCmd.getName() + " - " + theCmd.getDesc());
					}
					count++;
				}

				return true;
			}
		}
		return super.onCommand(text, cmd, args);
	}

}
