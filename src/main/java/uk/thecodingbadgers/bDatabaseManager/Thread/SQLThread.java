package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.bukkit.plugin.java.JavaPlugin;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.Database.SQLDatabase;

public class SQLThread extends DatabaseThread {
	
	SQLDatabase.SQLOptions m_options;
	
	public void Setup(String database, JavaPlugin plugin, SQLDatabase.SQLOptions options, int updateTime) {
		super.Setup(null, plugin, updateTime);
		m_options = options;
		m_options.databaseName = database;
	}
	
	public void login(String host, String user, String password, int port) {
		m_options.host = host;
		m_options.username = user;
		m_options.password = password;
		m_options.port = port;
	}
	
	protected synchronized void ExecuteQuieries() {
		
		if (m_queries.size() != 0)
		{		
			String query = null;
			Iterator<String> queryItr = null;
			Connection connection = null;
			Statement statement = null;
			
			try {	
				if ((connection = GetConnection()) == null) {
					Utilities.OutputError(m_plugin, "Could not connect to database.");
					return;
				}
			
				statement = connection.createStatement();
				
				queryItr = m_queries.iterator();
				while (queryItr.hasNext()) {
					query = queryItr.next();
					statement.addBatch(query);	
				}
				
				statement.executeBatch();

				statement.close();
				connection.close();
				m_queries.clear();
			} catch (SQLException e) {
				if (!(e.getMessage().toLowerCase().contains("locking") || e.getMessage().toLowerCase().contains("locked"))) {
					System.out.println("[BDM] Batch Exception: Exception whilst executing");
					System.out.println(query);
					e.printStackTrace();
					m_queries.remove(query);
				}	
				try {
					statement.close();
					connection.close();
				} catch (SQLException ce) {
				}
			}		
		}				
	}
	
	private Connection GetConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Utilities.OutputError(m_plugin, "Could not find sql drivers");
		  return null;
		}
		
		try {
			return DriverManager.getConnection("jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
												m_options.databaseName + "?" +
												"user=" + m_options.username +
												"&password=" + m_options.password);
		} catch (SQLException e) {
			return null;
		}
		
	}

}
