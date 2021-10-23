package net.spookysquad.spookster.gui.accountmanager.auth;

import java.io.IOException;
import java.net.URL;

import net.spookysquad.spookster.gui.accountmanager.auth.payload.AuthPayload;
import net.spookysquad.spookster.gui.accountmanager.auth.response.Session;
import net.spookysquad.spookster.gui.accountmanager.auth.util.HTTPRequester;

public class ModernAuth implements AuthMethod {

	/**
	 * The server to attempt auth with
	 */
	private static final String AUTH_SERVER = "https://authserver.mojang.com/";

	@Override
	public Session login(String username, String password) throws AuthenticationException, IOException {
		HTTPRequester requester = new HTTPRequester(new URL(AUTH_SERVER + "authenticate"));
		AuthPayload payload = new AuthPayload(username, password);
		Session responseSession = requester.gsonRequest(payload, Session.class);

		return responseSession;
	}

	@Override
	public boolean isSessionValid(Session s) {
		return true; //TODO implement check with Mojang server to see if session is valid
	}

}
