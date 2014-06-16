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
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class EventListFragment extends MainFragment {
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
				new String[] { "Group Item","G2" }, // the key of group item.
				new int[] { R.id.row_name, R.id.row_name2 }, // ID of each group item. -Data under the key goes into this TextView.
				childList, // childData describes second-level entries.
				R.layout.child_row, // Layout for sub-level entries(second level).
				new String[] { "Sub Item" }, // Keys in childData maps to display.
				new int[] { R.id.grp_child } // Data under the keys above go into these TextViews.
				);
		eventList.setAdapter(sela);
		// To update adapter:
		//sela.notifyDataSetChanged();
		return rootView;
	}

	private List<HashMap<String, Object>> createGroupList() {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		//List<Event> events = Event.getAll();
		for (int i = 0; i < events.size(); ++i) { 
			HashMap<String, Object> m = new HashMap<String, Object>();
			m.put("Group Item", events.get(i).toString());
			m.put("G2","test");
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
			description.put("Sub Item", "Event description: " + event.getDescription());
			secList.add(description);
			//Adds the second item in the child list
			HashMap<String, String> date = new HashMap<String, String>();
			date.put("Sub Item", "Date: " + DateFormat.getDateInstance().format(event.getDatetime()));
			secList.add(date);
			//Adds the third item in the child list
			HashMap<String, String> time = new HashMap<String, String>();
			time.put("Sub Item", "Time: " + DateFormat.getTimeInstance().format(event.getDatetime()));
			secList.add(time);
			
			
			result.add(secList);
		}
		return result;
	}

}
