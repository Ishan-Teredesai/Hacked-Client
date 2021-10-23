package net.spookysquad.spookster.gui.accountmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.spookysquad.spookster.Spookster;

public class AccountManager {

	public static final List<MinecraftAccount> minecraftAccounts = new ArrayList<MinecraftAccount>();

	private static File accountFile = new File(Spookster.SAVE_FOLDER, "alts.data");

	static {
		if (!AccountManager.accountFile.exists()) {
			try {
				AccountManager.accountFile.createNewFile();
			} catch (IOException e) {
				Spookster.logger.info("Failed creating alts.data | Exception: " + e.getMessage());
			}
		} else {
			AccountManager.loadAccounts();
		}
	}

	public static void loadAccounts() {
		minecraftAccounts.clear();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(
					AccountManager.accountFile));
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				final String[] lineSplit = currentLine.trim().split(":");
				final String username = lineSplit[0].trim();
				final String password = lineSplit.length == 1 ? "" : lineSplit[1].trim();
				AccountManager.minecraftAccounts.add(new MinecraftAccount(username, password));
			}
			reader.close();
		} catch (final Exception e) {
			Spookster.logger.info("Failed loading alts | Exception: " + e.getMessage());
		}
	}

	public static void saveAccounts() {
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(
					AccountManager.accountFile));
			for (final MinecraftAccount account : AccountManager.minecraftAccounts) {
				writer.write(account.username + ":" + account.password);
				writer.newLine();
			}
			writer.close();
		} catch (final Exception e) {
			Spookster.logger.info("Failed saving alts | Exception: " + e.getMessage());
		}
	}
}
