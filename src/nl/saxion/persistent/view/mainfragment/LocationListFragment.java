package nl.saxion.persistent.view.mainfragment;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Filter;
import nl.saxion.persistent.model.Location;
import nl.saxion.persistent.model.Table.TableName;
import nl.saxion.persistent.model.User;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LocationListFragment extends MainFragment {
	
	private List<Location> locations;
	private ArrayAdapter<Location> adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);
		ListView locationList = (ListView) rootView.findViewById(R.id.loction_list);
		locations = new ArrayList<Location>();
		adapter = new ArrayAdapter<Location>(getActivity(), android.R.layout.simple_list_item_1, locations);
		locationList.setAdapter(adapter);
		return rootView;
	}
	
	@Override
	public void onStart() {
		List<Filter> filters = Filter.get(TableName.LOCATION, getActivity());
		List<Location> new_locations = Location.get(filters);
		locations.clear();
		for (Location u : new_locations) {
			locations.add(u);
		}
		adapter.notifyDataSetChanged();
		super.onStart();
	}
}
