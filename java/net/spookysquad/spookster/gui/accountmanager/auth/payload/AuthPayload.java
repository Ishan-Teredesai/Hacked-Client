package net.spookysquad.spookster.gui.accountmanager.auth.payload;

import java.util.UUID;

import net.spookysquad.spookster.gui.accountmanager.auth.MinecraftInfo;

import com.google.gson.annotations.SerializedName;

public class AuthPayload {

	/**
	 * The current agent (Mojang's fancy word for game. For this it is Minecraft.
	 */
	@SerializedName("agent")
	private final Agent agent = new Agent(MinecraftInfo.AGENT_NAME, MinecraftInfo.AGENT_VERSION);
	/**
	 * The account username/email
	 */
	@SerializedName("username")
	private final String username;
	/**
	 * The account password
	 */
	@SerializedName("password")
	private final String password;
	/**
	 * A randomly generated hexadecimal client token
	 */
	@SerializedName("clientToken")
	private final String clientToken;

	/**
	 * Creates a new authentication request to later be formatted with gson.
	 *
	 * @param username Mojang account username/email
	 * @param password Mojang account password
	 */
	public AuthPayload(String username, String password) {
		this.username = username;
		this.password = password;
		this.clientToken = UUID.randomUUID().toString(); //a randomly generated hexadecimal UUID
	}

}
