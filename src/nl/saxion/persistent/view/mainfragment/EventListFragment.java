package nl.saxion.persistent.view.mainfragment;

import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Event;
import nl.saxion.persistent.model.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class EventListFragment extends MainFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
		ListView eventList = (ListView) rootView.findViewById(R.id.event_list);
		List<Event> events = Event.getAll(); 
		eventList.setAdapter(new ArrayAdapter<Event>(this.getActivity(), android.R.layout.simple_list_item_1, events));
		return rootView;
	}

}
