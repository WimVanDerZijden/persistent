package nl.saxion.persistent.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Filter
{
	public static List<Filter> get(String tableName, Activity activity)
	{
		SharedPreferences prefs = activity.getPreferences(Activity.MODE_PRIVATE);
		String json = prefs.getString(tableName, null);
		List<Filter> filters;
		if (json == null) {
			// Set default filter
			filters = new ArrayList<Filter>();
			if (tableName.equals("Event")) {
				// TODO fix this filter (Create a view Event_v with a bool for IsInFuture)
				Filter filter = new Filter("Date and Time", "datetime", Operator.GREATER_THAN, System.currentTimeMillis());
				filters.add(filter);
			}
			else {
				Log.i("Filter","No default filter found for: " + tableName);
			}
			Editor editor = prefs.edit();
			editor.putString(tableName, new Gson().toJson(filters));
			editor.commit();
		}
		else {
			// We need to use TypeToken to specify the correct class, because
			// ArrayList<Filter>.class doesn't work.
			filters = new Gson().fromJson(json, new TypeToken<ArrayList<Filter>>(){}.getType()); 
			Log.i("Filter","Filter loaded for: " + tableName);
		}
		return filters;
	}
	
	private String name;
	private String columnName;
	private Operator operator;
	private Object value;
	
	public enum Operator
	{
		EQUAL, NOT_EQUAL, GREATER_THAN, LESS_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL
	}

	public Filter(String name, String columnName, Operator operator, Object value)
	{
		this.name = name;
		this.columnName = columnName;
		this.operator = operator;
		this.value = value;
	}
	
	public String getSQL()
	{
		return " " + getColumnName() + getOperatorSQL() + "?";
	}

	public String getColumnName()
	{
		return columnName;
	}

	public Operator getOperator()
	{
		return operator;
	}
	
	public String getOperatorSQL()
	{
		switch (operator)
		{
		case EQUAL:
			return " = ";
		case NOT_EQUAL:
			return " != ";
		case GREATER_THAN:
			return " > ";
		case LESS_THAN:
			return " < ";
		case GREATER_THAN_OR_EQUAL:
			return " >= ";
		case LESS_THAN_OR_EQUAL:
			return " <= ";
		}
		throw new IllegalStateException("Unknown operator");
	}

	public Object getValue()
	{
		return value;
	}
	
	public String getDisplayString() {
		// TODO resolve value
		return name + getOperatorSQL() + value;
	}
	
}
