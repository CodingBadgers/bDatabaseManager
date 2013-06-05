package uk.thecodingbadgers.bDatabaseManager.DatabaseTable;

import java.lang.reflect.Field;
import java.util.HashMap;

import uk.thecodingbadgers.bDatabaseManager.Utilities;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;

public abstract class DatabaseTable {
	
	/**
	 * 
	 */
	protected BukkitDatabase m_database = null;
	
	/**
	 * 
	 */
	protected String m_name = null;
	
	/**
	 * 
	 */
	private static HashMap<String, String> TYPECONVERSION = new HashMap<String, String>();
	
	
	/**
	 * @param name
	 * @param layout
	 * @return
	 */
	public boolean create(Class<?> layout) {
		
		Field[] publicFields = layout.getFields();

		if (publicFields.length == 0) {
			Utilities.outputError("The given table layout '" + layout.getName() + "' for the table '" + m_name + "' has no public fields.");
			return false;
		}
				
		String createTable = "CREATE TABLE '" + m_name + "' (";
		for (Field field : publicFields) {			
			final String fieldName = field.getName();
			final String fieldType = convertType(field.getType().getSimpleName());	
			
			if (fieldType == null) {
				Utilities.outputError("The given table layout '" + layout.getName() + "' for the table '" + m_name + "' has unknown type '" + field.getType().getSimpleName() + "'.");
				return false;
			}
			
			createTable += fieldName + " " + fieldType + ",";			
		}
		createTable = createTable.substring(0, createTable.length() - 1);
		createTable += ")";

		m_database.query(createTable, true);
		
		return true;
	}
	
	
	/**
	 * @param data
	 * @param layout
	 * @param instant
	 * @return
	 */
	public boolean insert(DatabaseTableData data, Class<?> layout, boolean instant) {
		
		Field[] publicFields = layout.getFields();
		
		String fields = "(";
		String values = "VALUES (";

		for (Field field : publicFields) {
			
			fields += field.getName() + ",";
			try {
				values += "\"" + field.get(data).toString() + "\",";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		fields = fields.substring(0, fields.length() - 1) + ") ";
		values = values.substring(0, values.length() - 1) + ");";
		
		String insertQuery = "INSERT INTO '" + m_name + "' " + fields + values;
		
		m_database.query(insertQuery, instant);
		
		return true;
	}
		
	
	/**
	 * @param type
	 * @return
	 */
	protected String convertType(String type) {	
		return TYPECONVERSION.get(type.toLowerCase());
	}
	
	
	/**
	 * 
	 */
	public static void addDefaultConversions() {
		addTypeConversion("int", "INT");
		addTypeConversion("long", "BIGINT");
		addTypeConversion("double", "DOUBLE");
		addTypeConversion("float", "FLOAT");
		addTypeConversion("char", "VARCHAR");
		addTypeConversion("string", "TEXT");
	}
	
	
	/**
	 * @param javaType
	 * @param databaseType
	 */
	public static void addTypeConversion(String javaType, String databaseType) {
		if (TYPECONVERSION.containsKey(javaType)) {
			TYPECONVERSION.remove(javaType);
		}
		TYPECONVERSION.put(javaType, databaseType);
	}

}