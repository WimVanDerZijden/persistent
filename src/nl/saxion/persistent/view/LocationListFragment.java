package nl.saxion.persistent.view;

import nl.saxion.persistent.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationListFragment extends MenuFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);
		ListView locationList = (ListView) rootView.findViewById(R.id.loction_list);
		String[] locationArray = {"1", "2", "3"};
		locationList.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, locationArray));
		return rootView;
	}
}
