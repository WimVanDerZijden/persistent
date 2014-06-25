package nl.saxion.persistent.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.model.Column;
import nl.saxion.persistent.model.Column.DataType;
import nl.saxion.persistent.model.Table;
import nl.saxion.persistent.model.Table.TableName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Filter
{
	public static List<Filter> get(TableName tableName, Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		String json = prefs.getString(tableName.name(), null);
		List<Filter> filters;
		if (json == null) {
			// Set default filter
			filters = new ArrayList<Filter>();
			Column[] cols = Column.get(tableName);
			if (tableName == TableName.EVENT) {
				// TODO fix this filter (Create a view Event_v with a bool for IsInFuture)
				Filter filter = new Filter(cols[1], Operator.GREATER_THAN, System.currentTimeMillis());
				filters.add(filter);
			}
			else {
				Log.i("Filter", "No default filter found for: " + tableName);
			}
			Editor editor = prefs.edit();
			editor.putString(tableName.name(), new Gson().toJson(filters));
			editor.commit();
		}
		else {
			// We need to use TypeToken to specify the correct class, because
			// ArrayList<Filter>.class doesn't work.
			Log.i("Filter", json);
			filters = new Gson().fromJson(json, new TypeToken<ArrayList<Filter>>() {
			}.getType());
			Log.i("Filter", "Filter loaded for: " + tableName.toString());
		}
		return filters;
	}

	public static void save(TableName tableName, Context ctx, List<Filter> filters) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		Editor editor = prefs.edit();
		editor.putString(tableName.name(), new Gson().toJson(filters));
		editor.commit();
	}

	private Column column;
	private Operator operator;
	private Object value;

	public enum Operator
	{
		EQUAL(" = ", "is"),
		NOT_EQUAL(" != ", "isn't"),
		GREATER_THAN(" > "),
		LESS_THAN(" < "),
		GREATER_THAN_OR_EQUAL(" >= ", "at least"),
		LESS_THAN_OR_EQUAL(" <= ", "at most"),
		LIKE(" LIKE ", "contains"),
		NOT_LIKE(" NOT LIKE ", "doesn't contain"),
		IS_TRUE(" = 1 ", "Yes"),
		IS_FALSE(" = 0 ", "No"),
		BEFORE(" < ", "before"),
		AFTER(" > ", "after");

		private String name;
		private String sql;

		private Operator(String sql)
		{
			this.name = sql;
			this.sql = sql;
		}

		private Operator(String sql, String name) {
			this.name = name;
			this.sql = sql;
		}

		public String getSQL()
		{
			return sql;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public Filter(Column column, Operator operator, Object value)
	{
		this.column = column;
		this.operator = operator;
		this.value = value;
	}
	
	public String getSQL()
	{
		if (column.getDataType() == DataType.TEXT)
			return " UPPER(" + column.getColumnName() + ")" + operator.getSQL() + "UPPER(?)";
		if (column.getDataType() == DataType.BOOLEAN)
			return " " + column.getColumnName() + operator.getSQL();
		// Default (Reference, Date, Number)
		return " " + column.getColumnName() + operator.getSQL() + "?";
	}

	public Operator getOperator()
	{
		return operator;
	}

	public Object getValue()
	{
		if (column.getDataType() == DataType.NUMBER)
			return getValueAsInt();
		return value;
	}

	public Spanned getDisplayHtml() {
		String result = "<b>" + column.getName() + "</b> <i>" + operator.toString() + "</i> ";
		switch (column.getDataType())
		{
		case TIMESTAMP:
			result += DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(getValueAsLong());
			break;
		case REFERENCE:
			result += Table.getById(column.getTableName(), getValueAsInt());
			break;
		case NUMBER:
			result += getValueAsInt();
			break;
		case TEXT:
			// Text is stored with prefix and postfix %, which is cut here.
			result += value.toString().substring(1, value.toString().length() - 1);
			break;
		case BOOLEAN:
			// value is included in operator, so nothing to do here
			break;
		}
		return Html.fromHtml(result);
	}

	/**
	 * Value may be parsed from json by Gson. Gson doesn't know that the value was
	 * originally an Integer. Therefore we cast to Number instead of to Integer.
	 * 
	 * This throws an Exception if value cannot be cast to Number.
	 * 
	 * @return
	 */
	public Integer getValueAsInt() {
		return ((Number) value).intValue();
	}

	/**
	 * Value may be parsed from json by Gson. Gson doesn't know that the value was
	 * originally a Long. Therefore we cast to Number instead of to Long.
	 * 
	 * This throws an Exception if value cannot be cast to Number.
	 * 
	 * @return
	 */
	public Long getValueAsLong() {
		return ((Number) value).longValue();
	}

}
