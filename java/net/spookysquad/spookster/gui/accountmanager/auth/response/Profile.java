package net.spookysquad.spookster.gui.accountmanager.auth.response;

import com.google.gson.annotations.SerializedName;

public class Profile {

	/**
	 * The hexadecimal profile id
	 */
	@SerializedName("id")
	private final String id;
	/**
	 * Name for the profile
	 */
	@SerializedName("name")
	private final String name;
	/**
	 * Whether the profile is legacy
	 */
	@SerializedName("legacy")
	private boolean legacy = false;

	/**
	 * Creates a new profile with the specified id and display name
	 *
	 * @param id          Profiler identifier
	 * @param displayName Display name
	 * @param legacy      Whether the profile is legacy
	 * @deprecated This should never be used as objects of this class should be generated with GSON.
	 * This constructor is only used with legacy auth.
	 */
	public Profile(String id, String displayName, boolean legacy) {
		this.id = id;
		this.name = displayName;
		this.legacy = legacy;
	}

	/**
	 * Creates a new legacy profile with the specified id and display name
	 *
	 * @param id          Profiler identifier
	 * @param displayName Display name
	 * @deprecated This should never be used as objects of this class should be generated with GSON.
	 * This constructor is only used with legacy auth.
	 */
	public Profile(String id, String displayName) {
		this(id, displayName, false);
	}

	/**
	 * Gets the hexadecimal profile id
	 *
	 * @return Profile id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Gets the profile display name
	 *
	 * @return Display name for the profile
	 */
	public String getDisplayName() {
		return this.name;
	}

	/**
	 * Gets whether the session is legacy
	 *
	 * @return Whether the session is legacy
	 */
	public boolean isLegacy() {
		return this.legacy;
	}

}
