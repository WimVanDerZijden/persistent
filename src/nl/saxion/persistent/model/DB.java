package nl.saxion.persistent.model;

import java.io.InputStream;
import java.util.Scanner;

import nl.saxion.persistent.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper
{
	private Context context;
	private static DB db_helper;

	private DB(Context context)
	{
		super(context, "Persistent5", null, 1);
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
		for (int n = 0; n < params.length; n++)
			s_params[n] = params[n].toString();
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
		db.execSQL(sql, params);
	}

	/**
	 * Runs the provided raw resource as a script that doesn't return anything.
	 * 
	 * All terminators (;) must be at the end of a line.
	 * 
	 * @param db
	 * @param rawResourceId
	 */
	public void runScript(SQLiteDatabase db, int rawResourceId)
	{
		InputStream in = context.getResources().openRawResource(R.raw.db_create);
		Scanner s = new Scanner(in);
		String sql = "";
		while (s.hasNext())
		{
			sql += "\n" + s.nextLine();
			if (sql.endsWith(";"))
			{
				db.execSQL(sql);
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
		throw new IllegalStateException("Upgrade not implemented");
	}
}
