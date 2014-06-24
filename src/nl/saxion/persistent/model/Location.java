package nl.saxion.persistent.model;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.controller.Filter;
import android.database.Cursor;
import android.util.Log;
/**
 *
 *
 * @author Rayvr
 *
 */
public class Location extends Table{
	private int id;
	private String name;
	private int capacity;

	/**
	 * Get location by name.
	 * 
	 * @param name
	 * @return
	 */
	public static Location getByName(String name) {
		Cursor cursor = DB.get(
				"SELECT name, capacity, id FROM Locations WHERE name = ?", name);
		Location location = cursor.moveToFirst() ? new Location(cursor) : null;
		cursor.close();
		return location;
	}
	
	/**
	 * Get location by id.
	 * 
	 * @param id
	 * @return
	 */
	public static Location getById(int id) {
		Cursor cursor = DB.get(
				"SELECT name, capacity, id FROM Locations WHERE id = ?", id);
		Location location = cursor.moveToFirst() ? new Location(cursor) : null;
		cursor.close();
		return location;		
	}
	
	/**
	 * Get a list of all Locations.
	 * 
	 * Returns empty list when no locations found
	 * 
	 * @return locations
	 */
	public static List<Location> getAll() {
		return get(null);
	}

	/**
	 * Get all locations from a cursor
	 * 
	 * @param cursor
	 * @return
	 */
	public static List<Location> getAll(Cursor cursor) {
		List<Location> locationList = new ArrayList<Location>();
		if (cursor.moveToFirst()) {
			do {
				locationList.add(new Location(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return locationList;
	}
	
	/**
	 * Get a filtered list of Locations from database
	 * 
	 * Returns empty list when no locations found
	 * 
	 * @return locations
	 */
	public static List<Location> get(List<Filter> filters) {
		// TODO implement filters
		Cursor cursor = DB.get("SELECT name, capacity, id FROM Locations");
		return getAll(cursor);
	}
	
	/**
	 * Get all locations that are available for a certain date-time and duration,
	 * both in milliseconds. 
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Location> getAvailable(Long from, Long to) {
		String sql = "SELECT name, capacity, id FROM Locations l \n"
				+ "WHERE NOT EXISTS \n"
				+ "    (SELECT * FROM Event \n"
				+ "     WHERE location_id = l.id \n"
				+ "     AND ((? > datetime AND ? <= datetime_to) \n"
				+ "     OR (? >= datetime AND ? < datetime_to) \n"
				+ "     OR (? <= datetime AND ? >= datetime_to)))";
		Log.i("Location","to=" + to);
		Log.i("Location","from=" + from);
		Cursor cursor = DB.get(sql,to,to,from,from,from,to);
		return getAll(cursor);
	}

	private Location(Cursor cursor) {
		name = cursor.getString(0);
		capacity = cursor.getInt(1);
		id = cursor.getInt(2);
	}
	
	/**
	 * User friendly display string for use in Spinners and such
	 * 
	 */
	@Override
	public String toString(){
		return name + " (" + capacity + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public int getId() {
		return id;
	}

}
