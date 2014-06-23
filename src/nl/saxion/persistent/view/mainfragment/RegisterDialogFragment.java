package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.R;
import nl.saxion.persistent.model.User;
import nl.saxion.persistent.view.MainActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterDialogFragment extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = inflater.inflate(R.layout.register_fragment, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view)
	    		.setTitle("Register")
	    // Add action buttons
	           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   String email = ((EditText) view.findViewById(R.id.register_email)).getText().toString();
	            	   String password = ((EditText) view.findViewById(R.id.register_password)).getText().toString();
	            	   String name = ((EditText) view.findViewById(R.id.register_name)).getText().toString();
	            	   User user = User.register(name, email, password);
	            	   if (user != null)
	            	   {
	            		   ((MainActivity) getActivity()).login(email, password);
	            		   Toast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_SHORT).show();
	            	   }
	            	   else
	              		   Toast.makeText(getActivity(), R.string.register_failed, Toast.LENGTH_SHORT).show();
	               }
	           })
	           .setNegativeButton(android.R.string.cancel , new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.cancel();
	               }
	           });
	    return builder.create();
	}
}
