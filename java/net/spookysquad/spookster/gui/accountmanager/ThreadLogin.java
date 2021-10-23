package net.spookysquad.spookster.gui.accountmanager;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.gui.accountmanager.auth.ModernAuth;

class ThreadLogin implements Runnable {

	private final String username, password;

	private final Messageable parent;
	
	private static Field session;
	
	static {
		for(Field f : Minecraft.class.getDeclaredFields()) {
			if(f.getType().equals(Session.class)) {
				session = f;
			}
		}
		session.setAccessible(true);
	}

	ThreadLogin(final Messageable messageable, final String username,
			final String password) {
		this.parent = messageable;
		this.username = username;
		this.password = password;
	}

	@Override
	public void run() {
		if ((this.password == null || this.password.equals(""))
				&& !this.username.equals("")) {
			this.setSession(new Session(this.username, "0", "0", "0"));
			this.parent.setMessage("\247bLogged in as cracked - "
					+ this.username);
			return;
		}
		try {
			this.parent.setMessage("\2477\247kLogging in...");
			net.spookysquad.spookster.gui.accountmanager.auth.response.Session authSession = new ModernAuth().login(username, password);
			this.setSession(new Session(authSession.getProfile().getDisplayName(), authSession.getProfile().getId(), authSession.getAccessToken(), authSession.getClientToken()));
			this.parent.setMessage("\247aLogged in as "
					+ Minecraft.getMinecraft().getSession().getUsername());
		} catch (final Exception e) {
			this.parent.setMessage("\2474Failed to login");
			Spookster.logger.info("Failed to login | Exception: " + e.getMessage());
			return;
		}
	}
	
	private void setSession(Session s) {
		try {
			this.session.set(Minecraft.getMinecraft(), s);
		} catch (Exception e) {
			Spookster.logger.info("Failed setting session | Exception: " + e.getMessage());
		}
	}

}