package nl.saxion.persistent.view;

import nl.saxion.persistent.R;
import nl.saxion.persistent.R.layout;
import nl.saxion.persistent.R.menu;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
/**
 * This activity shows the help for the app.
 * @author Erik
 *
 */
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{       
	    if (menuItem.getItemId() == android.R.id.home){
	    	onBackPressed();
	    }
	    return true;
	}

}
