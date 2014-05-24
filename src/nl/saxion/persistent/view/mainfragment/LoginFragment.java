package nl.saxion.persistent.view.mainfragment;

import nl.saxion.persistent.R;
import nl.saxion.persistent.view.MainActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends MainFragment
{
	/** Minimum length of a password */
	public static final int PASSWORD_MIN_LENGTH = 1;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);

		// Set up the login form.
		mEmailView = (EditText) rootView.findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) rootView.findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent)
			{
				if (id == R.id.login || id == EditorInfo.IME_NULL)
				{
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		rootView.findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						((MainActivity) getActivity()).hideKeyBoard();
						attemptLogin();
					}
				});
		return rootView;
	}

	public void attemptLogin()
	{
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword))
		{
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		else if (mPassword.length() < PASSWORD_MIN_LENGTH)
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail))
		{
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		else if (!mEmail.contains("@"))
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel)
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		// Attempt login
		else if (!((MainActivity) getActivity()).login(mEmail, mPassword))
		{
			mPasswordView.setError(getString(R.string.error_incorrect_password));
			mPasswordView.requestFocus();
		}
	}

}
