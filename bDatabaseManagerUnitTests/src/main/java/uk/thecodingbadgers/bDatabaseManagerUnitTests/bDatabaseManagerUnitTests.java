package uk.thecodingbadgers.bDatabaseManagerUnitTests;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;

/**
 * @author The Coding Badgers
 *
 * Some simple tests for the bDatabaseManager API
 * 
 */
public class bDatabaseManagerUnitTests extends JavaPlugin {
	
	BukkitDatabase 	m_sqliteDatabase = null;
	DatabaseTable 	m_sqlTable = null;
	
	@Override
	public void onEnable() {
		
		createSQLiteDatabase();
		createSQLiteTable();
		insertSQLiteData();
		
	}

	@Override
	public void onDisable() {
		
	}
	
	private void log(final String message) {
		Bukkit.getLogger().log(Level.INFO, "[BDM Tests] " + message);
	}
	
	private void createSQLiteDatabase() {
		m_sqliteDatabase = bDatabaseManager.createDatabase("TestSQL", this, DatabaseType.SQLite);
		if (m_sqliteDatabase == null) {
			log("createSQLiteDatabase Failed!");
			return;
		}
		
		log("createSQLiteDatabase Passed");
	}	
	
	private void createSQLiteTable() {
		if (m_sqliteDatabase == null) {
			return;
		}
		
		m_sqlTable = m_sqliteDatabase.createTable("TestTable", SQLiteTestData.class);
		if (m_sqlTable == null) {
			log("createSQLiteTable Failed!");
			return;
		}
		
		log("createSQLiteTable Passed");
	}
	
	private void insertSQLiteData() {
		if (m_sqlTable == null) {
			return;
		}
		
		SQLiteTestData data = new SQLiteTestData();
		data.id = 0;
		data.name = "SAM";
		data.time = System.currentTimeMillis();
		
		SQLiteTestData data1 = new SQLiteTestData();
		data1.id = 1;
		data1.name = "JAMES";
		data1.time = System.currentTimeMillis();
		
		SQLiteTestData data2 = new SQLiteTestData();
		data2.id = 2;
		data2.name = "NIC";
		data2.time = System.currentTimeMillis();
		
		if (!m_sqlTable.insert(data, SQLiteTestData.class, true)) {
			log("insertSQLiteData Failed!");
			return;
		}
		
		if (!m_sqlTable.insert(data1, SQLiteTestData.class, true)) {
			log("insertSQLiteData Failed!");
			return;
		}
		
		if (!m_sqlTable.insert(data2, SQLiteTestData.class, true)) {
			log("insertSQLiteData Failed!");
			return;
		}
		
		log("insertSQLiteData Passed");
	}
	
}
