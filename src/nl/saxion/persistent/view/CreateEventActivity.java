package nl.saxion.persistent.view;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Event;
import nl.saxion.persistent.model.Location;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Class to create Event
 * 
 * TODO TimeFrom/TimeTo is only saved if Date is filled in
 * Fix this by disabling/hiding Time fields when date is not filled in 
 * 
 * @author EINv1u2
 *
 */
public class CreateEventActivity extends Activity implements
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
		OnItemSelectedListener {

	private TextView dateField;
	private TextView timeFromField;
	private TextView timeToField;
	private EditText maxNrField;
	private EditText minNrField;
	private EditText eventNameField;
	private EditText eventDescriptionField;
	private DialogFragment current_dialog;
	
	private Spinner locationSpinner;
	private ArrayAdapter<Location> locationAdapter;
	private static List<Location> locations;
	private static Location location;
	
	private static Calendar date;
	private static Calendar timeFrom;
	private static Calendar timeTo;
	
	
	private DialogType dialogType;
	
	private enum DialogType { Date, TimeTo, TimeFrom };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
	}

	/**
	 * Sets the values to dateFields and timeFields
	 */
	@Override
	public void onStart() {
		super.onStart();
		dateField = (TextView) findViewById(R.id.date1_view);
		timeFromField = (TextView) findViewById(R.id.time1_from_view);
		timeToField = (TextView) findViewById(R.id.time1_to_view);
		maxNrField = (EditText) findViewById(R.id.max_nr_field);
		minNrField =(EditText) findViewById(R.id.min_nr_field);
		eventNameField = (EditText) findViewById(R.id.event_name_field);
		eventDescriptionField = (EditText) findViewById(R.id.event_description_field);
		locationSpinner = (Spinner) findViewById(R.id.location_spinner);
		if (locations == null)
			locations = new ArrayList<Location>();
		
		locationAdapter = new ArrayAdapter<Location>(this, R.layout.spinner_item, locations);
		locationSpinner.setAdapter(locationAdapter);
		locationSpinner.setOnItemSelectedListener(this);
		updateLocationSpinner();
		
		DateFormat df = DateFormat.getDateInstance();
		DateFormat tf = DateFormat.getTimeInstance();
		if (date != null)
			dateField.setText(df.format(date.getTime()));
		if (timeFrom != null)
			timeFromField.setText(tf.format(timeFrom.getTime()));
		if (timeTo != null)
			timeToField.setText(tf.format(timeTo.getTime()));
		if (location != null)
			; //locationSpinner.set;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// Avoid trying to recreate the open dialog on screen rotation,
		// which will fail and cause a crash.
		if (current_dialog != null)
			current_dialog.dismiss();
	}
	
	@Override
	public void onBackPressed() {
		reset();
		super.onBackPressed();
	}

	/**
	 * This loads the location spinner with available locations for the selected
	 * date, time from and time to, or hides it when one of these are not known.
	 * 
	 */
	private void updateLocationSpinner() {
		if (date == null || timeFrom == null || timeTo == null) {
			locationSpinner.setVisibility(View.GONE);
		}
		else {
			locationSpinner.setVisibility(View.VISIBLE);
			List<Location> locs = Location.getAvailable(getDateTime(), getDateTimeTo());
			locations.clear();
			for (Location loc : locs) {
				locations.add(loc);
			}
			locationAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * Shows the datepicker dialog
	 * 
	 * @param dateNr
	 * which field is clicked
	 */
	public void showDatePicker(final DialogType dialogType) {
		this.dialogType = dialogType;
		current_dialog = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Create a new instance of DatePickerDialog and return it
				// TODO impl. diff. dates
				Calendar c = date == null ? Calendar.getInstance() : date;
				return new DatePickerDialog(getActivity(), CreateEventActivity.this,
						c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			}
		};
		current_dialog.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		if (date == null) {
			date = Calendar.getInstance();
			date.clear();
		}
		date.set(year, monthOfYear, dayOfMonth);
		DateFormat df = DateFormat.getDateInstance();
		dateField.setText(df.format(date.getTime()));
		updateLocationSpinner();
	}

	public void showTimePicker(final DialogType dialogType) {
		this.dialogType = dialogType;
		current_dialog = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				
				Calendar c = null;
				if (dialogType == DialogType.TimeFrom)
					c = timeFrom == null ? Calendar.getInstance() : timeFrom;
				else if (dialogType == DialogType.TimeTo)
					c = timeTo == null ? Calendar.getInstance() : timeTo;
					
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);

				// Create a new instance of TimePickerDialog and return it
				return new TimePickerDialog(getActivity(), CreateEventActivity.this, hour, minute,
						android.text.format.DateFormat.is24HourFormat(getActivity()));
			}
		};
		current_dialog.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar updatedTime = null;
		TextView timeField = null; 
		if (dialogType == DialogType.TimeFrom) {
			if (timeFrom == null) {
				timeFrom = Calendar.getInstance();
				timeFrom.clear();
			}
			updatedTime = timeFrom;
			timeField = timeFromField;
		} else if (dialogType == DialogType.TimeTo) {
			if (timeTo == null) {
				timeTo = Calendar.getInstance();
				timeTo.clear();
			}
			updatedTime = timeTo;
			timeField = timeToField;
		} else
			throw new IllegalStateException("Unknown TimePicker DialogType: " + dialogType);

		updatedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
		updatedTime.set(Calendar.MINUTE, minute);
		DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
		timeField.setText(tf.format(updatedTime.getTime()));
		updateLocationSpinner();
	}

	public void date1Pressed(View v) {
		showDatePicker(DialogType.Date);
	}

	public void time1FromPressed(View v) {
		showTimePicker(DialogType.TimeFrom);
	}

	public void time1ToPressed(View v) {
		showTimePicker(DialogType.TimeTo);
	}

	public void minNrPressed(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create();
			builder.show();
	}

	public void maxNrPressed(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).create();
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).create();
		builder.show();
	}

	public void cancelButtonPressed(View v) {
		onBackPressed();
	}

	public void okButtonPressed(View v){
		boolean valid = true;
		View requestfocus = null;
		
		String name = eventNameField.getText().toString();
		if (name.length() == 0) {
			eventNameField.setError("Name is required");
			requestfocus = eventNameField;
			valid = false;
		}
		if (date == null) {
			valid = false;
			dateField.setError("Date is required");
			requestfocus = dateField;
		}
		if (timeFrom == null) 			{
			timeFromField.setError("Time From is required");
			requestfocus = timeFromField;
			valid = false;		
		}
		if (timeTo == null) {
			valid = false;
			timeToField.setError("Time To is required");
			requestfocus = timeToField;
		}
		if (date != null && timeTo != null && timeFrom != null && getDateTimeTo() <= getDateTime()) {
			timeToField.setError("Time To must be higher than Time From");
			requestfocus = timeToField;
			valid = false;
		}
		String description = eventDescriptionField.getText().toString();
		Integer minParticipants = 0;
		Integer maxParticipants = null;
		try {
			maxParticipants = Integer.parseInt(maxNrField.getText().toString());
			minParticipants = Integer.parseInt(minNrField.getText().toString());
		}
		catch (NumberFormatException ignore) {}
		if (maxParticipants == null) {
			maxNrField.setError("Max participants is required");
			valid = false;
			requestfocus = maxNrField;
		}
		if (maxParticipants != null && maxParticipants < minParticipants) {
			maxNrField.setError("Max Participants must be higher than or equal to Min Participants");
			requestfocus = maxNrField;
			valid = false;
		}
		if (location == null) {
			valid = false;
		}
		
		if(valid){
			if(Event.createEvent(MainActivity.getUser(), name, getDateTime(), getDateTimeTo(), maxParticipants, minParticipants, description, location))
				Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		else
			requestfocus.requestFocus();
	}
	
	/**
	 * Get datetime as long. Only call when both date and timeFrom are not null.
	 * This modifies date by adding the time.
	 * 
	 * @return
	 */
	private Long getDateTime() {
		date.set(Calendar.HOUR_OF_DAY, timeFrom.get(Calendar.HOUR_OF_DAY));
		date.set(Calendar.MINUTE, timeFrom.get(Calendar.MINUTE));
		return date.getTimeInMillis();
	}
	
	/**
	 * Get timeTo as long. Only call when both timeTo and date
	 * are not null.
	 * 
	 * This modifies timeTo by adding the date.
	 * 
	 * @return
	 */
	
	private Long getDateTimeTo() {
		timeTo.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));;
		return timeTo.getTimeInMillis();
	}
	
	/**
	 * This resets the static variables.
	 * 
	 */
	private void reset()
	{
		date = null;
		timeTo = null;
		timeFrom = null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		location = locations.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		location = null;
		
	}
}
