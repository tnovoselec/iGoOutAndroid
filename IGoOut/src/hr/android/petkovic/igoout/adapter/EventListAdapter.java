package hr.android.petkovic.igoout.adapter;

import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.model.Event;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter {

	private ArrayList<Event> events;
	private LayoutInflater inflater;
	private String[] interests;

	public EventListAdapter(Context context, ArrayList<Event> events) {
		this.events = events;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.interests = context.getResources().getStringArray(R.array.interests);
	}

	@Override
	public int getCount() {
		return events != null ? events.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return events != null ? events.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.event_list_item, parent, false);
		}
		Event event = events.get(position);
		TextView title = (TextView) v.findViewById(R.id.event_list_item_title);
		TextView interests = (TextView) v.findViewById(R.id.event_list_item_interests);

		title.setText(event.getName());
		interests.setText(buildInterests(event));
		return v;
	}

	private String buildInterests(Event event) {
		StringBuilder sb = new StringBuilder();
		for (int i : event.getAssignedInterest()) {
			sb.append(interests[i]);
			sb.append(" ");
		}
		return sb.toString();
	}

}
