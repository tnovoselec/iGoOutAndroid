package hr.android.petkovic.igoout.utils;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.model.User;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Utils {
	private Utils() {
	}

	public static void saveUserSession(User user, Context context) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString(Constants.USERNAME, user.getUsername());
		editor.putString(Constants.PASSWORD, user.getPassword());
		editor.putInt(Constants.USER_ID, user.getId());
		editor.putLong(Constants.LAST_VISIT, System.currentTimeMillis());
		editor.commit();
	}

	public static User getUser(Context context) {
		User user = new User();
		String username = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USERNAME, null);
		String password = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.PASSWORD, null);
		int userId = PreferenceManager.getDefaultSharedPreferences(context).getInt(Constants.USER_ID, -1);
		if (username == null || password == null || userId == -1) {
			return null;
		}
		user.setId(userId);
		user.setPassword(password);
		user.setUsername(username);
		return user;
	}

	public static void updateLastVisit(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(Constants.LAST_VISIT, System.currentTimeMillis()).commit();
	}
	public static long getLastVisit(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(Constants.LAST_VISIT, -1);
	}
}
