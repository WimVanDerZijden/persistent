package nl.saxion.persistent.view;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.DB;
import nl.saxion.persistent.model.Table.TableName;
import nl.saxion.persistent.model.User;
import nl.saxion.persistent.view.mainfragment.MainFragment;
import nl.saxion.persistent.view.mainfragment.RegisterDialogFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	/**
	 * The logged in user
	 * 
	 */
	private static User sUser;
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DB.init(this);
		setContentView(R.layout.activity_main);
		mTitle = getTitle();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
		if (autoLogin())
			mNavigationDrawerFragment.toggle(true);
		else
			logOut();
	}
	
	/**
	 * Sets the main fragment for the activity.
	 * 
	 * @param fragment
	 */
	
	public void setMainFragment(MainFragment fragment)
	{
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{
		setMainFragment(MainFragment.newInstance(position + 1));
	}

	public void onSectionAttached(int number)
	{
		Log.i("MainActivity","onSectionAttached:" + number);
		switch (number)
		{
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 100:
			mTitle = getString(R.string.title_login);
			break;
		}
		// Ask to rebuild option menu when new fragment attached 
		invalidateOptionsMenu();
	}

	public void restoreActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		Log.i("MainActivity","onCreateOptionsMenu:" + mTitle);
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			int menuId = -1;
			if (mTitle.equals(getString(R.string.title_section1)))
				menuId = R.menu.event;
			else if (mTitle.equals(getString(R.string.title_section2)))
				menuId = R.menu.location;
			else if (mTitle.equals(getString(R.string.title_section3)))
				menuId = R.menu.user;
			else if (mTitle.equals(getString(R.string.title_section4)))
				menuId = R.menu.main;
			else if (mTitle.equals(getString(R.string.title_login)))
				menuId = R.menu.login;
			else
				menuId = R.menu.global;
			
			getMenuInflater().inflate(menuId, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help)
		{
			Intent intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		} 
		if (id == R.id.action_logout)
		{
			logOut();
			return true;
		}
		if (id == R.id.action_register){
			register();
			return true;
		}
		if (id == R.id.action_create_event){
			Intent intent = new Intent(this, CreateEventActivity.class);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_filter_event) {
			Intent intent = new Intent(this,FilterActivity.class);
			intent.putExtra("TableName", TableName.EVENT);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_filter_user) {
			Intent intent = new Intent(this,FilterActivity.class);
			intent.putExtra("TableName", TableName.USER);
			startActivity(intent);
			return true;
		}
		if (id == R.id.action_filter_location) {
			Intent intent = new Intent(this,FilterActivity.class);
			intent.putExtra("TableName", TableName.LOCATION);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void register() {
		RegisterDialogFragment registerDialog = new RegisterDialogFragment();
		registerDialog.show(getFragmentManager(), "Register Dialog");
		
	}

	public static User getUser()
	{
		return sUser;
	}
	
	public void setUser(User user)
	{
		sUser = user;
		mNavigationDrawerFragment.selectItem();
		mNavigationDrawerFragment.toggle(true);
	}
	
	public void logOut()
	{
		sUser = null;
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.remove(EMAIL);
		editor.remove(PASSWORD);
		editor.commit();
		setMainFragment(MainFragment.newInstance(100));
		mNavigationDrawerFragment.toggle(false);
	}
	
	public boolean autoLogin()
	{
		if (sUser != null)
			return true;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String email = sp.getString(EMAIL, null);
		String password = sp.getString(PASSWORD, null);
		if (email != null && password != null)
			return login(email, password);
		return false;
	}
	
	public boolean login(String email, String password)
	{
		User user = User.get(email, password);
		if (user == null)
			return false;
		setUser(user);
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(EMAIL, email);
		editor.putString(PASSWORD, password);
		editor.commit();
		return true;
	}
	
	public void hideKeyBoard()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = getCurrentFocus();
		if (v != null)
			imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public void showKeyBoard()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = getCurrentFocus();
		if (v != null)
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);		
	}
}
