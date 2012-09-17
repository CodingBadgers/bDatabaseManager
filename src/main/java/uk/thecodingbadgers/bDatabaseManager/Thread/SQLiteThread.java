package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import uk.thecodingbadgers.bDatabaseManager.Utilities;

public class SQLiteThread extends DatabaseThread {

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
	
}
