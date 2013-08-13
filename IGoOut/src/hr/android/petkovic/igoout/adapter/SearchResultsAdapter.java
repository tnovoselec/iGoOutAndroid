package hr.android.petkovic.igoout.adapter;

import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.model.Location;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchResultsAdapter extends BaseAdapter {

	private ArrayList<Location> locations;
	private LayoutInflater inflater;

	public SearchResultsAdapter(Context context, ArrayList<Location> locations) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.locations = locations;
	}

	@Override
	public int getCount() {
		return locations != null ? locations.size() : 0;
	}

	@Override
	public Object getItem(int positions) {
		return locations != null ? locations.get(positions) : null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.search_result_item, parent, false);
		}
		Location loc = locations.get(position);
		TextView title = (TextView) v.findViewById(R.id.search_result_item_title);
		TextView address = (TextView) v.findViewById(R.id.search_result_item_address);

		title.setText(loc.getName());
		address.setText(loc.getAddress());
		return v;
	}

}
