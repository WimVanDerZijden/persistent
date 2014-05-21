package nl.saxion.persistent.view;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.DB;
import nl.saxion.persistent.model.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
	private static User user;
	private static String EMAIL = "email";
	private static String PASSWORD = "password";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DB.init(this);
		setContentView(R.layout.activity_main);
		mTitle = getTitle();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		if (autoLogin())
			showNavigation();
		else
		{
			showLoggedOut();
		}
	}
	
	public void showNavigation()
	{
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
		mNavigationDrawerFragment.getView().setVisibility(View.VISIBLE);
	}
	
	/**
	 * Sets the main fragment for the activity.
	 * 
	 * @param fragment
	 */
	
	public void setMainFragment(BaseFragment fragment)
	{
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position)
	{
		setMainFragment(MenuFragment.newInstance(position + 1));
	}

	public void onSectionAttached(int number)
	{
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
		}
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
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			
			if (mTitle.equals(getString(R.string.title_section1))){
				getMenuInflater().inflate(R.menu.event, menu);
			} else if (mTitle.equals(getString(R.string.title_section2))){
				
			} else if (mTitle.equals(getString(R.string.title_section3))){
				
			} else if (mTitle.equals(getString(R.string.title_section4))){
				
			}
				
			getMenuInflater().inflate(R.menu.main, menu);
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
		if (id == R.id.action_settings)
		{
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} 
		if (id == R.id.action_logout)
		{
			logOut();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static User getUser()
	{
		return user;
	}
	
	public void logOut()
	{
		user = null;
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.remove(EMAIL);
		editor.remove(PASSWORD);
		editor.commit();
		showLoggedOut();
	}
	
	public boolean autoLogin()
	{
		user = null;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String email = sp.getString(EMAIL,null);
		String password = sp.getString(PASSWORD, null);
		if (email != null && password != null)
			return login(email,password);
		return false; 
	}
	
	public boolean login(String email, String password)
	{
		User user = User.get(email, password);
		if (user == null)
			return false;
		MainActivity.user = user;
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString(EMAIL, email);
		editor.putString(PASSWORD, password);
		editor.commit();
		showNavigation();
		onNavigationDrawerItemSelected(0);
		return true;
	}
	
	public void showLoggedOut()
	{
		setMainFragment(new LoginFragment());
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setTitle(R.string.title_login);
	}

}
