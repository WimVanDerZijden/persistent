package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.view.MainActivity;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Every fragment that is used as a main fragment in the activity
 * should inherit this class and be added to the newInstance() method
 * with a unique identifier. 
 * 
 *
 */

public abstract class MainFragment extends Fragment
{

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	protected static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 * 
	 * Section numbers of 100 and up are not displayed in the drawer
	 */
	public static MainFragment newInstance(int sectionNumber)
	{
		MainFragment fragment = null;
		switch (sectionNumber)
		{
		case 1:
			fragment = new EventListFragment();
			break;
		case 2:
			fragment = new LocationListFragment();
			break;
		case 3:
			fragment = new PeopleListFragment();
			break;
		case 4: 
			fragment = new ProfileFragment();
			break;
		case 100:
			fragment = new LoginFragment();
			break;
		}
		
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		Bundle args = getArguments();
		int section = args != null ? args.getInt(ARG_SECTION_NUMBER) : 0;
		if (section > 0)
			((MainActivity) activity).onSectionAttached(section);
	}

}
