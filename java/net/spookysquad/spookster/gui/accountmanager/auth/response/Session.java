package net.spookysquad.spookster.gui.accountmanager.auth.response;

import com.google.gson.annotations.SerializedName;

public class Session {

	/**
	 * Hexadecimal access token used to connect to servers
	 */
	@SerializedName("accessToken")
	private final String accessToken;
	/**
	 * The client token we sent earlier
	 */
	@SerializedName("clientToken")
	private final String clientToken;
	/**
	 * The currently selected profile
	 */
	@SerializedName("selectedProfile")
	private final Profile selectedProfile;
	/**
	 * An array of available profiles.
	 * I'm pretty sure that this is not implemented in Minecraft yet and has no use.
	 */
	@SerializedName("availableProfiles")
	private final Profile[] availableProfiles;

	/**
	 * Creates a new session
	 *
	 * @param profile     Session profile
	 * @param accessToken Access token
	 * @param clientToken Randomly generated client token
	 * @param availableProfiles Array of available profiles
	 * @deprecated This should never be used. Instead this class should be generated with GSON.
	 * This constructor is only used with the legacy auth.
	 */
	public Session(Profile profile, String accessToken, String clientToken, Profile[] availableProfiles) {
		this.selectedProfile = profile;
		this.accessToken = accessToken;
		this.clientToken = clientToken;
		this.availableProfiles = availableProfiles;
	}

	/**
	 * Gets the profile attached to the session
	 *
	 * @return Session's profile
	 */
	public Profile getProfile() {
		return this.selectedProfile;
	}

	/**
	 * A randomly generated number representing the client on authentication.
	 *
	 * @return The client token that was sent to the auth server.
	 */
	public String getClientToken() {
		return this.clientToken;
	}

	/**
	 * Access token generated to the server on authentication.
	 *
	 * @return Access token recieved from the auth server.
	 */
	public String getAccessToken() {
		return this.accessToken;
	}

}
