package nl.saxion.persistent.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.saxion.persistent.controller.Filter.Operator;

public class Column
{
	private static final Map<String,Column[]> COLUMNS = new HashMap<String, Column[]>();
	private static final Map<DataType,Operator[]> OPERATORS = new HashMap<DataType, Operator[]>();
	
	static
	{
		Column[] eventColumns = new Column[2];
		eventColumns[0] = new Column("Date and time","datetime",DataType.TIMESTAMP);
		eventColumns[1] = new Column("Name","name",DataType.TEXT);
		COLUMNS.put(Event.TABLE_NAME, eventColumns);
		
		Operator[] ops;
		ops = new Operator[2];
		ops[0] = Operator.GREATER_THAN;
		ops[1] = Operator.LESS_THAN;
		OPERATORS.put(DataType.TIMESTAMP, ops);
		
		ops = new Operator[2];
		ops[0] = Operator.EQUAL;
		ops[1] = Operator.NOT_EQUAL;
		OPERATORS.put(DataType.TEXT, ops);
		
		//ops = 
	}
	
	/**
	 * Get all searchable columns for this table 
	 * 
	 * @param tableName
	 * @return
	 */
	
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
	
	/**
	 * Get all meaningful operators for this column
	 * 
	 * @return
	 */
	public Operator[] getOperators()
	{
		// Immutable, so return a clone
		return OPERATORS.get(dataType).clone();
	}
	

	public String getName()
	{
		return name;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public DataType getDataType()
	{
		return dataType;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

}
