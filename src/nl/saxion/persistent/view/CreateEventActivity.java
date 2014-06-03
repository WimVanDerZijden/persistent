package nl.saxion.persistent.view;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Event;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateEventActivity extends Activity implements
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	int updateFieldNr;
	private TextView dateField;
	private TextView timeFromField;
	private TextView timeToField;
	private EditText maxNrField;
	private EditText minNrField;
	private EditText eventNameField;
	private EditText eventDescriptionField;
	private int timeFromMinutes;
	private int timeToMinutes;
	
	private static Calendar date;

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
		DateFormat df = DateFormat.getDateInstance();
		if (date == null) {
			date = new GregorianCalendar();
		}
		dateField.setText(df.format(date.getTime()));
	}

	/**
	 * Shows the datepicker dialog
	 * 
	 * @param dateNr
	 * which field is clicked
	 */
	public void showDatePicker(int dateNr) {
		updateFieldNr = dateNr;
		DialogFragment d = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Use the current date as the default date in the picker
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);

				// Create a new instance of DatePickerDialog and return it
				return new DatePickerDialog(getActivity(),
						CreateEventActivity.this, year, month, day);
			}
		};
		d.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		date = new GregorianCalendar();
		date.clear();
		date.set(year, monthOfYear, dayOfMonth);
		DateFormat df = DateFormat.getDateInstance();
		dateField.setText(df.format(date.getTime()));
	}

	public void showTimePicker(int timeNr) {
		updateFieldNr = timeNr;
		DialogFragment d = new DialogFragment() {
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Use the current time as the default values for the picker
				final Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);

				// Create a new instance of TimePickerDialog and return it
				return new TimePickerDialog(getActivity(),
						CreateEventActivity.this, hour, minute,
						android.text.format.DateFormat
								.is24HourFormat(getActivity()));
			}
		};
		d.show(getFragmentManager(), "datePicker");
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (updateFieldNr == 0) {
			date.set(Calendar.HOUR_OF_DAY, hourOfDay);
			date.set(Calendar.MINUTE, minute);
			timeFromMinutes = hourOfDay * 60 + minute;
			timeFromField.setText("" + hourOfDay + ":" + minute);
		} else {
			timeToMinutes = hourOfDay * 60 + minute;
			timeToField.setText("" + hourOfDay + ":" + minute);
		}
	}

	public void date1Pressed(View v) {
		showDatePicker(0);
	}

	public void time1FromPressed(View v) {
		showTimePicker(0);
	}

	public void time1ToPressed(View v) {
		showTimePicker(1);
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
		String name = eventNameField.getText().toString();
		Long datetime = date.getTimeInMillis();
		int duration = timeToMinutes - timeFromMinutes;
		String description = eventDescriptionField.getText().toString();
		if(minNrField.getText() != null && minNrField.getText() != null && !name.equals("") && duration > 0){
			int maxparticipants = Integer.parseInt(minNrField.getText().toString());
			int minparticipants = Integer.parseInt(maxNrField.getText().toString());
			if(Event.createEvent(name, datetime, duration, maxparticipants, minparticipants, description))
				Toast.makeText(this, "Event created", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Incorert values, event not created", Toast.LENGTH_LONG).show();
		}
		
		onBackPressed();
	}
}
