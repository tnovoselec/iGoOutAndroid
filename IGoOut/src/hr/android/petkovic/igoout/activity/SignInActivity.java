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

public class SignInActivity extends AbstractFragmentActivity implements OnClickListener {

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
			userListener = new UserListener() {

				@Override
				public void onSuccess(User user) {
					hideDialog();
					startActivity(new Intent(SignInActivity.this, HomeActivity.class));
				}

				@Override
				public void onFailure(Throwable t) {
					hideDialog();
					Toast.makeText(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();

				}
			};
			RestApiClient.get().loginUser(username, password, userListener);
		}
	}

}
