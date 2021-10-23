package net.spookysquad.spookster.render.external.console;

import java.awt.Color;

public class ConsoleMessage {

	private String message;
	private MessageType type;

	public ConsoleMessage(String message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStringType() {
		return MessageType.getTypeString(type);
	}
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}


	public String getElement() {
		Color col = MessageType.getColour(this.getType());
		String color = "rgb(" + col.getRed() + "," + col.getGreen() + "," + col.getBlue() + ")";
		return ("<html><font color=" + color + ">" + this.getMessage().replace("<", "<>").replace(">", "<>") + "</font></html>");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsoleMessage other = (ConsoleMessage) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
