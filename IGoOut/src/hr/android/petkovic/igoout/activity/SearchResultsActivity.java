package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.adapter.SearchResultsAdapter;
import hr.android.petkovic.igoout.model.Location;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SearchResultsActivity extends AbstractFragmentActivity implements OnItemClickListener {

	private ListView list;
	private SearchResultsAdapter adapter;
	private ArrayList<Location> locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results);

		list = (ListView) findViewById(R.id.search_results_list);
		locations = (ArrayList<Location>) getIntent().getSerializableExtra(Constants.LOCATIONS);
		if (locations != null) {
			adapter = new SearchResultsAdapter(this, locations);
			list.setAdapter(adapter);
		}

		list.setOnItemClickListener(this);

		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this, LocationActivity.class);
		intent.putExtra(Constants.LOCATION, locations.get(arg2));
		startActivity(intent);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
