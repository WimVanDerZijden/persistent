package nl.saxion.persistent.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
/**
 *
 *
 * @author Rayvr
 *
 */
public class Location {
	private int id;
	private String name;
	private int capacity;

	public static Location get(String name) {
		Cursor cursor = DB.get(
				"SELECT name, capacity, id FROM Locations WHERE name = ?", name);
		Location location = cursor.moveToFirst() ? new Location(cursor) : null;
		cursor.close();
		return location;
	}
	
	/**
	 * Get a list of Locations from database
	 * 
	 * Returns empty list when no locations found
	 * 
	 * @return locations
	 */
	public static List<Location> getAll() {
		Cursor cursor = DB.get("SELECT name, capacity FROM Locations");
		List<Location> locationList = new ArrayList<Location>();
		if (cursor.moveToFirst()) {
			do {
				locationList.add(new Location(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return locationList;
	}

	private Location(Cursor cursor) {
		name = cursor.getString(0);
		capacity = cursor.getInt(1);
		id = cursor.getInt(2);
	}
	
	@Override
	public String toString(){
		return name + "\n" + capacity;
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
