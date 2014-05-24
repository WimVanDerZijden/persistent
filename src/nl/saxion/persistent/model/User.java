package nl.saxion.persistent.model;

import nl.saxion.persistent.view.mainfragment.LoginFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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

	public boolean setName(String name)
	{
		if (name == null || name.length() < 2)
			return false;
		try
		{
			DB.doIt("UPDATE User SET Name = ? WHERE Email = ?", name, email);
			this.name = name;
			return true;
		}
		catch (SQLiteException e)
		{
			Log.e("User", "SQL failed: " + e.getMessage());
			return false;
		}
	}
	
	public boolean setPassword(String current, String new_pass, String repeat)
	{
		if (current == null || new_pass == null || repeat == null ||
				current.length() < LoginFragment.PASSWORD_MIN_LENGTH ||
				new_pass.length() < LoginFragment.PASSWORD_MIN_LENGTH ||
				!new_pass.equals(repeat))
			return false;
		int updated = DB.doItCount("UPDATE User SET password = ? WHERE email = ? AND password = ?", new_pass, email, current);
		// This cannot update more than 1 row, because email is a key
		if (updated == 1)
			return true;
		return false;
	}

}
