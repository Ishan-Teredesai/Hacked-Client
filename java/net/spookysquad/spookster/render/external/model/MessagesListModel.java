package net.spookysquad.spookster.render.external.model;

import javax.swing.DefaultListModel;

import net.spookysquad.spookster.render.external.console.ConsoleManager;

public class MessagesListModel extends DefaultListModel<String> {
	private static final long serialVersionUID = 1L;
	private ConsoleManager console;

	public MessagesListModel(ConsoleManager console) {
		this.console = console;
	}
	
	@Override
	public String getElementAt(int index) {
		return this.console.getList().get(index).getElement();
	}

	@Override
	public void addElement(String paramE) {
		super.addElement(paramE);
	}
	
	@Override
	public int getSize() {
		return this.console.getList().size();
	}
	
}
