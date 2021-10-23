package net.spookysquad.spookster.mod;

public enum Type {

	COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), EXPLOITS("Exploits"), MISC("Misc"), WORLD("World"), CORE("");
	
	private final String typeName;

	private Type(String name) {
		typeName = name;
	}

	public String getName() {
		return typeName;
	}

	public static Type getValueOf(String upperCase) {
		try {
			return valueOf(upperCase);
		} catch (Exception e) {
			return CORE;
		}
	}

}
