package net.spookysquad.spookster.command.commands;

import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.command.Command;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.PlayerUtil;
import net.spookysquad.spookster.utils.Wrapper;

public class VClip extends Command {

	public VClip() {
		super(new String[] { "vclip", "up", "down" }, "Lets you teleport up or down.");
	}
	
	public boolean onCommand(String text, String cmd, String[] args) {
		for(String name: getNames()) {
			if(cmd.equalsIgnoreCase(name)) {
				if(args.length > 1) {
					try {
						double teleportAmount = Double.parseDouble(args[1]);
						PlayerUtil.getPlayer().boundingBox.offset(0, teleportAmount, 0);
						Wrapper.logChat(MessageType.NOTIFCATION, "Teleported " + teleportAmount + " block(s)!");
					}
					catch(Exception e) {
						Wrapper.logChat(MessageType.NOTIFCATION, "Invalid usage! Use:");
						Wrapper.logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " <amount>");
					}
				} else {
					Wrapper.logChat(MessageType.NOTIFCATION, "Invalid syntax! Use:");
					Wrapper.logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " <amount>");
				}
				return true;
			}
		}
		return super.onCommand(text, cmd, args);
	}

}
