package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

public class SignInActivity extends AbstractFragmentActivity implements OnClickListener {

	private Button signUpBtn;
	private Button signInBtn;
	private EditText usernameEdt;
	private EditText passwordEdt;

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
			startActivity(new Intent(this, HomeActivity.class));
		}
	}

}
