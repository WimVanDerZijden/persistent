package nl.saxion.persistent.view;

import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Location;
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
		List<Location> locations = Location.getAll(); 
		locationList.setAdapter(new ArrayAdapter<Location>(this.getActivity(), android.R.layout.simple_list_item_1, locations));
		return rootView;
	}
}
