package hr.android.petkovic.igoout;

import hr.android.petkovic.igoout.api.RestApiClient;
import android.app.Application;

public class IGoOutApp extends Application {

	public void onCreate() {
		super.onCreate();
		RestApiClient.get().init(this);
	};
}
