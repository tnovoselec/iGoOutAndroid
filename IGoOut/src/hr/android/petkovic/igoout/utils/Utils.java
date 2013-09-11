package hr.android.petkovic.igoout.utils;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

public class Utils {
	private Utils() {
	}

	public static final String EVENT_TIME_FORMAT = "dd.MM.yyyy hh:mm";
	public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

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

	public static long getLastVisit(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(Constants.LAST_VISIT, -1);
	}

	public static String formatEventTime(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(EVENT_TIME_FORMAT);
			SimpleDateFormat sdf2 = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
			Date d = sdf2.parse(time);
			String ret = sdf.format(d);
			return ret;
		} catch (Exception e) {
			return time;
		}
	}

	public static Date formatEventDate(String time) {
		try {
			SimpleDateFormat sdf2 = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
			Date d = sdf2.parse(time);

			return d;
		} catch (Exception e) {
			return new Date();
		}
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
}
