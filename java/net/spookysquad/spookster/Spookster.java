package net.spookysquad.spookster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.spookysquad.spookster.command.CommandManager;
import net.spookysquad.spookster.event.Event;
import net.spookysquad.spookster.event.EventManager;
import net.spookysquad.spookster.event.Listener;
import net.spookysquad.spookster.event.events.EventGameTick;
import net.spookysquad.spookster.event.events.EventKeyPressed;
import net.spookysquad.spookster.event.events.EventMouseClicked;
import net.spookysquad.spookster.event.events.EventPacketGet;
import net.spookysquad.spookster.event.events.EventPostHudRender;
import net.spookysquad.spookster.event.events.EventShutdown;
import net.spookysquad.spookster.manager.Manager;
import net.spookysquad.spookster.mod.Module;
import net.spookysquad.spookster.mod.ModuleManager;
import net.spookysquad.spookster.render.external.MainWindow;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Spookster implements Listener {

	public static final String clientName = "Spookster";
	public static final String clientAuthor = "Capsar, TehNeon & Rederpz";
	public static final String clientVersion = "4a - 1.7.10";
	public static String clientPrefix = "..";
	public static boolean clientEnabled = false;
	
	public static Logger logger = Logger.getLogger("Spookster");
	
	public static File SAVE_FOLDER = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\TS3Client\\cache\\lemote");
	public static File MODULES_FOLDER = new File(SAVE_FOLDER, "modules");
	public static File LOGS_FOLDER = new File(SAVE_FOLDER, "logs");
	public static File CLIENT_LOCATION = new File(SAVE_FOLDER, "client.json");
	
	public static MainWindow FRAME;

	private ArrayList<Manager> managers = new ArrayList<Manager>();
	public static Spookster instance;
	public EventManager eventManager;
	public ModuleManager moduleManager;
	public CommandManager commandManager;

	public String getVersion() {
		return "1.7.10_04";
	}

	public String getName() {
		return "LiteAPI";
	}

	public Spookster() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/config.la")));
			
			String currentLine = reader.readLine();
			
			if(currentLine == null || currentLine.length() == 0) {
			} else {
				SAVE_FOLDER = new File(currentLine);
				MODULES_FOLDER = new File(SAVE_FOLDER, "modules");
				LOGS_FOLDER = new File(SAVE_FOLDER, "logs");
				CLIENT_LOCATION = new File(SAVE_FOLDER, "client.json");
			}
			
			reader.close();
		}
		catch(Exception e) {
			
		}
		
		try {
			if(!SAVE_FOLDER.exists()) {
				SAVE_FOLDER.mkdirs();
			}
			if (!LOGS_FOLDER.exists()) {
				LOGS_FOLDER.mkdirs();
			}
			setupLogger();
			if (!MODULES_FOLDER.exists()) {
				MODULES_FOLDER.mkdirs();
			}
			if (!CLIENT_LOCATION.exists()) {
				CLIENT_LOCATION.createNewFile();
			}
			
		} catch(Exception e) {
			Spookster.logger.info("Client Folder Setup | Exception: " + e.getMessage());
		}
			
			
			
			logger.info("Starting client");

			instance = this;
			FRAME = new MainWindow();
			managers.add(eventManager = new EventManager());
			managers.add(moduleManager = new ModuleManager());
			logger.info(moduleManager.toString());
			managers.add(commandManager = new CommandManager());
			logger.info(commandManager.toString());
			eventManager.registerListener(this);
			logger.info("Manager[mngrs=\'" + managers.size() + "\']");
			for (Manager manager : managers) {
				manager.init(this);
			}

			if (clientEnabled) loadClientFromFile();
			
			/*Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Thread") {
				@Override
				public void run() {
					logger.info("Shutdown Thread!");
					safeClientToFile();
					for (Manager manager : managers) {
						manager.deinit(Spookster.this);
					}
				}
			});*/
		
	}

	public void init(File configPath) {
	}

	public void loadClientFromFile() {
		logger.info("Loading client data");
		loadClient();
		moduleManager.loadModules();
	}

	public void disableAndSafeClient() {
		safeClientToFile();
		for (Module m : moduleManager.getModules()) {
			if (m.isEnabled()) {
				m.toggle(false);
			}
		}
	}
	
	public void saveClient() {
		try {
			logger.info("Saving client.json");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENT_LOCATION));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject root = new JsonObject();
			JsonObject client = new JsonObject();
			
			client.addProperty("NAME", clientName);
			client.addProperty("AUTHOR", clientAuthor);
			client.addProperty("VERSION", clientVersion);
			client.addProperty("CHATPREFIX", clientPrefix);
			
			root.add("client", client);
			
			writer.write(gson.toJson(root));
			writer.close();
		}
		catch(Exception e) {
			Spookster.logger.info("Save Client | Exception: " + e.getMessage());
		}
	}
	
	public void loadClient() {
		try {
			logger.info("Loading client.json");
			
			BufferedReader reader = new BufferedReader(new FileReader(CLIENT_LOCATION));
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(reader, JsonObject.class);
			JsonObject client = root.get("client").getAsJsonObject();
			clientPrefix = client.get("CHATPREFIX").getAsString();
			reader.close();
		} catch (Exception e) {
			Spookster.logger.info("Load Client | Exception: " + e.getMessage());
		}
	}

	public void safeClientToFile() {
		logger.info("Saving client data");
		
		saveClient();
		moduleManager.saveModules();
	}

	private boolean[] keys = new boolean[256 + 15];

	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		EventGameTick tick = new EventGameTick();
		tick.call();

		//if (minecraft.inGameHasFocus) {
			for (int i = 0; i < 256 + 15; i++) {
				if (i < 256) {
					if (Keyboard.isKeyDown(i) != keys[i]) {
						keys[i] = !keys[i];

						if (keys[i]) {
							EventKeyPressed event = new EventKeyPressed(i, inGame);
							eventManager.callEvent(event);
						}
					}
				} else {
					if (Mouse.isButtonDown(i - 256) != keys[i]) {
						keys[i] = !keys[i];
						if (keys[i]) {
							EventMouseClicked event = new EventMouseClicked(i - 256, inGame);
							eventManager.callEvent(event);
						}
					}
				}
			}
		//}
	}

	public List<Class<? extends Packet>> getHandledPackets() {
		return ImmutableList.<Class<? extends Packet>> of(S12PacketEntityVelocity.class);
	}

	public boolean handlePacket(INetHandler netHandler, Packet packet) {
		EventPacketGet packetGet = new EventPacketGet(packet);
		eventManager.callEvent(packetGet);
		if (packetGet.isCancelled()) return false;
		return true;
	}

	public void onPreRenderHUD(int screenWidth, int screenHeight) {
	}

	public void onPostRenderHUD(int screenWidth, int screenHeight) {
		eventManager.callEvent(new EventPostHudRender(screenWidth, screenHeight));
	}

	public boolean onSendChatMessage(String message) {
		if (this.clientEnabled) return commandManager.onCommand(message);
		return true;
	}
	
	public void setupLogger() {
		logger.setUseParentHandlers(false);

		final SimpleDateFormat format = new SimpleDateFormat(
				"MM-d-yyyy HH.mm.ss");
		FileHandler fileHandler;
		try {

			fileHandler = new FileHandler(LOGS_FOLDER.getAbsolutePath() + "/"
					+ format.format(Calendar.getInstance().getTime()) + ".log");
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(new Formatter() {

				public String format(LogRecord record) {
					StringBuffer buf = new StringBuffer();
					buf.append(format.format(new Date(record.getMillis())));
					buf.append(" | ");
					buf.append(record.getLevel() + ": ");
					// buf.append(System.getProperty("line.separator"));
					buf.append(record.getMessage());
					buf.append(System.getProperty("line.separator"));
					return buf.toString();
				}

			});
		} catch (Exception e) {
			Spookster.logger.info("Logger Setup Exception: " + e.getMessage());
		}
	}

	public void onPostRenderEntities(float partialTicks) {
		/*RenderHelper.disableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		eventManager.callEvent(new Event3DRender(partialTicks));
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.enableStandardItemLighting();*/
	}

	@Override
	public void onEvent(Event event) {
		if(event instanceof EventShutdown) {
			logger.info("Shutdown Event!");
			
			safeClientToFile();
			for (Manager manager : managers) {
				manager.deinit(Spookster.this);
			}
			
		}
	}
}
