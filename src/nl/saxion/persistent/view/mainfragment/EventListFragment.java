package nl.saxion.persistent.view.mainfragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.controller.Filter;
import nl.saxion.persistent.model.Event;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class EventListFragment extends MainFragment implements OnChildClickListener {
	
	private List<Event> events;
	private List<HashMap<String, String>> groupList;
	private List<ArrayList<HashMap<String, String>>> childList;
	private SimpleExpandableListAdapter sela;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
		ExpandableListView eventList = (ExpandableListView) rootView.findViewById(R.id.event_list);
		groupList = new ArrayList<HashMap<String, String>>();
		childList = new ArrayList<ArrayList<HashMap<String, String>>>();
		sela = new SimpleExpandableListAdapter(
				this.getActivity(),
				groupList, // Creating group List.
				R.layout.group_row, // Group item layout XML.
				new String[] { "Name","Date", "Location" }, // the key of group item.
				new int[] { R.id.row_name, R.id.row_date, R.id.row_location }, // ID of each group item. -Data under the key goes into this TextView.
				childList, // childData describes second-level entries.
				R.layout.child_row, // Layout for sub-level entries(second level).
				new String[] { "Sub Item" }, // Keys in childData maps to display.
				new int[] { R.id.grp_child } // Data under the keys above go into these TextViews.
				);
		eventList.setAdapter(sela);
		eventList.setOnChildClickListener(this);
		return rootView;
	}
	
	@Override
	public void onStart()
	{
		List<Filter> filters = Filter.get(Event.TABLE_NAME, getActivity());
		events = Event.get(filters);
		updateGroupList(groupList);
		updateChildList(childList);
		sela.notifyDataSetChanged();
		super.onStart();
	}

	private void updateGroupList(List<HashMap<String, String>> result) {
		result.clear();
		for (int i = 0; i < events.size(); ++i) { 
			Event event = events.get(i);
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("Name", event.getName());
			m.put("Date", DateFormat.getDateInstance().format(event.getDatetime()));
			m.put("Location", event.getLocation().getName());
			result.add(m);
		}
	}

	private void updateChildList(List<ArrayList<HashMap<String, String>>> result) {
		result.clear();
		for (int i = 0; i < events.size(); ++i) {
			ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
			Event event = events.get(i);
			//Adds the first item in the child list
			HashMap<String, String> description = new HashMap<String, String>();
			description.put("Sub Item", getString(R.string.more_details));
			secList.add(description);
			//Adds the second item in the child list
			HashMap<String, String> date = new HashMap<String, String>();
			date.put("Sub Item", getString(R.string.people));
			secList.add(date);
			//Adds the third item in the child list
			HashMap<String, String> time = new HashMap<String, String>();
			time.put("Sub Item", getString(R.string.sign_up));
			secList.add(time);
			result.add(secList);
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		switch(childPosition){
			case 0: //More Details
					break;
			case 1: //People Signed up
					break;
			case 2: //Sign up
					break;
		}
		
		Toast.makeText(getActivity(), "Group: " + groupPosition + " Child: " + childPosition, Toast.LENGTH_LONG).show();
		return false;
	}

}
