package nl.saxion.persistent.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.controller.Filter;
import nl.saxion.persistent.view.mainfragment.LoginFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class User extends Table {
	private int id;
	private String name;
	// private Uri photo;
	// private String phonenumber;
	private String email;
	private Bitmap photo;
	private boolean isThales;

	/**
	 * Return User if email password is correct.
	 * 
	 * Otherwise return null.
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static User get(String email, String password) {
		Cursor cursor = DB.get("SELECT name, email, photo, id, is_thales FROM User_v WHERE Email = ? AND password = ?",
				email, password);
		User user = cursor.moveToFirst() ? new User(cursor) : null;
		cursor.close();
		return user;
	}
	
	/**
	 * Get user by id
	 * 
	 * @param id
	 * @return
	 */
	public static User getById(int id) {
		Cursor cursor = DB.get("SELECT name, email, photo, id, is_thales FROM User_v WHERE id = ?", id);
		User user = cursor.moveToFirst() ? new User(cursor) : null;
		cursor.close();
		return user;
	}

	/**
	 * Get all users
	 * 
	 * @return
	 */
	public static List<User> getAll()
	{
		return get(null);
	}
	
	/**
	 * Get users filtered
	 * 
	 * @param filters
	 * @return
	 */
	
	public static List<User> get(List<Filter> filters) {
		String sql = "SELECT name, email, photo, id, is_thales FROM User_v "
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
		sql += " ORDER BY name";
		Log.e("User",params.toString());
		Log.e("User",sql);
		Cursor cursor = DB.get(sql, params.toArray());
		return getAll(cursor);
	}
	
	/**
	 * Get all users from cursor.
	 * 
	 * @param cursor
	 * @return
	 */
	public static List<User> getAll(Cursor cursor)
	{
		List<User> userList = new ArrayList<User>();
		if (cursor.moveToFirst()) {
			do {
				userList.add(new User(cursor));
			}
			while (cursor.moveToNext());
		}
		cursor.close();
		return userList;
	}

	public static User register(String name, String email, String password) {
		if (password == null || password.length() < LoginFragment.PASSWORD_MIN_LENGTH
				|| email == null || email.length() < 1 || !email.contains("@")
				|| name == null || name.length() < 1)
			return null;
		try {
			DB.doIt("INSERT INTO User (name,email,password) VALUES (?,?,?)",
					name, email, password);
			User user = get(email, password);
			return user;
		}
		catch (SQLiteException e) {
			Log.i("User", "Can't register user: " + e.getMessage());
			return null;
		}
	}

	protected User(Cursor cursor) {
		name = cursor.getString(0);
		email = cursor.getString(1);
		id = cursor.getInt(3);
		isThales = cursor.getInt(4) == 1;
		try {
			byte[] photo_bytes = cursor.getBlob(2);
			if (photo_bytes != null && photo_bytes.length > 0)
				photo = BitmapFactory.decodeByteArray(photo_bytes, 0, photo_bytes.length);
		}
		catch (Exception e) {
			Log.i("User", "Could not load photo: " + e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Bitmap getPhoto() {
		return photo;
	}
	
	public int getId() {
		return id;
	}

	public boolean isThales() {
		return isThales;
	}

	public boolean setName(String name) {
		if (name == null || name.length() < 2)
			return false;
		try {
			DB.doIt("UPDATE User SET Name = ? WHERE Email = ?", name, email);
			this.name = name;
			return true;
		}
		catch (SQLiteException e) {
			Log.e("User", "SQL failed: " + e.getMessage());
			return false;
		}
	}

	public boolean setPassword(String current, String new_pass, String repeat) {
		if (current == null || new_pass == null || repeat == null
				|| current.length() < LoginFragment.PASSWORD_MIN_LENGTH
				|| new_pass.length() < LoginFragment.PASSWORD_MIN_LENGTH
				|| !new_pass.equals(repeat))
			return false;
		int updated = DB
				.doItCount(
						"UPDATE User SET password = ? WHERE email = ? AND password = ?",
						new_pass, email, current);
		// This cannot update more than 1 row, because email is a key
		if (updated == 1)
			return true;
		return false;
	}

	public boolean setPhoto(Bitmap bitmap) {
		try {
			ByteArrayOutputStream blob = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, blob);
			DB.doIt("UPDATE User SET photo = ? WHERE email = ?", blob.toByteArray(), email);
			photo = bitmap;
			return true;
		}
		catch (Exception e) {
			Log.e("User", "Failed to save photo in db: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * User friendly display string for use in Spinners and such
	 * 
	 */
	public String toString()
	{
		return name;
	}

}
