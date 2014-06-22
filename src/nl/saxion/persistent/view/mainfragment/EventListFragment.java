package nl.saxion.persistent.view.mainfragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Event;
import nl.saxion.persistent.model.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

public class EventListFragment extends MainFragment implements OnChildClickListener {
	List<Event> events;
	List<HashMap<String, String>> groupList;
	List<ArrayList<HashMap<String, String>>> childList;
	SimpleExpandableListAdapter sela;

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
				new String[] { "Name","Date", "Availability" }, // the key of group item.
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
		events = Event.getAll();
		updateGroupList(groupList);
		updateChildList(childList);
		sela.notifyDataSetChanged();
		super.onStart();
		
	}
	/**
	 * 
	 * @param result
	 */
	private void updateGroupList(List<HashMap<String, String>> result) {
		result.clear();
		for (int i = 0; i < events.size(); ++i) { 
			Event event = events.get(i);
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("Name", event.getName());
			m.put("Date", DateFormat.getDateInstance().format(event.getDatetime()));
			m.put("Availability", "" + event.getUsers().size() + " / " + event.getLocation().getCapacity());
			result.add(m);
		}
	}

	private void updateChildList(List<ArrayList<HashMap<String, String>>> result) {
		result.clear();
		for (int i = 0; i < events.size(); ++i) {
			ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
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
			case 0: createDetailsDialog(groupPosition);
					break;
			case 1: createPeopleSignedUpDialog(groupPosition);
					break;
			case 2: createSignUpDialog(groupPosition);
					break;
		}
		return false;
	}
	

	/**
	 * Creates a dialog thats will ask the user if he wants to sign up for the
	 * event
	 * 
	 * @param groupPosition The position of this event in the list of events
	 */
	private void createSignUpDialog(final int groupPosition) {
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setMessage(R.string.sign_up_confirmation)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
								String email = sp.getString("email", null);
								String password = sp.getString("password", null);
								if(events.get(groupPosition).register(User.get(email, password)))
									Toast.makeText(getActivity(), "Signed up succesfully", Toast.LENGTH_LONG).show();
								else
									Toast.makeText(getActivity(), "Error while signing up", Toast.LENGTH_LONG).show();
							}
						})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}).create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	/**
	 * Create a dialog with a list of people currently signed up for the event
	 * 
	 * @param groupPosition The position of this event in the list of events
	 */
	private void createPeopleSignedUpDialog(int groupPosition) {
		final ListView peopleListView = new ListView(getActivity());
		// TODO put data in listView
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setView(peopleListView)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		})
		.create();
		alertDialog.show();
	}
	/**
	 * Create a dialog with all the additional information about a event
	 * 
	 * @param groupPosition The position of this event in the list of events
	 */
	private void createDetailsDialog(int groupPosition) {
		Event event = events.get(groupPosition);
		DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
		final String dialogMessage = "Description:" + event.getDescription() + "\n\nTime: " + tf.format(event.getDatetime())
				+ " - " + tf.format(event.getDatetime() + event.getDuration()) + "\nLocation: " + event.getLocation().getName();
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
		.setMessage(dialogMessage)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}
		})
		.create();
		alertDialog.show();
	}

}
