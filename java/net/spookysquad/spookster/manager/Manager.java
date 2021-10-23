package net.spookysquad.spookster.manager;

import net.spookysquad.spookster.Spookster;

public abstract class Manager {
	
	public abstract void init(Spookster spookster);
	
	public abstract void deinit(Spookster spookster);

}
