package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.Config;
import hr.android.petkovic.igoout.model.Event;
import hr.android.petkovic.igoout.model.Location;
import hr.android.petkovic.igoout.model.User;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

public class RestApiClient {

	private String TAG = this.getClass().getSimpleName();

	private static final String LOCATIONS_PATH = "/locations";
	private static final String EVENTS_PATH = "/events/%s";
	private static final String COMMENTS_PATH = "/comments/%s";
	private static final String USER_REGISTER = "/user/register";
	private static final String USER_LOGIN = "/user/login";

	private RequestQueue reqQueue;
	private static RestApiClient INSTANCE;

	public void init(Context c) {
		reqQueue = Volley.newRequestQueue(c);
	}

	public static RestApiClient get() {
		if (INSTANCE == null) {
			INSTANCE = new RestApiClient();
		}
		return INSTANCE;
	}

	public void getLocations(int[] interest, int[] venues, int radiusId, final LocationsListener locationsListener) {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				locationsListener.onLocationsReady(null);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				locationsListener.onLocationsReady(parseLocations(response));
			}

		};
		try {
			reqQueue.add(new JsonObjectRequest(Method.POST, Config.MAIN_URL + LOCATIONS_PATH, buildLocationJson(interest, venues, radiusId), listener,
					errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			locationsListener.onLocationsReady(null);
		}
	}

	public void getEvents(int locationId, final EventsListener eventsListener) {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				eventsListener.onEventsReady(null);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				eventsListener.onEventsReady(parseEvents(response));
			}

		};
		try {

			reqQueue.add(new JsonObjectRequest(Method.GET, Config.MAIN_URL + String.format(EVENTS_PATH, locationId), null, listener, errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			eventsListener.onEventsReady(null);
		}

	}

	public void registerUser(String username, String password, final UserListener userListener) {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Registration failed", error);
				userListener.onFailure(error);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, "Registration succeded" + response);
				userListener.onSuccess(getUser(response));

			}
		};

		JSONObject userObj = new JSONObject();
		JSONObject requestObj = new JSONObject();

		try {
			userObj.put("password", password);
			userObj.put("username", username);

			requestObj.put("user", userObj);

		} catch (JSONException e) {
			userListener.onFailure(e);
			return;
		}

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		RegistrationRequest jsonReq = new RegistrationRequest(Method.POST, Config.MAIN_URL + USER_REGISTER, requestObj, headers, listener, errorListener);

		reqQueue.add(jsonReq);

	}

	private User getUser(JSONObject json) {

		User user = new User();
		try {
			user.setCommentedEvents(parseIntArray(json.optJSONArray("commentedEvents")));
			user.setId(json.optInt("id"));
			user.setRatedEvents(parseIntArray(json.optJSONArray("ratedEvents")));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}

	private static String basicAuthHeaderValue(String username, String password) {
		String x = username + ":" + password;
		try {
			return "Basic " + Base64.encodeToString(x.getBytes("UTF-8"), Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public void loginUser(String email, String password, final UserListener url) {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				url.onFailure(error);
			}
		};

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", basicAuthHeaderValue(email, password));

		Listener<User> userListener = new Listener<User>() {

			@Override
			public void onResponse(User user) {

				url.onSuccess(user);
			}
		};

		reqQueue.add(new GsonRequest<User>("http://cipele46.org/users/show.json", User.class, headers, userListener, errorListener));
	}

	private JSONObject buildLocationJson(int[] interest, int[] venue, int radiusId) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray interests = new JSONArray();
		for (int i : interest) {
			interests.put(i);
		}
		JSONArray venues = new JSONArray();
		for (int i : venue) {
			venues.put(i);
		}
		json.put("interests", interests);
		json.put("venues", venues);
		json.put("radiusId", radiusId);
		return json;

	}

	private ArrayList<Event> parseEvents(JSONObject jsonEvents) {
		ArrayList<Event> list = new ArrayList<Event>();
		try {
			JSONArray events = jsonEvents.getJSONArray("event");
			for (int i = 0; i < events.length(); i++) {
				JSONObject j = events.getJSONObject(i);
				Event e = new Event();
				e.setAssignedInterest(parseIntArray(j.optJSONArray("assignedInterest")));
				e.setComments(parseIntArray(j.optJSONArray("comments")));
				e.setDetailsUrl(j.optString("detailsUrl"));
				e.setId(j.optInt("id"));
				e.setLocationId(j.optInt("locationId"));
				e.setName(j.optString("name"));
				e.setPictureUrl(j.optString("pictureUrl"));
				e.setRatingAvg((float) j.optDouble("ratingAvg"));
				e.setStartTime(j.optString("startTime"));
				e.setSummary(j.optString("summary"));
				list.add(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	private ArrayList<Location> parseLocations(JSONObject jsonLocations) {
		ArrayList<Location> list = new ArrayList<Location>();
		try {
			JSONArray locations = jsonLocations.getJSONArray("location");
			for (int i = 0; i < locations.length(); i++) {
				JSONObject j = locations.getJSONObject(i);
				Location loc = new Location();
				loc.setAddress(j.optString("address"));
				loc.setId(j.optInt("id"));
				loc.setEvents(parseIntArray(j.optJSONArray("events")));
				loc.setLat(j.optDouble("lat"));
				loc.setLng(j.optDouble("lng"));
				loc.setName(j.optString("name"));
				loc.setPhoneNumber(j.optString("phoneNumber"));
				loc.setPictureUrl(j.optString("pictureUrl"));
				loc.setSummary(j.optString("summary"));
				loc.setType(j.optString("type"));
				loc.setWebsite(j.optString("website"));
				loc.setWorkingHours(j.optString("workingHours"));
				list.add(loc);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	private int[] parseIntArray(JSONArray json) throws JSONException {
		if (json == null) {
			return new int[0];
		}
		int[] array = new int[json.length()];
		for (int i = 0; i < json.length(); i++) {
			array[i] = json.optInt(i);
		}
		return array;
	}
}
