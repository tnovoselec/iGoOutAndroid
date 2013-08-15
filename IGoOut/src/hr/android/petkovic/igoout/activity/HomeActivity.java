package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.api.LocationsListener;
import hr.android.petkovic.igoout.api.RestApiClient;
import hr.android.petkovic.igoout.model.Location;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity; //gdje se koristi?

public class HomeActivity extends AbstractFragmentActivity implements OnClickListener { //zakaj AbstractFragmentActivity?

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
	protected void onResume() { //kaj toèno se dešava ovdje?
		super.onResume();
		locationsListener = new LocationsListener() {

			@Override
			public void onLocationsReady(ArrayList<Location> locations) {
				hideDialog();
				HomeActivity.this.locations = locations;
				onSearchResults(locations);
			}
		};
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
		// onSearchResults(MockData.getLocations());
		showDialog();
		RestApiClient.get().getLocations(getInterests(), getVenues(), mSelectedRadius, locationsListener);
	}

	private void onSearchResults(ArrayList<Location> locations) {
		if (locations != null && locations.size() > 0) { // Zakaj dupla provjera?
			Intent intent = new Intent(this, SearchResultsActivity.class);
			intent.putExtra(Constants.LOCATIONS, locations);
			startActivity(intent);
		}
	}

	private void showInterestsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_interests);
		builder.setMultiChoiceItems(R.array.venues, mSelectedInterests, new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				mSelectedInterests[which] = isChecked;
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
		String savedInterests = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.SELECTED_INTERESTS, null);// Zakaj String, a ne array?
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
		PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.SELECTED_INTERESTS, sb.toString()).commit(); //Zakaj ne stavljati jednostavno bool vrijednosti? zakaj se koristi stringbuilder varijabla?
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

	private int[] getInterests() { // Zakaj se koristi pretvorba iz List<Integer> u int[] ?
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
		PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(Constants.SELECTED_RADIUS, mSelectedRadius);
	}

}
