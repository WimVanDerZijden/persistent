package nl.saxion.persistent.model;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class Event {
	private int id;
	private String name;
	private Long datetime;
	private Integer duration;
	private Integer maxparticipants; 
	private Integer minparticipants;
	

	private String description;
	
	private Long datetime1;
	private Long datetime2;
	private Long datetime3;
	
	private Event(Cursor cursor) {
		name = cursor.getString(0);
		datetime = cursor.getLong(1);
		duration = cursor.getInt(2);
		maxparticipants = cursor.getInt(3);
		minparticipants = cursor.getInt(4);
		description = cursor.getString(5);
		datetime1 = cursor.getLong(6);
		datetime2 = cursor.getLong(7);
		datetime3 = cursor.getLong(8);
		id = cursor.getInt(9);
		
		
		
	}

	public int getId() {
		return id;
	}
	
	public static boolean createEvent(String name, Long datetime, int duration, int maxparticipants, int minparticipants, String description){
		return false;
	}

	public static List<Event> getAll() {
		Cursor cursor = DB.get("SELECT name, datetime, duration, maxparticipants, minparticipants, description, datetime1, datetime2, datetime3, id FROM Event");
		List<Event> eventList = new ArrayList<Event>();
		if (cursor.moveToFirst()) {
			do {
				eventList.add(new Event(cursor));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return eventList;
	}
	public String getName() {
		return name;
	}

	public Long getDatetime() {
		return datetime;
	}

	public Integer getDuration() {
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
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
