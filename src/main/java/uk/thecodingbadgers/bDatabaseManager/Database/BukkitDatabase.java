package uk.thecodingbadgers.bDatabaseManager.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.Thread.DatabaseThread;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitDatabase {
	
	protected DatabaseThread m_thread = null;
	protected JavaPlugin m_plugin = null;
	protected String m_databaseName = null;
	protected File m_databaseFile = null;
	protected Statement m_statement = null;
	protected int m_updateTime = 0;
	
	public BukkitDatabase(String name, JavaPlugin owner, DatabaseType type, int updateTime) {
		
		m_plugin = owner;
		m_databaseName = name;
		m_updateTime = updateTime;
	}
	
	protected void finalize() throws Throwable
	{

	} 
	
	// Only used on sql
	public boolean login(String host, String user, String password, int port) {
		return false;
	}
	
	public void Query(String query) {
		m_thread.Query(query);
	}
	
	public void Query(String query, boolean instant) {
		
	}
	
	public ResultSet QueryResult(String query) {
		return null;		
	}
	
	public void FreeResult(ResultSet result) {
		
		try {
			
			if (result != null)
				result.close();
			
			if (m_statement != null) {
				Connection connection = m_statement.getConnection();
				m_statement.close();
				connection.close();
			}			
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void End() {
		
		if (m_thread == null)
			return;
		
		try {
			m_statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		m_thread.Kill();
		m_thread = null;
		
	}
	
	public boolean TableExists(String name) {
		return false;
	}

}
