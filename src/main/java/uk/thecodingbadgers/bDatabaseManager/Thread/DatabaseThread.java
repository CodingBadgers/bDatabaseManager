package uk.thecodingbadgers.bDatabaseManager.Thread;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class DatabaseThread extends Thread {
	
	protected boolean m_running = false;
	protected JavaPlugin m_plugin = null;
	protected ArrayList<String> m_queries = new ArrayList<String>();	
	protected File m_databaseFile = null;
	protected int m_updateTime = 0;
	
	public DatabaseThread() {
		
	}
	
	public void run () {
		
		m_running = true;
		
		while (m_running) {
			
			ExecuteQuieries();
			
			try {
				Thread.sleep(m_updateTime * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		Release();
		
	}
	
	// only used on sql
	public void login(String host, String dbname, String user, int port) {
	}
	
	private void Release() {
		
		ExecuteQuieries();
		
	}
	
	protected synchronized void ExecuteQuieries() {
						
	}
	
	public void Kill() {
		m_running = false;
	}

	public void Setup(File database, JavaPlugin plugin, int updateTime) {
		
		m_databaseFile = database;
		m_plugin = plugin;
		m_running = true;
		m_updateTime = updateTime;
		
	}
	
	public synchronized void Query(String query) {

		m_queries.add(query);
			
	}

}
