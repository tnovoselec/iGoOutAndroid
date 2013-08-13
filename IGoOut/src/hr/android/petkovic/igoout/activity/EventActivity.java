package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.adapter.CommentsAdapter;
import hr.android.petkovic.igoout.model.Comment;
import hr.android.petkovic.igoout.model.Event;
import hr.android.petkovic.igoout.model.Location;
import hr.android.petkovic.igoout.model.MockData;
import hr.android.petkovic.igoout.utils.BitmapLruCache;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class EventActivity extends AbstractFragmentActivity {

	private ListView commentsList;
	private NetworkImageView eventImg;
	private TextView eventName;
	private TextView locationName;
	private TextView interests;
	private TextView startTime;
	private TextView summary;
	private TextView moreInfo;
	private RatingBar ratingBar;

	private View header;

	private Event event;
	private Location location;
	private ArrayList<Comment> comments;
	private CommentsAdapter adapter;

	private RequestQueue mRequestQueue;
	private ImageLoader imageLoader;

	private String[] interestsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);

		mRequestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(BitmapLruCache.getDefaultLruCacheSize()));
		interestsList = getResources().getStringArray(R.array.interests);

		header = getLayoutInflater().inflate(R.layout.event_header, null, false);

		commentsList = (ListView) findViewById(R.id.event_comment_list);
		commentsList.addHeaderView(header);
		eventImg = (NetworkImageView) header.findViewById(R.id.event_img);
		eventName = (TextView) header.findViewById(R.id.event_name);
		locationName = (TextView) header.findViewById(R.id.event_location_name);
		interests = (TextView) header.findViewById(R.id.event_interest_list);
		startTime = (TextView) header.findViewById(R.id.event_start_time);
		summary = (TextView) header.findViewById(R.id.event_summary);
		moreInfo = (TextView) header.findViewById(R.id.event_more_info);
		ratingBar = (RatingBar) header.findViewById(R.id.event_rating);

		event = (Event) getIntent().getSerializableExtra(Constants.EVENT);
		location = (Location) getIntent().getSerializableExtra(Constants.LOCATION);
		comments = MockData.getComments();
		adapter = new CommentsAdapter(this, comments);
		commentsList.setAdapter(adapter);

		eventImg.setDefaultImageResId(R.drawable.ic_launcher);
		eventImg.setImageUrl(event.getPictureUrl(), imageLoader);
		eventName.setText(event.getName());
		locationName.setText(location.getName());
		interests.setText(buildInterests(event));
		startTime.setText(getString(R.string.start_time, event.getStartTime()));
		summary.setText(event.getSummary());
		moreInfo.setText(getString(R.string.more_info, event.getDetailsUrl()));
		ratingBar.setRating(event.getRatingAvg());

		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setTitle(event.getName());
			ab.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String buildInterests(Event event) {
		StringBuilder sb = new StringBuilder();
		for (int i : event.getAssignedInterest()) {
			sb.append(interestsList[i]);
			sb.append(" ");
		}
		return sb.toString();
	}

}
