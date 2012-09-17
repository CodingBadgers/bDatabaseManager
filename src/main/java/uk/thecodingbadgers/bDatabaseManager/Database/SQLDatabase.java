package uk.thecodingbadgers.bDatabaseManager.Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.Thread.SQLThread;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLDatabase extends BukkitDatabase {
	
	public class SQLOptions {
		public String	host;
		public String 	databaseName;
		public String 	username;
		public String 	password;
		public int 		port;
	}
	
	private SQLOptions m_options;

	public SQLDatabase(String name, JavaPlugin owner, int updateTime) {
		super(name, owner, DatabaseType.SQL, updateTime);
		
		m_options = new SQLOptions();
		m_options.databaseName = name;
		
		SQLThread thread = new SQLThread();
		thread.Setup(name, m_plugin, m_options, updateTime);
		m_thread = thread;
		m_thread.start();
	}
	
	public boolean login(String host, String user, String password, int port) {
		m_options.host = host;
		m_options.username = user;
		m_options.password = password;
		m_options.port = port;
		
		m_thread.login(host, user, password, port);
		
		return GetConnection() != null;
	}
	
	private Connection GetConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Utilities.OutputError(m_plugin, "Could not find sql drivers");
		  return null;
		}
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + m_options.host + ":" + m_options.port + "/" +
												m_options.databaseName + "?" +
												"user=" + m_options.username +
												"&password=" + m_options.password);
			
			if (connection == null) 
				Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
			
			return connection;
	
		} catch (SQLException e) {
			return null;
		}
		
	}
		
	public void Query(String query, boolean instant) {
		
		if (!instant)
			m_thread.Query(query);
		else
		{
			try {
				
				Connection connection = null;
				if ((connection = GetConnection()) == null)
					return;
				
				m_statement = connection.createStatement();
				m_statement.executeUpdate(query);
				
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet QueryResult(String query) {
				
		try {
			Connection connection = null;

			if ((connection = GetConnection()) == null) {
				Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
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
		}
		
		return null;		
	}
			
	public boolean TableExists(String name) {
		
		try {
			Connection connection = null;

			if ((connection = GetConnection()) == null) {
				Utilities.OutputError(m_plugin, "Could not connect to database '" + m_databaseName + "' using '" + m_options.username + "'@'" + m_options.host + ":" + m_options.port + "' using password '" + (m_options.password.length() == 0 ? "NO" : "YES") + "'");
				return false;
			}
			
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet tables = metaData.getTables(null, null, name, null);
			boolean exists = tables.next();
			
			tables.close();
			connection.close();
			
			return exists;
			
		} catch (SQLException e) {
			return false;
		}		
		
	}

}
