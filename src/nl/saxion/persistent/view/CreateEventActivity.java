package nl.saxion.persistent.view;

import java.util.Calendar;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.Event;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.CalendarContract.EventDays;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateEventActivity extends Activity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

	int updateFieldNr;
	TextView[] dateFields = new TextView[1];
	TextView[] timeFields = new TextView[2];
	EditText eventNameField;
	EditText eventDescriptionField;
	CreateEventActivity thisClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		thisClass = this;
		initViews();
	}
	
	/**
	 * Sets the values to dateFields and timeFields
	 */
	public void initViews(){
		dateFields[0] = (TextView) findViewById(R.id.date1_view);
		timeFields[0] = (TextView) findViewById(R.id.time1_from_view);
		timeFields[1] = (TextView) findViewById(R.id.time1_to_view);
		eventNameField = (EditText) findViewById(R.id.event_name_field);
		eventDescriptionField = (EditText) findViewById(R.id.event_description_field);
	}
	/**
	 * Shows the datepicker dialog
	 * @param dateNr which field is clicked
	 */
	public void showDatePicker(int dateNr){
		updateFieldNr = dateNr;
		DialogFragment d = new DialogFragment(){
			@Override
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        // Use the current date as the default date in the picker
		        final Calendar c = Calendar.getInstance();
		        int year = c.get(Calendar.YEAR);
		        int month = c.get(Calendar.MONTH);
		        int day = c.get(Calendar.DAY_OF_MONTH);

		        // Create a new instance of DatePickerDialog and return it
		        return new DatePickerDialog(getActivity(), thisClass, year, month, day);
		    }
		};
		d.show(getFragmentManager(), "datePicker");
	}
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		dateFields[updateFieldNr].setText("" + dayOfMonth + " / " + monthOfYear + " / " + year);
	}
	
	public void showTimePicker(int timeNr){
		updateFieldNr = timeNr;
		DialogFragment d = new DialogFragment(){
			@Override
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        // Use the current time as the default values for the picker
		        final Calendar c = Calendar.getInstance();
		        int hour = c.get(Calendar.HOUR_OF_DAY);
		        int minute = c.get(Calendar.MINUTE);

		        // Create a new instance of TimePickerDialog and return it
		        return new TimePickerDialog(getActivity(), thisClass, hour, minute,
		                DateFormat.is24HourFormat(getActivity()));
		    }
		};
		d.show(getFragmentManager(), "datePicker");
	}
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		timeFields[updateFieldNr].setText("" + hourOfDay + ":" + minute);
	}
	
	public void date1Pressed(View v){
		showDatePicker(0);
	}
	public void time1FromPressed(View v){
		showTimePicker(0);
	}
	public void time1ToPressed(View v){
		showTimePicker(1);
	}
	public void minNrPressed(View v){
		
	}
	public void maxNrPressed(View v){
		
	}
	
	public void cancelButtonPressed(View v){
		onBackPressed();
	}
	public void okButtonPressed(View v){
		String name = eventNameField.getText().toString();
		Long datetime;
		//Event.createEvent(name, datetime, duration, maxparticipants, minparticipants, description);
		onBackPressed();
	}
	
}
