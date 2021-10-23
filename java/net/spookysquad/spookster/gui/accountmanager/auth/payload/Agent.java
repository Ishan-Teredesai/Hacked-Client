package net.spookysquad.spookster.gui.accountmanager.auth.payload;

import com.google.gson.annotations.SerializedName;

public class Agent {

	/**
	 * Name of the agent. Normally "Minecraft".
	 */
	@SerializedName("name")
	private final String name;
	/**
	 * Version of the agent.
	 */
	@SerializedName("version")
	private final int version;

	/**
	 * Creates a new agent
	 *
	 * @param name    Name of the agent
	 * @param version Version of the agent
	 */
	public Agent(String name, int version) {
		this.name = name;
		this.version = version;
	}

}
