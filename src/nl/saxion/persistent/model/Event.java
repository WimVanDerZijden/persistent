package nl.saxion.persistent.model;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.controller.Filter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Event extends Table{
	
	public static final String TABLE_NAME = "Event";
	
	private int id;
	private String name;
	private Long datetime;
	private Long duration;
	private Integer maxparticipants; 
	private Integer minparticipants;
	
	private String description;
	
	private Location location;
	
	private Long datetime1;
	private Long datetime2;
	private Long datetime3;
	
	///** The users who registered for this event */
	//private List<User> users;
	
	/** The user who created this event */
	private User user;
	
	private Event(Cursor cursor) {
		name = cursor.getString(0);
		datetime = cursor.getLong(1);
		duration = cursor.getLong(2);
		maxparticipants = cursor.getInt(3);
		minparticipants = cursor.getInt(4);
		description = cursor.getString(5);
		datetime1 = cursor.getLong(6);
		datetime2 = cursor.getLong(7);
		datetime3 = cursor.getLong(8);
		id = cursor.getInt(9);
		user = User.getById(cursor.getInt(10));
		location = Location.getById(cursor.getInt(11));
	}

	public int getId() {
		return id;
	}
	
	public static boolean createEvent(User user, String name, Long datetime, Long duration, Integer maxparticipants, Integer minparticipants, String description, Location location){
		try {
			DB.doIt("INSERT INTO Event (user_id,name,duration,maxparticipants,minparticipants,description,datetime,location_id) VALUES (?,?,?,?,?,?,?,?)",
					user.getId(), name, duration, maxparticipants, minparticipants, description, datetime, location.getId());
			Log.i("Event", "Event created");
			return true;
		} catch (SQLiteException e){
			Log.e("Event", "SQLiteException: " + e.toString());
			return false;
		}
	}

	/**
	 * Get a list of events, filtered by the given list of filters.
	 * 
	 * Filters may be null or an empty list to get all events
	 * 
	 * @param filters
	 * @return
	 */
	public static List<Event> get(List<Filter> filters)
	{
		String sql = "SELECT name, datetime, duration, maxparticipants, minparticipants, description, datetime1, datetime2, datetime3, id, user_id, location_id "
				+ "FROM Event "
				+ "WHERE";
		List<Object> params = new ArrayList<Object>();
		if (filters != null) {
			for (Filter filter : filters) {
				sql += filter.getSQL();
				sql += " AND";
				if (filter.getValue() != null)
					params.add(filter.getValue());
			}
		}
		// Remove the last word from the query (WHERE or AND)
		sql = sql.substring(0, sql.lastIndexOf(" "));
		sql += " ORDER BY datetime";
		Cursor cursor = DB.get(sql, params.toArray());
		List<Event> eventList = new ArrayList<Event>();
		if (cursor.moveToFirst()) {
			do {
				eventList.add(new Event(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return eventList;
	}
	
	public static Event getById(int id) {
		Cursor cursor = DB.get("SELECT name, datetime, duration, maxparticipants, minparticipants, description, datetime1, datetime2, datetime3, id, user_id, location_id "
				+ "FROM Event "
				+ "WHERE id = ?", id);
		Event event = cursor.moveToFirst() ? new Event(cursor) : null;
		cursor.close();
		return event;
	}
	
	/**
	 * Register a user for an event by inserting a row into the Invite table
	 * with isAccepted = 1 (default value).
	 * 
	 * Will return false if user could not be registered. In this case the user
	 * was probably already registered.
	 * 
	 * @param event
	 * @param user
	 * @return
	 */
	public boolean register(User user) {
		try {
			DB.doIt("INSERT INTO Invite (event_id, user_id, datetime) VALUES (?,?,?)",
					getId(), user.getId(), System.currentTimeMillis());
			return true;
		}
		catch (SQLiteException e) {
			Log.e("User", "SQL failed: " + e.getMessage());
			return false;
		}
	}
	/**
	 * Removing a user from an event by removing a row from the invite table.
	 *  
	 * @param user
	 * @return
	 */
	public boolean unRegister(User user){
		try {
			DB.doIt("DELETE FROM Invite WHERE user_id=?", user.getId());
			return true;
		} catch (SQLiteException e) {
			Log.e("User", "SQL failed: " + e.getMessage());
			return false;
		}
		
	}
	
	public List<User> getUsers() {
		Cursor cursor = DB.get("SELECT u.name, u.email, u.photo, u.id FROM User u \n"
				+ "INNER JOIN Invite i ON i.user_id = u.id AND i.event_id = ?", getId());
		List<User> users = User.getAll(cursor);
		// User.getAll() already closes the cursor
		//cursor.close();
		return users;
	}
	
	/**
	 * Check if a specific user is registered for this event.
	 * 
	 * @return
	 */
	public boolean isRegistered(User user) {
		Cursor cursor = DB.get("SELECT u.name, u.email, u.photo, u.id FROM User u \n"
				+ "INNER JOIN Invite i ON i.user_id = u.id AND i.event_id = ? \n"
				+ "WHERE u.id = ?",
				getId(), user.getId());
		boolean result = cursor.moveToFirst();
		cursor.close();
		return result;
	}
	
	public String getName() {
		return name;
	}

	public Long getDatetime() {
		return datetime;
	}

	public Long getDuration() {
		return duration;
	}

	public Integer getMaxparticipants() {
		return maxparticipants;
	}

	public Integer getMinparticipants() {
		return minparticipants;
	}

	public String getDescription() {
		return description;
	}

	public Long getDatetime1() {
		return datetime1;
	}

	public Long getDatetime2() {
		return datetime2;
	}

	public Long getDatetime3() {
		return datetime3;
	}
	
	/**
	 * Get the user that created this event
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}
	
	public Location getLocation() {
		return location;
	}
	
	/**
	 * User friendly display string for use in Spinners and such
	 * 
	 */
	@Override
	public String toString()
	{
		return name;
	}
	
}
