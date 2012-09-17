package uk.thecodingbadgers.bDatabaseManager;

import org.bukkit.plugin.java.JavaPlugin;

public class Utilities {
	
	public static void OutputError(JavaPlugin plugin, String message) {
		plugin.getLogger().severe("[BDM] " + message + ".");
	}
	
	public static void OutputDebug(JavaPlugin plugin, String message) {
		plugin.getLogger().info("[BDM] " + message + ".");
	}

}
