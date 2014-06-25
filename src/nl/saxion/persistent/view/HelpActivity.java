package nl.saxion.persistent.view;

import nl.saxion.persistent.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
	
	public void clickEvent(View v){
		switch(v.getId()){
		case R.id.join_event_button:
			hideShowTv(R.id.join_event);
			break;
		case R.id.create_account_button:
			hideShowTv(R.id.create_account);
			break;
		case R.id.create_event_button:
			hideShowTv(R.id.create_event);
			break;
		case R.id.edit_account_button:
			hideShowTv(R.id.edit_account);
			break;
		case R.id.adding_filters_button:
			hideShowTv(R.id.adding_filters);
			break;
		}
	}
	private void hideShowTv(int tvId){
		TextView tv0 = (TextView) findViewById(tvId);
		tv0.setVisibility((tv0.getVisibility() + 8) % 16);
	}

}
