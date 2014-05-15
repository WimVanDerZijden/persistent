package nl.saxion.persistent.view;

import nl.saxion.persistent.model.User;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public abstract class BaseFragment extends Fragment
{

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	protected static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static BaseFragment newInstance(int sectionNumber)
	{
		// TODO Implement the loading of different fragments here.
		BaseFragment fragment = null;
		switch (sectionNumber)
		{
		case 1:
			fragment = new LoginFragment();
			break;
		case 2:
			fragment = new PlaceholderFragment();
			break;
		case 3:
			fragment = new PlaceholderFragment();
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
