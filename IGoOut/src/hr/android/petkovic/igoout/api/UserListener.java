package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.model.User;

public interface UserListener {

	void onSuccess(User user);
	
	void onFailure(Throwable t);
}
