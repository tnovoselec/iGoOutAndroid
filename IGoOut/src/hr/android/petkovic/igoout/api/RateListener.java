package hr.android.petkovic.igoout.api;

public interface RateListener {
	void onSuccess();

	void onFailure(int code);
}
