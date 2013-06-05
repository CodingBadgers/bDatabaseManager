bDatabaseManager
==========

Description:
------------
bDatabaseManager is a database API for Bukkit (a Minecraft server mod).
The api allows the user to create a database of the following types:
 - SQL
 - SQLite

Features:
---------
 - Easy to use.
 - SQL and SQLite support.
 - Seporate execution thread, meaning queries are non-blocking.
 - Ability to force a query to execute instantally (this will cause the command to block).
 - Ability to return a result from a query.
 - A command to release all data associated with a query result.
 - Fast and light weight.
 
Example:
------------

// Create an SQLite database
m_database = bDatabaseManager.createDatabase("MyDatabase", plugin, DatabaseType.SQLite);

// Create a table in the SQLite database
m_table = m_database.createTable("TestTable", TestData.class);

// Insert some data into the table of the type TestData,
// also insert the data instantally rather than adding it to the query thread.
m_table.insert(data, TestData.class, true);

// Select all the data from the table
ResultSet allData = m_table.select("*");

// Select the name from the table where the id is 7;
ResultSet id7Data = m_table.select("name", "id='7'");

// Free the results;
m_database.freeResult(allData);
m_database.freeResult(id7Data);

// Free the database
m_sqliteDatabase.freeDatabase();


License:
------------
This API is released under the GPL 3.0 license.
Foe more information please see http://www.gnu.org/licenses/gpl-3.0.txt