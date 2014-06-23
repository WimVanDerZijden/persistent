package nl.saxion.persistent.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.model.Column;
import nl.saxion.persistent.model.Event;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Filter
{
	public static List<Filter> get(String tableName, Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
		String json = prefs.getString(tableName, null);
		List<Filter> filters;
		if (json == null) {
			// Set default filter
			filters = new ArrayList<Filter>();
			if (tableName.equals("Event")) {
				// TODO fix this filter (Create a view Event_v with a bool for IsInFuture)
				Column[] eventCols = Column.get(Event.TABLE_NAME);
				Filter filter = new Filter(eventCols[0], Operator.GREATER_THAN, System.currentTimeMillis());
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
			Log.i("Filter",json);
			filters = new Gson().fromJson(json, new TypeToken<ArrayList<Filter>>(){}.getType()); 
			Log.i("Filter","Filter loaded for: " + tableName);
		}
		return filters;
	}
	
	public static void save(String tableName, Context ctx, List<Filter> filters) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());;
		Editor editor = prefs.edit();
		editor.putString(tableName, new Gson().toJson(filters));
		editor.commit();
	}
	
	private Column column;
	private Operator operator;
	private Object value;
	
	public enum Operator
	{
		EQUAL(" = "),
		NOT_EQUAL(" != "),
		GREATER_THAN(" > "),
		LESS_THAN(" < "),
		GREATER_THAN_OR_EQUAL (" >= "),
		LESS_THAN_OR_EQUAL(" <= ");
		
		private String name;
		
		private Operator(String name) {
			this.name = name;
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
		return " " + column.getColumnName() + operator.toString() + "?";
	}

	public Operator getOperator()
	{
		return operator;
	}

	public Object getValue()
	{
		return value;
	}
	
	public String getDisplayString() {
		// TODO resolve value
		String result = column.getName() + operator.toString();
		switch(column.getDataType())
		{
		case TIMESTAMP:
			return result + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(value);
		case EVENT:
			break;
		case LOCATION:
			break;
		case NUMBER:
			break;
		case TEXT:
			return result + value;
		case USER:
			break;
		default:
			break;
		}
		return result;
		//+ value;
	}
	
}
