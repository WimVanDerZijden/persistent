package nl.saxion.persistent.view;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.controller.Filter;
import nl.saxion.persistent.controller.Filter.Operator;
import nl.saxion.persistent.model.Column;
import nl.saxion.persistent.model.Column.DataType;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class FilterActivity extends Activity
{
	private static Calendar date;
	private static Calendar time;

	private List<Filter> filters;
	private FilterAdapter filterAdapter;
	private String tableName;

	private Spinner columnSpinner;
	private Column[] columns;

	private Spinner operatorSpinner;
	private ArrayAdapter<Operator> operatorAdapter;
	private List<Operator> operators;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		tableName = (String) getIntent().getExtras().get("TableName");
		filters = Filter.get(tableName, this);
		ListView filterList = (ListView) findViewById(R.id.filter_list);
		filterAdapter = new FilterAdapter(this, filters);
		filterList.setAdapter(filterAdapter);

		columns = Column.get(tableName);
		columnSpinner = (Spinner) findViewById(R.id.column_spinner);
		ArrayAdapter<Column> adapter = new ArrayAdapter<Column>(this, R.layout.location_spinner_item, columns);
		columnSpinner.setAdapter(adapter);
		columnSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				// Hide and show the applicable value fields for this data type
				Column column = (Column) parent.getItemAtPosition(position);
				if (column.getDataType() == DataType.TIMESTAMP) {
					findViewById(R.id.set_date_time).setVisibility(View.VISIBLE);
				}
				else {
					findViewById(R.id.set_date_time).setVisibility(View.GONE);
				}
				if (column.getDataType() == DataType.TEXT) {
					findViewById(R.id.set_text).setVisibility(View.VISIBLE);
				}
				else {
					findViewById(R.id.set_text).setVisibility(View.GONE);
				}
				// Load the applicable operators for this column
				operators.clear();
				for (Operator o : column.getOperators())
					operators.add(o);
				operatorAdapter.notifyDataSetChanged();
				operatorSpinner.setSelection(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		operators = new ArrayList<Operator>();
		operatorSpinner = (Spinner) findViewById(R.id.operator_spinner);
		operatorAdapter = new ArrayAdapter<Operator>(this, R.layout.location_spinner_item, operators);
		operatorSpinner.setAdapter(operatorAdapter);
		operatorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

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

		});

		final TextView dateField = (TextView) findViewById(R.id.date_view);
		dateField.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment current_dialog = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						// Create a new instance of DatePickerDialog and return it
						Calendar c = date == null ? Calendar.getInstance() : date;
						return new DatePickerDialog(FilterActivity.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								if (date == null) {
									date = Calendar.getInstance();
									date.clear();
								}
								date.set(year, monthOfYear, dayOfMonth);
								DateFormat df = DateFormat.getDateInstance();
								dateField.setText(df.format(date.getTime()));
							}
						},
								c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
					}
				};
				current_dialog.show(getFragmentManager(), "datePicker");
			}
		});
		if (date != null)
			dateField.setText(DateFormat.getDateInstance().format(date.getTime()));

		final TextView timeField = (TextView) findViewById(R.id.time_view);
		timeField.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment current_dialog = new DialogFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						// Create a new instance of DatePickerDialog and return it
						Calendar c = time == null ? Calendar.getInstance() : time;
						return new TimePickerDialog(FilterActivity.this, new OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int hour, int minutes) {
								if (time == null) {
									time = Calendar.getInstance();
									time.clear();
								}
								time.set(0, 0, 0, hour, minutes);
								DateFormat tf = DateFormat.getTimeInstance();
								timeField.setText(tf.format(time.getTime()));
							}
						},
								c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
								android.text.format.DateFormat.is24HourFormat(getActivity()));
					}
				};
				current_dialog.show(getFragmentManager(), "timePicker");
			}
		});
		if (time != null)
			timeField.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(time.getTime()));

		// Listener for ok button
		((Button) findViewById(R.id.add_button)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Column column = (Column) columnSpinner.getSelectedItem();
				Operator operator = (Operator) operatorSpinner.getSelectedItem();
				boolean valid = true;
				switch (column.getDataType()) {
				case EVENT:
					break;
				case LOCATION:
					break;
				case NUMBER:
					break;
				case TEXT:
					String value = ((EditText) findViewById(R.id.set_text)).getText().toString();
					((EditText) findViewById(R.id.set_text)).setText("");
					Filter filter = new Filter(column, operator, "%" + value + "%");
					filters.add(filter);
					break;
				case TIMESTAMP:
					if (date == null) {
						dateField.setError("Date is required");
						valid = false;
					}
					else {
						Calendar c_value = Calendar.getInstance();
						c_value.clear();
						c_value.set(date.get(Calendar.YEAR),
								date.get(Calendar.MONTH),
								date.get(Calendar.DAY_OF_MONTH),
								time == null ? 0 : time.get(Calendar.HOUR_OF_DAY),
								time == null ? 0 : time.get(Calendar.MINUTE));
						filters.add(new Filter(column, operator, c_value.getTimeInMillis()));
						date = null;
						time = null;
						dateField.setText("");
						timeField.setText("");
					}
					break;
				case USER:
					break;
				default:
					break;
				}
				// Save filters to local preferences
				if (valid) {
					Filter.save(tableName, FilterActivity.this, filters);
					filterAdapter.notifyDataSetChanged();
				}
			}

		});

	}

	@Override
	public void onBackPressed()
	{
		// TODO reset static values
		date = null;
		time = null;
		super.onBackPressed();
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
			Filter.save(tableName, FilterActivity.this, filters);
			notifyDataSetChanged();
		}
	}
}
