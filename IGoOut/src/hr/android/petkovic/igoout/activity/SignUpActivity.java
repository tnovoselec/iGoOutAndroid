package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.api.RestApiClient;
import hr.android.petkovic.igoout.api.UserListener;
import hr.android.petkovic.igoout.model.User;
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
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordRepeat)) {
			Toast.makeText(this, "Error occurred, please check your data", Toast.LENGTH_SHORT).show();
		} else {
			if (password.equals(passwordRepeat)) {
				userListener = new UserListener() {

					@Override
					public void onSuccess(User user) {
						hideDialog();
						startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
					}

					@Override
					public void onFailure(Throwable t) {
						hideDialog();
						Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();

					}
				};
				RestApiClient.get().registerUser(username, password, userListener);
			}else{
				Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
