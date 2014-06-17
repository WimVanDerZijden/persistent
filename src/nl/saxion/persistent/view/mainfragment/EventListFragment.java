package nl.saxion.persistent.view.mainfragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.saxion.persistent.R;
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
	List<Event> events;
	List<HashMap<String, Object>> groupList;
	List<ArrayList<HashMap<String, String>>> childList;
	SimpleExpandableListAdapter sela;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
		ExpandableListView eventList = (ExpandableListView) rootView.findViewById(R.id.event_list);
		events = Event.getAll();
		groupList = createGroupList();
		childList = createChildList();
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
		// To update adapter:
		//sela.notifyDataSetChanged();
		return rootView;
	}

	private List<HashMap<String, Object>> createGroupList() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < events.size(); ++i) { 
			Event event = events.get(i);
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("Name", event.toString());
			m.put("Date", DateFormat.getDateInstance().format(event.getDatetime()));
			m.put("Location", "/Events location");
			result.add(m);
		}
		return result;
	}

	private List<ArrayList<HashMap<String, String>>> createChildList() {

		ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
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
		return result;
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
