package nl.saxion.persistent.view.mainfragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.controller.Filter;
import nl.saxion.persistent.model.Event;
import nl.saxion.persistent.model.User;
import nl.saxion.persistent.model.Table.TableName;
import nl.saxion.persistent.view.MainActivity;
import nl.saxion.persistent.view.MyArrayAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
	
	private void refresh()
	{
		List<Filter> filters = Filter.get(TableName.EVENT, getActivity());
		events = Event.get(filters);
		updateGroupList(groupList);
		updateChildList(childList);
		sela.notifyDataSetChanged();
	}
	@Override
	public void onStart(){
		refresh();
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
			m.put("Availability", "" + event.getUsers().size() + " / " + event.getMaxparticipants());
			result.add(m);
		}
	}

	private void updateChildList(List<ArrayList<HashMap<String, String>>> result) {
		result.clear();
		for (Event event : events) {
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
			if(event.isRegistered(MainActivity.getUser()))
				time.put("Sub Item", getString(R.string.sign_out));
			else
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
		final User user = MainActivity.getUser();
		final boolean signUp;
		final String signUpMessage;
		if(events.get(groupPosition).isRegistered(MainActivity.getUser())){
			signUpMessage = getString(R.string.sign_out_confirmation);
			signUp = false;
		} else {
			signUpMessage = getString(R.string.sign_up_confirmation);
			signUp = true;
		}
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setMessage(signUpMessage)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(!signUp){
									if(events.get(groupPosition).unRegister(user))
										Toast.makeText(getActivity(), "Signed out", Toast.LENGTH_LONG).show();
									else
										Toast.makeText(getActivity(), "Error Signing out", Toast.LENGTH_LONG).show();
								}
								else if(events.get(groupPosition).register(user))
									Toast.makeText(getActivity(), "Signed up succesfully", Toast.LENGTH_LONG).show();
								else
									Toast.makeText(getActivity(), "Error while signing up", Toast.LENGTH_LONG).show();
								refresh();
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
	 * Create a dialog with a list of people currently signed up for the event.
	 * Show a message if no people are currently signed up.
	 * 
	 * @param groupPosition The position of this event in the list of events
	 */
	private void createPeopleSignedUpDialog(int groupPosition) {
		final ListView peopleListView = new ListView(getActivity());
		if (events.get(groupPosition).getUsers().size() > 0) {
			MyArrayAdapter aa = new MyArrayAdapter(getActivity(), events.get(groupPosition).getUsers());
			peopleListView.setAdapter(aa);
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
		else {
			Toast.makeText(getActivity(), "No people currently signed up", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * Create a dialog with all the additional information about a event
	 * 
	 * @param groupPosition The position of this event in the list of events
	 */
	private void createDetailsDialog(int groupPosition) {
		Event event = events.get(groupPosition);
		DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
		final String dialogMessage = "Description:" + event.getDescription()
				+ "\nTime: " + tf.format(event.getDatetime()) + " - " + tf.format(event.getDatetime() + event.getDateTimeTo())
				+ "\nLocation: " + event.getLocation().getName()
				+ "\nInitiator: " + event.getUser().getName()
				+ "\nMin/Max Participants: " + event.getMinparticipants() + "/" + event.getMaxparticipants();
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
