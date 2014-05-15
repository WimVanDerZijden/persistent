package nl.saxion.persistent.model;

import android.database.Cursor;

public class User
{
	private String name;
	//private Uri photo;
	//private String phonenumber;
	private String email;

	/**
	 * Return User if email password is correct.
	 * 
	 * Otherwise return null.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static User get(String email, String password)
	{
		Cursor cursor = DB.get("SELECT name, email FROM User WHERE Email = ? AND password = ?", email, password);
		if (cursor.moveToFirst())
			return new User(cursor);
		return null;
	}

	private User(Cursor cursor)
	{
		name = cursor.getString(0);
		email = cursor.getString(1);
		cursor.close();
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

}
