package nl.saxion.persistent.model;

import java.io.InputStream;
import java.util.Scanner;

import nl.saxion.persistent.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB extends SQLiteOpenHelper
{
	/**
	 * Increment when DB script is changed
	 */
	private static final int DB_VERSION = 23;

	private Context context;
	private static DB db_helper;

	private DB(Context context)
	{
		super(context, "Persistent", null, DB_VERSION);
		this.context = context;
	}

	/**
	 * To be called from the onCreate of MainActivity  
	 * 
	 * @return
	 */
	public static void init(Context context)
	{
		if (db_helper == null)
			// Using App context to avoid memory leaks:
			// http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html
			db_helper = new DB(context.getApplicationContext());
	}

	/**
	 *  Convert Objects to String.
	 *  SQLite will perform some magic to convert back to REAL/INTEGER:
	 *	http://www.sqlite.org/datatype3.html#expraff
	 * 
	 * @param params
	 * @return
	 */
	private static String[] objectsToString(Object[] params)
	{
		String[] s_params = new String[params.length];
		for (int n = 0; n < params.length; n++) {
			s_params[n] = params[n].toString();
		}
		return s_params;
	}

	/**
	 * Method to execute a query that returns a cursor:
	 * SELECT.
	 * 
	 * Assumes DB is already initialized: init() was called previously
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Cursor get(String sql, Object... params)
	{
		SQLiteDatabase db = db_helper.getReadableDatabase();
		Log.i("DB","SQL=" + sql);
		return db.rawQuery(sql, objectsToString(params));
	}

	/**
	 * Method to execute a query that doesn't return a cursor:
	 * UPDATE, INSERT.
	 * 
	 * Assumes DB is already initialized: init() was called previously
	 * 
	 * @param sql
	 * @param params
	 */

	public static void doIt(String sql, Object... params)
	{
		SQLiteDatabase db = db_helper.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys = ON");
		db.execSQL(sql, params);
		Log.i("DB","SQL=" + sql);
	}
	
	/**
	 * Same as doIt(), except this returns the number of
	 * affected rows.
	 * 
	 * @param table
	 * @param params
	 * @return
	 */
	
	public static int doItCount(String sql, Object... params)
	{
		SQLiteDatabase db = db_helper.getWritableDatabase();
		db.execSQL(sql, params);
		// "The changes() function returns the number of database rows that were changed or inserted or deleted by
		// the most recently completed INSERT, DELETE, or UPDATE statement, exclusive of statements in lower-level triggers."
		// source: http://www.sqlite.org/lang_corefunc.html#changes
		Cursor cursor = db.rawQuery("SELECT CHANGES()",null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		Log.i("DB","SQL=" + sql);
		Log.i("DB","Rows affected: " + count);
		return count;
	}

	/**
	 * Runs the provided raw resource as a script that doesn't return anything.
	 * 
	 * Warning: this is a NOT a foolproof SQL script interpreter.
	 * 
	 * Please note:
	 * All terminators (;) must be at the end of a line.
	 * 
	 * SQLiteErrors are logged as errors.
	 * They don't prevent the full script from being executed.
	 * 
	 * @param db
	 * @param rawResourceId
	 */
	public void runScript(SQLiteDatabase db, int rawResourceId)
	{
		Log.i("DB", "Running SQL script");
		InputStream in = context.getResources().openRawResource(rawResourceId);
		Scanner s = new Scanner(in);
		String sql = "";
		while (s.hasNext())
		{
			sql += " " + s.nextLine();
			if (sql.endsWith(";"))
			{
				try
				{
					db.execSQL(sql);
				}
				catch (SQLiteException e)
				{
					Log.e("DB","SQL failed: " + e.getMessage());
				}
				sql = "";
			}
		}
		s.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		runScript(db, R.raw.db_create);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		runScript(db, R.raw.db_create);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		runScript(db, R.raw.db_create);
	}

}
