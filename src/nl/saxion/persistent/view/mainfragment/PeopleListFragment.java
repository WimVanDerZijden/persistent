package nl.saxion.persistent.view.mainfragment;

import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.User;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class PeopleListFragment extends MainFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_people_list, container, false);
		ListView peopleList = (ListView) rootView.findViewById(R.id.people_list);
		List<User> users = User.getAll(); 
		peopleList.setAdapter(new ArrayAdapter<User>(this.getActivity(), android.R.layout.simple_list_item_1, users));
		return rootView;
	}

}
