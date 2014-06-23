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
		Column[] eventColumns = new Column[6];
		eventColumns[0] = new Column("Date and time","datetime",DataType.TIMESTAMP);
		eventColumns[1] = new Column("Name","name",DataType.TEXT);
		eventColumns[2] = new Column("Min Participants","minparticipants", DataType.NUMBER);
		eventColumns[3] = new Column("Max Participants","maxparticipants", DataType.NUMBER);
		eventColumns[4] = new Column("Location","location_id", DataType.LOCATION);
		eventColumns[5] = new Column("Organized By","user_id", DataType.USER);
		
 		COLUMNS.put(Event.TABLE_NAME, eventColumns);
		
		Operator[] ops;
		int n;
		ops = new Operator[n = 2];
		ops[--n] = Operator.GREATER_THAN;
		ops[--n] = Operator.LESS_THAN;
		OPERATORS.put(DataType.TIMESTAMP, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.NOT_LIKE;
		ops[--n] = Operator.LIKE;
		OPERATORS.put(DataType.TEXT, ops);
		
		ops = new Operator[n = 6];
		ops[--n] = Operator.EQUAL;
		ops[--n] = Operator.NOT_EQUAL;
		ops[--n] = Operator.LESS_THAN_OR_EQUAL;
		ops[--n] = Operator.GREATER_THAN_OR_EQUAL;
		ops[--n] = Operator.LESS_THAN;
		ops[--n] = Operator.GREATER_THAN;
		OPERATORS.put(DataType.NUMBER, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.NOT_EQUAL;
		ops[--n] = Operator.EQUAL;
		OPERATORS.put(DataType.LOCATION, ops);
		OPERATORS.put(DataType.USER, ops);
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
