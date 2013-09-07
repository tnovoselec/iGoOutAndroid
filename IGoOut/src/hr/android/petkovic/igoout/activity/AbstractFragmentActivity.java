package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.fragment.ProgressDialogFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public abstract class AbstractFragmentActivity extends SherlockFragmentActivity {

	@Override
	protected void onPause() {
		super.onPause();
		hideDialog();
	}

	protected void showDialog() {
		ProgressDialogFragment pdf = new ProgressDialogFragment();
		pdf.show(getSupportFragmentManager(), "pdf");
	}

	protected void hideDialog() {
		ProgressDialogFragment pdf = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag("pdf");
		if (pdf != null) {
			pdf.dismiss();
		}
	}
}
