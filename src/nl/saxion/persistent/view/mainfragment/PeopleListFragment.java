package nl.saxion.persistent.view.mainfragment;

import java.util.ArrayList;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Table.TableName;
import nl.saxion.persistent.model.Filter;
import nl.saxion.persistent.model.User;
import nl.saxion.persistent.view.UserAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class PeopleListFragment extends MainFragment {
	
	private List<User> users;
	private UserAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_people_list, container, false);
		ListView peopleList = (ListView) rootView.findViewById(R.id.people_list);
		users = new ArrayList<User>();
		adapter = new UserAdapter(this.getActivity(), users);
		peopleList.setAdapter(adapter);
		return rootView;
	}
	
	@Override
	public void onStart() {
		List<Filter> filters = Filter.get(TableName.USER, getActivity());
		List<User> new_users = User.get(filters);
		users.clear();
		for (User u : new_users) {
			users.add(u);
		}
		adapter.notifyDataSetChanged();
		super.onStart();
	}

}
