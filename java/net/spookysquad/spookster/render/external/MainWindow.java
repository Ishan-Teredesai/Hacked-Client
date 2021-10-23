package net.spookysquad.spookster.render.external;

import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import net.minecraft.util.ResourceLocation;
import net.spookysquad.spookster.Spookster;
import net.spookysquad.spookster.render.external.console.ConsoleManager;
import net.spookysquad.spookster.render.external.console.MessageType;
import net.spookysquad.spookster.render.external.model.MessagesListModel;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {

	private static boolean font = Arrays.asList(
			GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()).contains(
			"MS Reference Sans Serif");
	private static String globalfontname = font ? "MS Reference Sans Serif" : "Verdana";
	private static Font globalfont = font ? new Font(globalfontname, 0, 10) : new Font(globalfontname, 0, 10);
	private static JLabel lblAccounts, lblAltProxies, lblNames, lblNameProxies;

	public static ConsoleManager mainConsole = new ConsoleManager();
	private static MessagesListModel alt_console_list = new MessagesListModel(mainConsole);
	private static JList<String> main_console_table;
	private JTabbedPane baseTabbedPane;
	private JPanel mainPanel;

	public MainWindow() {
//		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource(new ResourceLocation("spookster", "textures/icon_editable.png").getResourcePath())));
		setTitle("Spookster " + Spookster.instance.getVersion() + " " + Spookster.instance.clientAuthor);
		setBounds(100, 100, 600, 590);
		setResizable(false);
		setAlwaysOnTop(true);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(null);

		baseTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		baseTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		baseTabbedPane.setBorder(null);
		baseTabbedPane.setBounds(0, 0, 594, 561);
		setThemaPanel(baseTabbedPane);
		addConsole(baseTabbedPane);
		mainPanel.add(baseTabbedPane);

		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (mainConsole.getList().isEmpty()) {
					mainConsole.addMessage("Welcome to Spookster made by " + Spookster.instance.clientAuthor + ".", MessageType.NOTIFCATION);
					mainConsole.addMessage("Current version: " + Spookster.clientVersion, MessageType.NOTIFCATION);
				}
				if (!mainConsole.getList().isEmpty()) {
					main_console_table.validate();
				}
			}
		}, new Date(), 250);
	}

	private void addConsole(JTabbedPane mainTabbedPane) {
		JScrollPane console_tab = new JScrollPane();
		console_tab.setFocusable(false);
		mainTabbedPane.addTab("Console", null, console_tab, null);
		main_console_table = new JList<String>();
		main_console_table.setModel(alt_console_list);
		main_console_table.setBackground(new Color(52, 52, 52));
		main_console_table.setForeground(new Color(255, 255, 255));
		main_console_table.setFont(globalfont);
		console_tab.setViewportView(main_console_table);
	}

	private void setThemaPanel(JComponent panel) {
		panel.setFocusable(false);
		panel.setFont(new Font(globalfontname, 0, 14));
		panel.setForeground(Color.WHITE);
		panel.setBackground(new Color(42, 42, 42));
	}

	public static void updateConsole() {
		main_console_table.updateUI();
	}

}
