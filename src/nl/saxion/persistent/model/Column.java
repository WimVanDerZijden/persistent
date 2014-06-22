package nl.saxion.persistent.model;

import java.util.HashMap;
import java.util.Map;

public class Column
{
	private static final Map<String,Column[]> COLUMNS = new HashMap<String, Column[]>();
	
	static
	{
		Column[] eventColumns = new Column[2];
		eventColumns[0] = new Column("Date and time","datetime",DataType.TIMESTAMP);
		eventColumns[1] = new Column("Name","name",DataType.TEXT);
		COLUMNS.put(Event.TABLE_NAME, eventColumns);
	}
	
	public static Column[] get(String tableName)
	{
		// Immutable, so return a clone
		return COLUMNS.get(tableName).clone();
	}
	
	private String name;
	private String columnName;
	private DataType dataType;

	public enum DataType
	{
		TIMESTAMP, TEXT, NUMBER, USER, LOCATION, EVENT
	}
	
	public Column(String name, String columnName, DataType dataType)
	{
		this.name = name;
		this.columnName = columnName;
		this.dataType = dataType;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

}
