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
	private String name;
	private int capacity;

	public static Location get(String name) {
		Cursor cursor = DB.get(
				"SELECT name, capacity FROM Locations WHERE name = ?", name);
		if (cursor.moveToFirst())
			return new Location(cursor);
		return null;
	}
	/**
	 * Get all Locations
	 * 
	 * Returns empty list when no locations found
	 * 
	 * @return locations
	 */
	public static List<Location> getAll() {
		Cursor cursor = DB.get("SELECT name, capacity FROM Locations");
		List<Location> locationList = new ArrayList<Location>();
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			do {
				locationList.add(new Location(cursor));
			} while (cursor.moveToNext());
		}
		return locationList;
	}

	private Location(Cursor cursor) {
		name = cursor.getString(0);
		capacity = cursor.getInt(1);
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

}
