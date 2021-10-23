package net.spookysquad.spookster.render.external.console;

import java.awt.Color;

public enum MessageType {

	ERROR, SUCCES, HIGH, MEDIUM, NOTIFCATION;

	public static Color getColour(MessageType type) {
		switch (type) {
		case SUCCES:
			return new Color(34, 177, 76);
		case NOTIFCATION:
			return new Color(77, 109, 243);
		case MEDIUM:
			return new Color(255, 194, 14);
		case HIGH:
			return new Color(255, 126, 0);
		case ERROR:
			return new Color(237, 28, 36);
		default:
			return new Color(165, 165, 165);
		}
	}

	public static String getTypeString(MessageType type) {
		switch (type) {
		case SUCCES:
			return "Succes";
		case NOTIFCATION:
			return "Notify";
		case MEDIUM:
			return "Medium";
		case HIGH:
			return "High";
		case ERROR:
			return "Critical";
		default:
			return "Unknown";
		}
	}

}
