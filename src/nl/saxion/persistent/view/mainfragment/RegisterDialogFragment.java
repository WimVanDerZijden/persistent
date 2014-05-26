package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class RegisterDialogFragment extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.register_fragment, null)).setTitle("Register")
	    // Add action buttons
	           .setPositiveButton("positive", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	               }
	           })
	           .setNegativeButton("Cancel" , new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   RegisterDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
}
