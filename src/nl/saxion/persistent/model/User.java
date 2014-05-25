package nl.saxion.persistent.model;

import java.io.ByteArrayOutputStream;

import nl.saxion.persistent.view.mainfragment.LoginFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class User
{
	private String name;
	//private Uri photo;
	//private String phonenumber;
	private String email;
	private Bitmap photo;

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
		Cursor cursor = DB.get("SELECT name, email, photo FROM User WHERE Email = ? AND password = ?", email, password);
		User user = cursor.moveToFirst() ? new User(cursor) : null;
		cursor.close();
		return user;
	}

	private User(Cursor cursor)
	{
		name = cursor.getString(0);
		email = cursor.getString(1);
		try
		{
			byte[] photo_bytes = cursor.getBlob(2);
			if (photo_bytes != null && photo_bytes.length > 0);
				photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
		}
		catch (Exception e)
		{
			Log.i("User", "Could not load photo: " + e.getMessage());
		}
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}
	
	public Bitmap getPhoto()
	{
		return photo;
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
	
	public boolean setPhoto(Bitmap bitmap)
	{
		try
		{
			ByteArrayOutputStream blob = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, blob);
			DB.doIt("UPDATE User SET photo = ? WHERE email = ?", blob.toByteArray(), email);
			photo = bitmap;
			return true;
		}
		catch (Exception e)
		{
			Log.e("User", "Failed to save photo in db: " + e.getMessage());
			return false;
		}
	}
	
	

}
