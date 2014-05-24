package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.R;
import nl.saxion.persistent.view.MainActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends MainFragment
{

	//private String mName;

	private TextView mNameView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		mNameView = (TextView) rootView.findViewById(R.id.name_textview);
		mNameView.setText(MainActivity.getUser().getName());
		rootView.findViewById(R.id.change_name_button)
				.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						createNameDialog();
					}
				});
		rootView.findViewById(R.id.change_password_button)
				.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						createPasswordDialog();
					}
				});
		return rootView;
	}
	
	private void createPasswordDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		// Creating the AlertDialog from builder
		final View dialogView = inflater.inflate(R.layout.dialog_password, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.change_password)
				.setView(dialogView)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						String current = ((EditText) dialogView.findViewById(R.id.current_password)).getText().toString();
						String new_pass = ((EditText) dialogView.findViewById(R.id.new_password)).getText().toString();
						String repeat = ((EditText) dialogView.findViewById(R.id.repeat_password)).getText().toString();
						if (!MainActivity.getUser().setPassword(current, new_pass, repeat))
							Toast.makeText(getActivity(), R.string.error_password_failed, Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getActivity(), R.string.password_success, Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				})
				.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
	}

	/**
	 * Creates a simple dialog to edit name
	 * 
	 */

	private void createNameDialog()
	{
		// Creating the AlertDialog from builder
		final EditText nameEdit = new EditText(getActivity());
		nameEdit.setText(mNameView.getText());
		nameEdit.setSelectAllOnFocus(true);
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.change_name)
				.setView(nameEdit)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (!MainActivity.getUser().setName(nameEdit.getText().toString()))
							Toast.makeText(getActivity(), R.string.error_invalid_name, Toast.LENGTH_SHORT).show();
						else
							mNameView.setText(MainActivity.getUser().getName());
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				})
				.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
}
