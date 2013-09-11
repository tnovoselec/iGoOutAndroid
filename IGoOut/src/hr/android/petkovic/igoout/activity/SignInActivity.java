package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.api.RestApiClient;
import hr.android.petkovic.igoout.api.UserListener;
import hr.android.petkovic.igoout.model.User;
import hr.android.petkovic.igoout.utils.Utils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AbstractFragmentActivity implements OnClickListener {

	private static long ONE_MONTH_IN_MILIS = 30 * 24 * 60 * 60 * 1000L;

	private Button signUpBtn;
	private Button signInBtn;
	private EditText usernameEdt;
	private EditText passwordEdt;
	private UserListener userListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		signInBtn = (Button) findViewById(R.id.sign_in_btn);
		signUpBtn = (Button) findViewById(R.id.sign_in_sign_up);
		usernameEdt = (EditText) findViewById(R.id.sign_in_username);
		passwordEdt = (EditText) findViewById(R.id.sign_in_password);

		signUpBtn.setOnClickListener(this);
		signInBtn.setOnClickListener(this);

		User user = Utils.getUser(this);
		if (user != null) {
			long lastVisit = Utils.getLastVisit(this);
			if (System.currentTimeMillis() - lastVisit < ONE_MONTH_IN_MILIS) {
				Utils.updateLastVisit(this);
				startActivity(new Intent(SignInActivity.this, HomeActivity.class));
				finish();
			}
		}
		getSupportActionBar().setTitle(R.string.sign_in);
	}

	@Override
	public void onClick(View v) {
		if (v == signUpBtn) {
			startActivity(new Intent(this, SignUpActivity.class));
		} else if (v == signInBtn) {
			showDialog();
			trySigningIn();
		}
	}

	private void trySigningIn() {
		String username = usernameEdt.getText() != null ? usernameEdt.getText().toString() : null;
		String password = passwordEdt.getText() != null ? passwordEdt.getText().toString() : null;
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "Error occurred, please check your data", Toast.LENGTH_SHORT);
		} else {
			if (!Utils.isOnline(this)) {
				Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show();
				return;
			}
			userListener = new UserListener() {

				@Override
				public void onSuccess(User user) {
					hideDialog();
					Utils.saveUserSession(user, SignInActivity.this);
					startActivity(new Intent(SignInActivity.this, HomeActivity.class));
				}

				@Override
				public void onFailure(Throwable t, int status) {
					hideDialog();
					showErrorMsg();
				}
			};
			RestApiClient.get().loginUser(username, password, userListener);
		}
	}

	private void showErrorMsg() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(R.string.error_sign_in);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
}
