package nl.saxion.persistent.view;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.saxion.persistent.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateEventActivity extends Activity implements
		DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	int updateFieldNr;
	private TextView dateField;
	private TextView timeFromField;
	private TextView timeToField;
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
	 *            which field is clicked
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
			timeToMinutes = hourOfDay * 60 + minute;
			timeFromField.setText("" + hourOfDay + ":" + minute);
		} else
			timeToField.setText("" + hourOfDay + ":" + minute);
			timeFromField.setText("" + hourOfDay + ":" + minute);
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
		
	}

	public void maxNrPressed(View v) {
		
	}

	public void cancelButtonPressed(View v) {
		onBackPressed();
	}

	public void okButtonPressed(View v){
		String name = eventNameField.getText().toString();
		Long datetime = date.getTimeInMillis();
		int duration = timeFromMinutes - timeToMinutes;
		if (!name.equals("") && duration > 0){
			//Event.createEvent(name, datetime, duration, maxparticipants, minparticipants, description);
		}
		onBackPressed();
	}
}
