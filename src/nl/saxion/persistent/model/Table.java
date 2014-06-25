package nl.saxion.persistent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to generically access multiple tables.
 * 
 * TODO
 * Rewrite to be more dynamic. Currently this code has
 * to be modified for every new table.
 * 
 * @author Wim van der Zijden
 *
 */

public abstract class Table {
	
	public enum TableName { LOCATION, USER, EVENT };
	
	public static List<Table> get(TableName name, List<Filter> filters) {
		List<Table> lst = new ArrayList<Table>();
		switch(name) {
		case EVENT:
			for (Event o : Event.get(filters))
				lst.add(o);
			break;
		case LOCATION:
			for (Location o : Location.get(filters))
				lst.add(o);
			break;
		case USER:
			for (User o : User.get(filters))
				lst.add(o);
			break;
		}
		return lst;
	}
	
	public static Table getById(TableName name, int id) {
		Table table = null;
		switch(name) {
		case EVENT:
			table = Event.getById(id);
			break;
		case LOCATION:
			table = Location.getById(id);
			break;
		case USER:
			table = User.getById(id);
			break;
		}
		return table;
	}
	
	public abstract int getId();
	 
}
