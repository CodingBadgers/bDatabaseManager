package uk.thecodingbadgers.bDatabaseManager.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.Thread.SQLiteThread;

public class SQLiteDatabase extends BukkitDatabase {

	public SQLiteDatabase(String name, JavaPlugin owner, int updateTime) {
		super(name, owner, DatabaseType.SQLite, updateTime);		
				
		if (!CreateDatabase()) {
			Utilities.OutputError(m_plugin, "Could not create database '" + m_databaseName + "'");
			return;
		}
		
		m_thread = new SQLiteThread();
		m_thread.Setup(m_databaseFile, m_plugin, updateTime);
		m_thread.start();
	}
	
	private boolean CreateDatabase() {
		
		Utilities.OutputDebug(m_plugin, "Loading " + m_plugin.getDataFolder() + File.separator + m_databaseName + ".sqlite");
		
		m_databaseFile = new File(m_plugin.getDataFolder() + File.separator + m_databaseName + ".sqlite");
		
		if (m_databaseFile.exists())
			return true;
		
		if (!m_plugin.getDataFolder().exists()) {
			m_plugin.getDataFolder().mkdir();
		}
		
		try {
			if (!m_databaseFile.createNewFile())
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private Connection GetConnection() {
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Utilities.OutputError(m_plugin, "Could not find sqlite drivers");
		  return null;
		}
		
		try {
			return DriverManager.getConnection("jdbc:sqlite:" + m_databaseFile.getAbsolutePath());
		} catch (SQLException e) {
			return null;
		}
		
	}
		
	public void Query(String query, boolean instant) {
		
		if (!instant)
			m_thread.Query(query);
		else
		{
			Connection connection = null;
			try {
				
				if ((connection = GetConnection()) == null) {
					Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "'");
					return;
				}
				
				m_statement = connection.createStatement();
				m_statement.executeUpdate(query);
				
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					m_statement.close();
					connection.close();
				} catch (SQLException ce) {
				}
			}
		}
	}
	
	public ResultSet QueryResult(String query) {
		
		Connection connection = null;
		
		try {
			
			if ((connection = GetConnection()) == null) {
				Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "'");
				return null;
			}
			
			if (m_statement != null) {
				m_statement.close();
				m_statement = null;
			}
			
			m_statement = connection.createStatement();
			ResultSet result = m_statement.executeQuery(query);
			
			if (result == null) {
				m_statement.close();
				m_statement = null;
				connection.close();
				return null;
			}

			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				m_statement.close();
				connection.close();
			} catch (SQLException ce) {
			}
		}
		
		return null;		
	}
			
	public boolean TableExists(String name) {
		
		Connection connection = null;
		ResultSet tables = null;
		
		try {
			
			if ((connection = GetConnection()) == null) {
				Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "'");
				return false;
			}
			
			DatabaseMetaData metaData = connection.getMetaData();
			tables = metaData.getTables(null, null, name, null);
			boolean exists = tables.next();
			
			tables.close();
			connection.close();
			
			return exists;
			
		} catch (SQLException e) {
			try {
				tables.close();
				connection.close();
			} catch (SQLException ce) {
			}
			return false;
		}		
		
	}

}
