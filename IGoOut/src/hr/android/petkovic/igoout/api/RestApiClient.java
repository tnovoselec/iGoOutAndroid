package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.Config;
import hr.android.petkovic.igoout.model.Comment;
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
	private static final String RATINGS_PATH = "/ratings";
	private static final String COMMENT_PATH = "/comments";

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

	public void getLocations(int[] interest, int[] venues, int radiusId, double latitude, double longitude, final LocationsListener locationsListener) {

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
			reqQueue.add(new DefaultRequest(Method.POST, Config.MAIN_URL + LOCATIONS_PATH, buildLocationJson(interest, venues, radiusId, latitude, longitude),
					listener, errorListener));
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

			reqQueue.add(new DefaultRequest(Method.GET, Config.MAIN_URL + String.format(EVENTS_PATH, locationId), null, listener, errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			eventsListener.onEventsReady(null);
		}

	}

	public void rateEvent(int eventId, int userId, String username, int rating, final RateListener rateListener) {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int code = error.networkResponse != null ? error.networkResponse.statusCode : 500;
				rateListener.onFailure(code);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				rateListener.onSuccess();
			}

		};
		try {
			JSONObject j = buildRatingJson(eventId, userId, username, rating);

			reqQueue.add(new DefaultRequest(Method.POST, Config.MAIN_URL + RATINGS_PATH, j, listener, errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			rateListener.onFailure(500);
		}
	}

	private JSONObject buildRatingJson(int eventId, int userId, String username, int rating) throws JSONException {
		JSONObject j = new JSONObject();
		j.put("event_id", eventId);
		j.put("user_id", userId);
		j.put("username", username);
		j.put("rating", rating);
		return j;
	}

	public void commentEvent(int eventId, int userId, String username, String comment, final CommentListener commentListener) {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int code = error.networkResponse != null ? error.networkResponse.statusCode : 500;
				commentListener.onFailure(code);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				commentListener.onSuccess();
			}

		};
		try {
			JSONObject j = buildCommentJson(eventId, userId, username, comment);

			reqQueue.add(new DefaultRequest(Method.POST, Config.MAIN_URL + COMMENT_PATH, j, listener, errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			commentListener.onFailure(500);
		}
	}

	private JSONObject buildCommentJson(int eventId, int userId, String username, String comment) throws JSONException {
		JSONObject j = new JSONObject();
		j.put("event_id", eventId);
		j.put("user_id", userId);
		j.put("username", username);
		j.put("comment", comment);
		return j;
	}

	public void getComments(int eventId, final CommentsListener commentsListener) {
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				commentsListener.onCommentsReady(null);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				commentsListener.onCommentsReady(parseComments(response));
			}

		};
		try {

			reqQueue.add(new DefaultRequest(Method.GET, Config.MAIN_URL + String.format(COMMENTS_PATH, eventId), null, listener, errorListener));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			commentsListener.onCommentsReady(null);
		}
	}

	public void registerUser(String username, String password, final UserListener userListener) {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Registration failed", error);
				int code = error.networkResponse != null ? error.networkResponse.statusCode : 500;
				userListener.onFailure(error, code);
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

		try {
			userObj.put("password", password);
			userObj.put("username", username);

		} catch (JSONException e) {
			userListener.onFailure(e, 500);
			return;
		}

		reqQueue.add(new DefaultRequest(Method.POST, Config.MAIN_URL + USER_REGISTER, userObj, listener, errorListener));

	}

	private User getUser(JSONObject json) {

		User user = new User();
		try {
			user.setPassword(json.optString("password"));
			user.setUsername(json.optString("username"));
			user.setCommentedEvents(parseIntArray(json.optJSONArray("commentedEvents")));
			user.setId(json.optInt("id"));
			user.setRatedEvents(parseIntArray(json.optJSONArray("ratedEvents")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public void loginUser(String username, String password, final UserListener url) {

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Login failed", error);
				// Log.e(TAG, new String(erroerror.networkResponse.data));
				int code = error.networkResponse != null ? error.networkResponse.statusCode : 500;
				url.onFailure(error, code);
			}
		};

		Listener<JSONObject> listener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				Log.d(TAG, "Login succeded" + response);
				url.onSuccess(getUser(response));

			}
		};

		JSONObject userObj = new JSONObject();

		try {
			userObj.put("password", password);
			userObj.put("username", username);

		} catch (JSONException e) {
			url.onFailure(e, 500);
			return;
		}

		reqQueue.add(new DefaultRequest(Method.POST, Config.MAIN_URL + USER_LOGIN, userObj, listener, errorListener));
	}

	private JSONObject buildLocationJson(int[] interest, int[] venue, int radiusId, double latitude, double longitude) throws JSONException {
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
		json.put("lat", latitude);
		json.put("lng", longitude);
		return json;

	}

	private ArrayList<Comment> parseComments(JSONObject jsonComments) {
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			JSONArray comments = jsonComments.optJSONArray("comment");
			if (comments == null) {
				list.add(getCommentFromJSON(jsonComments.optJSONObject("comment")));
				return list;
			}
			for (int i = 0; i < comments.length(); i++) {
				JSONObject j = comments.getJSONObject(i);

				list.add(getCommentFromJSON(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private Comment getCommentFromJSON(JSONObject j) {
		Comment comment = new Comment();
		comment.setComment(j.optString("comment"));
		comment.setDate(j.optString("date"));
		comment.setEventId(j.optInt("eventId"));
		comment.setId(j.optInt("id"));
		comment.setUserId(j.optInt("userId"));
		comment.setUsername(j.optString("username"));
		return comment;
	}

	private ArrayList<Event> parseEvents(JSONObject jsonEvents) {
		ArrayList<Event> list = new ArrayList<Event>();
		try {
			JSONArray events = jsonEvents.optJSONArray("event");
			if (events == null) {
				list.add(getEventFromJSON(jsonEvents.optJSONObject("event")));
				return list;
			}
			for (int i = 0; i < events.length(); i++) {
				JSONObject j = events.getJSONObject(i);

				list.add(getEventFromJSON(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	private Event getEventFromJSON(JSONObject j) throws JSONException {
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
		return e;
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
