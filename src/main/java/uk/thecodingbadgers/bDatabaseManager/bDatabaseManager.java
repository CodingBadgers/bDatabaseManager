package uk.thecodingbadgers.bDatabaseManager;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLDatabase;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLiteDatabase;

public class bDatabaseManager extends JavaPlugin
{

	public enum DatabaseType {
		SQLite,
		SQL
	};
	
	static private ArrayList<BukkitDatabase> m_databases = new ArrayList<BukkitDatabase>();

	static public BukkitDatabase CreateDatabase(String name, JavaPlugin owner, DatabaseType type) {		
		return CreateDatabase(name, owner, type, 20);
	}
	
	static public BukkitDatabase CreateDatabase(String name, JavaPlugin owner, DatabaseType type, int updateTime) {
		
		BukkitDatabase database = null;
		
		switch (type) {
		case SQLite:
			database = new SQLiteDatabase(name, owner, updateTime);
			break;
			
		case SQL:
			database = new SQLDatabase(name, owner, updateTime);
			break;
		}
		
		m_databases.add(database);
		return database;
	}

}
