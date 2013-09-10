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

import com.actionbarsherlock.app.SherlockActivity;

public class SignUpActivity extends AbstractFragmentActivity implements OnClickListener {

	private Button signUpBtn;
	private EditText usernameEdt;
	private EditText passwordEdt;
	private EditText repeatPasswordEdt;
	private UserListener userListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);

		signUpBtn = (Button) findViewById(R.id.sign_up_btn);
		usernameEdt = (EditText) findViewById(R.id.sign_up_username);
		passwordEdt = (EditText) findViewById(R.id.sign_up_password);
		repeatPasswordEdt = (EditText) findViewById(R.id.sign_up_password_repeat);

		signUpBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == signUpBtn) {
			showDialog();
			trySigningUp();
		}
	}

	private void trySigningUp() {
		String username = usernameEdt.getText() != null ? usernameEdt.getText().toString() : null;
		String password = passwordEdt.getText() != null ? passwordEdt.getText().toString() : null;
		String passwordRepeat = repeatPasswordEdt.getText() != null ? repeatPasswordEdt.getText().toString() : null;
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordRepeat) || username.length() < 4 || password.length() < 4) {
			showErrorMsg(getString(R.string.error_input_too_short));
		} else {
			if (password.equals(passwordRepeat)) {
				userListener = new UserListener() {

					@Override
					public void onSuccess(User user) {
						hideDialog();
						Utils.saveUserSession(user, SignUpActivity.this);
						startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
						finish();
					}

					@Override
					public void onFailure(Throwable t, int status) {
						hideDialog();
						if (status == 551) {
							showErrorMsg(getString(R.string.error_user_exists));
						} else {
							showErrorMsg(getString(R.string.error_server_error));
						}

					}
				};
				RestApiClient.get().registerUser(username, password, userListener);
			} else {
				showErrorMsg(getString(R.string.error_passwords_dont_match));
			}
		}
	}

	private void showErrorMsg(String msg) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(msg);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

}
