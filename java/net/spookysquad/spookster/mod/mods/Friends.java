package net.spookysquad.spookster.mod.mods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.command.Command;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.events.EventMouseClicked;
import net.spookysquad.spookster.mod.HasValues;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.Type;
import net.spookysquad.spookster.mod.values.Value;
import net.spookysquad.spookster.mod.values.Value.ValueType;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.utils.PlayerUtil;
import net.spookysquad.spookster.utils.Wrapper;

public class Friends extends Module implements HasValues {

	private boolean middleMouseFriends;

	public Friends() {
		super(new String[] { "FriendManager" }, "Modules adapt to the fact there are team members.", Type.MISC, -1, -1);
		this.toggle(false);
		Spookster.instance.commandManager.getCommands().add(new Command(new String[] { "friend", "friends", "fr", "f" }, "Manage your friends") {
			public boolean onCommand(String text, String cmd, String[] args) {
				for (String name : getNames()) {
					if (cmd.equalsIgnoreCase(name)) {
						if (args.length == 1) {
							logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " clear | clear all yo friends.");
							logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " list | lists all your friends.");
							logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " <name> | to quickly toggle a friend.");
							logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " add <name> [alias] | add a friend with a specific alias.");
							logChat(MessageType.NOTIFCATION, Spookster.clientPrefix + cmd + " rem <name/alias> | removes a friend from you friendlist.");
							return true;
						}
						if (args[1].toLowerCase().equals("clear")) {
							friends.clear();
							logChat(MessageType.NOTIFCATION, "Poof, all yo friends gone now nigguh.");
						} else if (args[1].toLowerCase().equals("list")) {
							if (!friends.isEmpty()) {
								String names = "";
								for (Friend f : friends) {
									names += f.getAlias() + ", ";
								}
								names = names.substring(0, names.length() - 2);
								logChat(MessageType.NOTIFCATION, friends.size() + " friends listed: " + names);
							} else {
								logChat(MessageType.ERROR, "You have no friends.");
							}
						} else if (args[1].toLowerCase().equals("add")) {
							if (args.length > 2) {
								String username = args[2];
								String alias = args[2];
								if (args.length > 3) {
									alias = "";
									for (int i = 3; i < args.length; i++) {
										alias += args[i] + " ";
									}
									alias = alias.substring(0, alias.length() - 1);
								}

								Friend isFriend = getFriend(username);
								if (isFriend != null) {
									friends.remove(isFriend);
									friends.add(new Friend(username, alias));
									Wrapper.logChat(MessageType.NOTIFCATION, "Changed friend \"" + username + "\", to the alias \"" + alias + "\"!");
								} else {
									friends.add(new Friend(username, alias));
									logChat(MessageType.NOTIFCATION, "Added friend \"" + username + "\" with the alias of \"" + alias + "\"!");
								}
							} else {
								logChat(MessageType.NOTIFCATION, "Invalid Syntax!");
							}
						} else if (args[1].toLowerCase().equals("rem") || args[1].toLowerCase().equals("remove") || args[1].toLowerCase().equals("del") || args[1].toLowerCase().equals("delete")) {
							if (args.length > 2) {
								String removeName = args[2];
								if (args.length > 3) {
									removeName = "";
									for (int i = 2; i < args.length; i++) {
										removeName += args[i] + " ";
									}
									removeName = removeName.substring(0, removeName.length() - 1);
								}

								if (friends.contains(Friends.getFriend(removeName))) {
									friends.remove(Friends.getFriend(removeName));
									logChat(MessageType.NOTIFCATION, "Removed friend \"" + removeName + "\"!");
								} else {
									logChat(MessageType.NOTIFCATION, "You're not friends with \"" + removeName + "\"!");
								}
							} else {
								logChat(MessageType.NOTIFCATION, "Invalid Syntax!");
							}
						} else if (!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("rem")) {
							for (EntityPlayer player : (List<EntityPlayer>) getWorld().playerEntities) {
								if (player.getCommandSenderName().equalsIgnoreCase(args[1])) {
									if (getFriend(player.getCommandSenderName()) == null) friends.add(new Friend(player.getCommandSenderName(), player.getCommandSenderName()));
									else friends.remove(getFriend(player.getCommandSenderName()));
								}
							}
							if (getFriend(args[1]) == null) friends.add(new Friend(args[1], args[1]));
							else friends.remove(getFriend(args[1]));
							logChat(MessageType.NOTIFCATION, getFriend(args[1]) == null ? "Removed \'" + args[1] + "\' from your friendist" : "Added \'" + args[1] + "\' to your friendist");
						}
						return true;
					}
				}
				return super.onCommand(text, cmd, args);
			}

		});
	}

	public boolean onDisable() {
		return false;
	};

	public static HashSet<Friend> friends = new HashSet<Friend>();

	public void onEvent(Event event) {
		if (event instanceof EventMouseClicked) {
			if (middleMouseFriends) {
				EventMouseClicked clicked = (EventMouseClicked) event;
				if (clicked.getButton() == 2) {
					if (PlayerUtil.getEntityOnMouseCurser(5) != null) {
						Entity entity = PlayerUtil.getEntityOnMouseCurser(5);
						if (entity instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) entity;
							if (getFriend(player.getCommandSenderName()) == null) friends.add(new Friend(player.getCommandSenderName(), player.getCommandSenderName()));
							else friends.remove(getFriend(player.getCommandSenderName()));
						}
					}
				}
			}
		}
	}

	public static boolean isFriend(String name) {
		return getFriend(name) != null;
	}

	public static boolean sameTeam(EntityLivingBase entity) {
		if(((Friends) Spookster.instance.moduleManager.getModule(Friends.class)).Teams) {
			if(entity.getTeam() == PlayerUtil.getPlayer().getTeam()) {
				return true;
			}
			
			return false;
		} else {
			return false;
		}
	}

	public static Friend getFriend(String name) {
		for (Friend friend : friends) {
			if (friend.getName().equalsIgnoreCase(name)) { return friend; }
		}
		for (Friend friend : friends) {
			if (friend.getAlias().equalsIgnoreCase(name)) { return friend; }
		}
		return null;
	}

	public boolean Teams = false;
	private String SAVINGFRIENDS = "Saved_Friends", MIDDLEMOUSE = "MiddleMouseFriends", TEAMS = "Teams", FRIENDS = "Friends";
	List<Value> values = Arrays.asList(new Value[] { new Value(MIDDLEMOUSE, false, true), new Value(TEAMS, false, true), new Value(SAVINGFRIENDS, friends) });
	private Value friendsDisplay = new Value(FRIENDS, false, new ArrayList<Value>(), ValueType.DISPLAYLIST);

	@Override
	public List<Value> getValues() {
		List<Value> tempList = new ArrayList<Value>();
		tempList.addAll(values);

		friendsDisplay.getOtherValues().clear();
		List<Value> friendList = new ArrayList<Value>();
		for (Friend f : friends) {
			friendsDisplay.getOtherValues().add(new Value(f.getName(), false, false));
		}
		tempList.add(friendsDisplay);
		return tempList;
	}

	@Override
	public Object getValue(String n) {
		if (n.equals(TEAMS)) {
			return Teams;
		} else if (n.equals(MIDDLEMOUSE)) {
			return middleMouseFriends;
		} else if (n.equals(SAVINGFRIENDS)) {
			String s = ",";
			for (Friend friend : friends) {
				String friendData = friend.getName() + ";" + friend.getAlias();
				s += friendData + ",";
			}
			return s;
		} else {
			return false;
		}
	}

	@Override
	public void setValue(String n, Object v) {
		if (n.equals(TEAMS)) {
			Teams = (Boolean) v;
		} else if (n.equals(MIDDLEMOUSE)) {
			middleMouseFriends = (Boolean) v;
		} else if (n.equals(SAVINGFRIENDS)) {
			friends.clear();
			String[] obj = String.valueOf(v).split(",");
			for (String s : obj) {
				if (!s.equals("")) {
					try {
						String[] friendData = s.split(";");
						Friend newFriend = new Friend(friendData[0], friendData[1]);
						friends.add(newFriend);
					} catch (Exception oi) {
						break;
					}
				}
			}
		}
	}

	public class Friend {

		private String name;
		private String alias;

		public Friend(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}

		public String getName() {
			return name;
		}

		public String getAlias() {
			return alias;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Friend other = (Friend) obj;
			if (!getOuterType().equals(other.getOuterType())) return false;
			if (name == null) {
				if (other.name != null) return false;
			} else if (!name.equals(other.name)) return false;
			return true;
		}

		private Friends getOuterType() {
			return Friends.this;
		}

	}

}
