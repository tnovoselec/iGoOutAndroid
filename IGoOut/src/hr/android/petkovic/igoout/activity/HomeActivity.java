package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.api.LocationsListener;
import hr.android.petkovic.igoout.api.RestApiClient;
import hr.android.petkovic.igoout.model.Location;
import hr.android.petkovic.igoout.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AbstractFragmentActivity implements OnClickListener {

	private Button interestsBtn;
	private Button venuesBtn;
	private Button radiusBtn;
	private Button searchBtn;

	private boolean[] mSelectedInterests;
	private boolean[] mSelectedVenues;
	private int mSelectedRadius = 0;

	private LocationsListener locationsListener;
	private ArrayList<Location> locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		interestsBtn = (Button) findViewById(R.id.home_interests);
		venuesBtn = (Button) findViewById(R.id.home_venues);
		radiusBtn = (Button) findViewById(R.id.home_radius);
		searchBtn = (Button) findViewById(R.id.home_search);

		interestsBtn.setOnClickListener(this);
		venuesBtn.setOnClickListener(this);
		radiusBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);

		mSelectedInterests = getSelectedInterests();
		mSelectedVenues = getSelectedVenues();
		mSelectedRadius = getSelectedRadius();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		locationsListener = null;
	}

	@Override
	public void onClick(View v) {
		if (v == interestsBtn) {
			showInterestsDialog();
		} else if (v == venuesBtn) {
			showVenuesDialog();
		} else if (v == radiusBtn) {
			showRadiusDialog();
		} else if (v == searchBtn) {
			search();
		}

	}

	private void search() {
		if (!Utils.isOnline(this)) {
			Toast.makeText(this, R.string.error_no_internet_connection, Toast.LENGTH_LONG).show();
			return;
		}
		// onSearchResults(MockData.getLocations());
		locationsListener = new LocationsListener() {

			@Override
			public void onLocationsReady(ArrayList<Location> locations) {
				hideDialog();
				HomeActivity.this.locations = locations;
				onSearchResults(locations);
			}
		};
		showDialog();
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		android.location.Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = loc != null ? loc.getLatitude() : Constants.DEFAULT_LAT;
		double lng = loc != null ? loc.getLongitude() : Constants.DEFAULT_LNG;
		RestApiClient.get().getLocations(getInterests(), getVenues(), mSelectedRadius, lat, lng, locationsListener);
	}

	private void onSearchResults(ArrayList<Location> locations) {
		if (locations != null && locations.size() > 0) {
			Intent intent = new Intent(this, SearchResultsActivity.class);
			intent.putExtra(Constants.LOCATIONS, locations);
			startActivity(intent);
		} else {
			showErrorMsg();
		}
	}

	private void showInterestsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_interests);
		builder.setMultiChoiceItems(R.array.interests, mSelectedInterests, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				mSelectedInterests[which] = isChecked;
				if (which == 0) {
					if (isChecked) {
						setAllChecked(mSelectedInterests);
					} else {
						// setAllUnChecked(mSelectedInterests);
					}
					for (int i = 0; i < mSelectedInterests.length; i++) {
						((AlertDialog) dialog).getListView().setItemChecked(which, isChecked);
					}

				}
			}
		});
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveSelectedInterests();
			}
		});
		builder.create().show();
	}

	private void showVenuesDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_venues);
		builder.setMultiChoiceItems(R.array.venues, mSelectedVenues, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				mSelectedVenues[which] = isChecked;
				if (which == 0) {
					if (isChecked) {
						setAllChecked(mSelectedVenues);
					} else {
						// setAllUnChecked(mSelectedVenues);
					}
					for (int i = 0; i < mSelectedInterests.length; i++) {
						((AlertDialog) dialog).getListView().setItemChecked(which, isChecked);
					}
				}
			}
		});
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveSelectedVenues();
			}
		});
		builder.create().show();
	}

	private void showRadiusDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_radius);
		builder.setSingleChoiceItems(R.array.radius, mSelectedRadius, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mSelectedRadius = which;

			}
		});
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveSelectedRadius();
			}
		});

		builder.create().show();
	}

	private boolean[] getSelectedInterests() {
		boolean[] interests = new boolean[getResources().getStringArray(R.array.interests).length];
		String savedInterests = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.SELECTED_INTERESTS, null);
		if (savedInterests == null) {
			return interests;
		} else {

			for (int i = 0; i < savedInterests.length(); i++) {
				if (savedInterests.charAt(i) == '1') {
					interests[i] = true;
				}
			}
			return interests;
		}
	}

	private void saveSelectedInterests() {
		StringBuilder sb = new StringBuilder();
		for (boolean b : mSelectedInterests) {
			if (b) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.SELECTED_INTERESTS, sb.toString()).commit();
	}

	private boolean[] getSelectedVenues() {
		boolean[] venues = new boolean[getResources().getStringArray(R.array.interests).length];
		String savedVenues = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.SELECTED_INTERESTS, null);
		if (savedVenues == null) {
			return venues;
		} else {

			for (int i = 0; i < savedVenues.length(); i++) {
				if (savedVenues.charAt(i) == '1') {
					venues[i] = true;
				}
			}
			return venues;
		}
	}

	private void saveSelectedVenues() {
		StringBuilder sb = new StringBuilder();
		for (boolean b : mSelectedVenues) {
			if (b) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.SELECTED_VENUES, sb.toString()).commit();
	}

	private int[] getInterests() {
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < mSelectedInterests.length; i++) {
			if (mSelectedInterests[i]) {
				l.add(i);
			}
		}
		int[] interests = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			interests[i] = l.get(i);
		}
		return interests;
	}

	private int[] getVenues() {
		List<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < mSelectedVenues.length; i++) {
			if (mSelectedVenues[i]) {
				l.add(i);
			}
		}
		int[] venues = new int[l.size()];
		for (int i = 0; i < l.size(); i++) {
			venues[i] = l.get(i);
		}
		return venues;
	}

	private int getSelectedRadius() {
		return PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.SELECTED_RADIUS, 0);
	}

	private void saveSelectedRadius() {
		PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constants.SELECTED_RADIUS, mSelectedRadius).commit();
	}

	private void setAllChecked(boolean[] what) {
		for (int i = 0; i < what.length; i++) {
			what[i] = true;
		}
	}

	private void setAllUnChecked(boolean[] what) {
		for (int i = 0; i < what.length; i++) {
			what[i] = false;
		}
	}

	private void showErrorMsg() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(R.string.error_no_locations_found);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

}
