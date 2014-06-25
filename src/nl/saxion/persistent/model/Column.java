package nl.saxion.persistent.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.saxion.persistent.model.Filter.Operator;
import nl.saxion.persistent.model.Table.TableName;

public class Column
{
	private static final Map<TableName,Column[]> COLUMNS = new HashMap<TableName, Column[]>();
	private static final Map<DataType,Operator[]> OPERATORS = new HashMap<DataType, Operator[]>();
	
	/**
	 * Initializes the static data structure
	 * 
	 */
	static {
		int n;
		Column[] cols;
		
		cols = new Column[n = 7];
		cols[--n] = new Column("Initiator","user_id", TableName.USER);
		cols[--n] = new Column("Location","location_id", TableName.LOCATION);
		cols[--n] = new Column("Max Participants","maxparticipants", DataType.NUMBER);
		cols[--n] = new Column("Min Participants","minparticipants", DataType.NUMBER);
		cols[--n] = new Column("Open?","is_open",DataType.BOOLEAN);
		//cols[--n] = new Column("Registered","registered", DataType.NUMBER);
		cols[--n] = new Column("Date and time","datetime",DataType.TIMESTAMP);
		cols[--n] = new Column("Name","name",DataType.TEXT);
 		COLUMNS.put(TableName.EVENT, cols);
 		
 		cols = new Column[n = 3];
 		cols[--n] = new Column("Thales Employee?","is_thales", DataType.BOOLEAN);
 		cols[--n] = new Column("Email", "email", DataType.TEXT);
 		cols[--n] = new Column("Name","name", DataType.TEXT);
 		COLUMNS.put(TableName.USER, cols);
		
 		cols = new Column[n = 2];
 		cols[--n] = new Column("Capacity","capacity",DataType.NUMBER);
 		cols[--n] = new Column("Name","name",DataType.TEXT);
 		COLUMNS.put(TableName.LOCATION, cols);
 		
		Operator[] ops;
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.BEFORE;
		ops[--n] = Operator.AFTER;
		OPERATORS.put(DataType.TIMESTAMP, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.NOT_LIKE;
		ops[--n] = Operator.LIKE;
		OPERATORS.put(DataType.TEXT, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.LESS_THAN_OR_EQUAL;
		ops[--n] = Operator.GREATER_THAN_OR_EQUAL;
		OPERATORS.put(DataType.NUMBER, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.NOT_EQUAL;
		ops[--n] = Operator.EQUAL;
		OPERATORS.put(DataType.REFERENCE, ops);
		
		ops = new Operator[n = 2];
		ops[--n] = Operator.IS_FALSE;
		ops[--n] = Operator.IS_TRUE;
		OPERATORS.put(DataType.BOOLEAN, ops);
	}
	
	/**
	 * Get all searchable columns for this table 
	 * 
	 * @param tableName
	 * @return
	 */
	
	public static Column[] get(TableName tableName)	{
		// Immutable, so return a clone
		return COLUMNS.get(tableName).clone();
	}
	
	private String name;
	private String columnName;
	private DataType dataType;
	/** The table referenced. Only to be used if data type is reference. */
	private TableName tableName;
	
	public enum DataType {
		TIMESTAMP, TEXT, NUMBER, REFERENCE, BOOLEAN
	}
	
	public Column(String name, String columnName, DataType dataType) {
		this.name = name;
		this.columnName = columnName;
		this.dataType = dataType;
	}
	
	/**
	 * Constructor for reference column
	 * 
	 * @param name
	 * @param columnName
	 * @param tableName
	 */
	public Column(String name, String columnName, TableName tableName) {
		this(name,columnName, DataType.REFERENCE);
		this.tableName = tableName; 
	}
	
	/**
	 * Get all meaningful operators for this column
	 * 
	 * @return
	 */
	public Operator[] getOperators() {
		// Immutable, so return a clone
		return OPERATORS.get(dataType).clone();
	}
	
	/**
	 * Get all possible values for this column.
	 * Only for reference data type.
	 * 
	 * @return
	 */
	public List<Table> getValues() {
		return Table.get(tableName, null);
	}

	public String getName() {
		return name;
	}

	public String getColumnName() {
		return columnName;
	}

	public DataType getDataType() {
		return dataType;
	}
	
	public TableName getTableName() {
		return tableName;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
