package net.spookysquad.spookster.render.external.console;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import net.spookysquad.spookster.render.external.MainWindow;

public class ConsoleManager {
	private ArrayList<ConsoleMessage> messages = new ArrayList<ConsoleMessage>();

	public void addMessage(final String message, final MessageType type) {
		ConsoleMessage mes = new ConsoleMessage(message, type);
		messages.add(mes);
		MainWindow.updateConsole();
	}

	public void removeMessage(ConsoleMessage message) {
		if (messages.contains(message)) {
			messages.remove(message);
		}
	}

	public ArrayList<ConsoleMessage> getList() {
		return messages;
	}

}
