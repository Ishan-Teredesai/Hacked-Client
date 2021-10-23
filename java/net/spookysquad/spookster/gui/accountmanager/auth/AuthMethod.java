package net.spookysquad.spookster.gui.accountmanager.auth;

import java.io.IOException;

import net.spookysquad.spookster.gui.accountmanager.auth.response.Session;

public interface AuthMethod {

	/**
	 * Login to Minecraft and get a session
	 *
	 * @param username Minecraft username/email
	 * @param password Minecraft password
	 * @return
	 */
	public Session login(String username, String password) throws AuthenticationException, IOException;

	/**
	 * Checks with Mojang if the current session is valid
	 *
	 * @param session Session to check
	 * @return Whether the session is valid
	 */
	public boolean isSessionValid(Session session);

}
