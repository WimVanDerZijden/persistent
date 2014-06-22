package nl.saxion.persistent.view;

import java.util.Arrays;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.controller.Filter;
import nl.saxion.persistent.model.Column;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class FilterActivity extends Activity implements OnItemSelectedListener
{
	private List<Filter> filters;
	private FilterAdapter adapter;
	private String tableName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		tableName = (String) getIntent().getExtras().get("TableName");
		filters = Filter.get(tableName, this);
		ListView filterList = (ListView) findViewById(R.id.filter_list);
		adapter = new FilterAdapter(this, filters);
		filterList.setAdapter(adapter);

		Spinner columnSpinner = (Spinner) findViewById(R.id.column_spinner);
		ArrayAdapter<Column> adapter = new ArrayAdapter<Column>(this, R.layout.location_spinner_item, Column.get(tableName));
		columnSpinner.setAdapter(adapter);
		columnSpinner.setOnItemSelectedListener(this);
	}

	private class FilterAdapter extends ArrayAdapter<Filter> implements OnClickListener
	{
		private static final int RESOURCE_ID = R.layout.filter_item;

		private LayoutInflater inflater;
		private List<Filter> filters;

		public FilterAdapter(Context context, List<Filter> filters)
		{
			super(context, RESOURCE_ID, filters);
			inflater = LayoutInflater.from(context);
			this.filters = filters;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			if (view == null)
				view = inflater.inflate(RESOURCE_ID, null);
			TextView textView = ((TextView) view.findViewById(R.id.filter_text));
			textView.setText(filters.get(position).getDisplayString());

			View deleteButton = view.findViewById(R.id.delete_button);
			deleteButton.setTag(position);
			deleteButton.setOnClickListener(this);
			return view;
		}

		@Override
		public void onClick(View v)
		{
			filters.remove((int) (Integer) v.getTag());
			notifyDataSetChanged();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub
		
	}
}
