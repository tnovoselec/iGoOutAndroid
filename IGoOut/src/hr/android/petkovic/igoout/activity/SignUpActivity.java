package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

public class SignUpActivity extends AbstractFragmentActivity implements OnClickListener {

	private Button signUpBtn;
	private EditText usernameEdt;
	private EditText passwordEdt;
	private EditText repeatPasswordEdt;

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
			startActivity(new Intent(this, HomeActivity.class));
		}
	}

}
