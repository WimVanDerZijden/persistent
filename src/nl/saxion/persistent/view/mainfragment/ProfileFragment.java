package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.R;
import nl.saxion.persistent.view.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends MainFragment
{

	private TextView mNameView;
	private ImageButton mImage;

	/** Requestcode for the image picker */
	private static final int IMAGE_PICKER_SELECT = 999;

	private static final double IMAGE_MAX_SIZE = 300.;
	
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
		mImage = (ImageButton) rootView.findViewById(R.id.imageButton1);
		Bitmap photo = MainActivity.getUser().getPhoto();
		if (photo != null)
			mImage.setImageBitmap(photo);
		mImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, IMAGE_PICKER_SELECT);
			}
		});
		return rootView;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == IMAGE_PICKER_SELECT && resultCode == Activity.RESULT_OK)
		{
			Bitmap bitmap = getBitmapFromCameraData(data, getActivity());
			// Crop image if width or height higher than IMAGE_MAX_SIZE
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int max = width > height ? width : height;
			if (max > IMAGE_MAX_SIZE)
			{
				double factor = IMAGE_MAX_SIZE / max;
				bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * factor), (int) (height * factor), false);
			}
			if (MainActivity.getUser().setPhoto(bitmap))
				mImage.setImageBitmap(bitmap);
			else
				Toast.makeText(getActivity(), R.string.error_save_image, Toast.LENGTH_SHORT).show();			
		}
	}

	/**
	 * Magically transforms a Uri into a Bitmap
	 * 
	 * @param intent
	 * @param context
	 * @return
	 */
	
	public static Bitmap getBitmapFromCameraData(Intent intent, Context context)
	{
		Uri selectedImage = intent.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return BitmapFactory.decodeFile(picturePath);
	}

	private void createPasswordDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View dialogView = inflater.inflate(R.layout.dialog_password, null);
		// Creating the AlertDialog from builder
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
