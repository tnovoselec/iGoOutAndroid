package hr.android.petkovic.igoout.activity;

import hr.android.petkovic.igoout.Constants;
import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.adapter.CommentsAdapter;
import hr.android.petkovic.igoout.api.CommentListener;
import hr.android.petkovic.igoout.api.CommentsListener;
import hr.android.petkovic.igoout.api.RateListener;
import hr.android.petkovic.igoout.api.RestApiClient;
import hr.android.petkovic.igoout.model.Comment;
import hr.android.petkovic.igoout.model.Event;
import hr.android.petkovic.igoout.model.Location;
import hr.android.petkovic.igoout.model.User;
import hr.android.petkovic.igoout.utils.BitmapLruCache;
import hr.android.petkovic.igoout.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class EventActivity extends AbstractFragmentActivity implements OnClickListener {

	private ListView commentsList;
	private NetworkImageView eventImg;
	private TextView eventName;
	private TextView locationName;
	private TextView interests;
	private TextView startTime;
	private TextView summary;
	private TextView moreInfo;
	private RatingBar ratingBar;
	private View rate;
	private View alertMe;
	private View comment;

	private View header;

	private Event event;
	private Location location;
	private ArrayList<Comment> comments;
	private CommentsAdapter adapter;

	private RequestQueue mRequestQueue;
	private ImageLoader imageLoader;

	private String[] interestsList;
	private CommentsListener commentsListener;

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
		rate = findViewById(R.id.event_menu_rate);
		alertMe = findViewById(R.id.event_menu_alert_me);
		comment = findViewById(R.id.event_menu_comment);

		rate.setOnClickListener(this);
		alertMe.setOnClickListener(this);
		comment.setOnClickListener(this);

		event = (Event) getIntent().getSerializableExtra(Constants.EVENT);
		location = (Location) getIntent().getSerializableExtra(Constants.LOCATION);

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
		getData();
	}

	private void getData() {
		showDialog();
		commentsListener = new CommentsListener() {

			@Override
			public void onCommentsReady(ArrayList<Comment> comments) {
				hideDialog();
				EventActivity.this.comments = comments;
				adapter = new CommentsAdapter(EventActivity.this, comments);
				commentsList.setAdapter(adapter);
			}
		};
		RestApiClient.get().getComments(event.getId(), commentsListener);

	}

	@Override
	protected void onPause() {
		commentsListener = null;
		super.onPause();
	}

	private String buildInterests(Event event) {
		StringBuilder sb = new StringBuilder();
		for (int i : event.getAssignedInterest()) {
			sb.append(interestsList[i]);
			sb.append(" ");
		}
		return sb.toString();
	}

	private void addEventToCalendar() {
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());
		intent.putExtra("allDay", true);
		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
		intent.putExtra("title", event.getName());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == rate) {
			showRateDialog();
		} else if (v == alertMe) {
			addEventToCalendar();
		} else if (v == comment) {
			showCommentDialog();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.event_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.location_share) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_SUBJECT, event.getName());
			intent.putExtra(Intent.EXTRA_TEXT, event.getDetailsUrl());
			intent.setType("text/plain");
			startActivity(Intent.createChooser(intent, getString(R.string.share)));
			return true;
		} else if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showRateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.rate_event);
		View v = getLayoutInflater().inflate(R.layout.rate, null, false);
		final RatingBar rate = (RatingBar) v.findViewById(R.id.event_rate);
		rate.setMax(5);
		builder.setView(v);
		builder.setPositiveButton(R.string.rate, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int rating = (int) rate.getRating();
				RateListener rateListener = new RateListener() {

					@Override
					public void onSuccess() {
						hideDialog();
						Toast.makeText(EventActivity.this, R.string.event_rated_successfully, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFailure(int code) {
						hideDialog();
						if (code == 552) {
							Toast.makeText(EventActivity.this, R.string.error_already_rated, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(EventActivity.this, R.string.error_server_error, Toast.LENGTH_SHORT).show();
						}
					}
				};
				User user = Utils.getUser(EventActivity.this);
				showDialog();
				RestApiClient.get().rateEvent(event.getId(), user.getId(), user.getUsername(), rating, rateListener);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

	private void showCommentDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.comment_event);
		View v = getLayoutInflater().inflate(R.layout.comment, null, false);
		final EditText comment = (EditText) v.findViewById(R.id.event_comment);
		comment.setMaxLines(2);
		builder.setView(v);
		builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				CommentListener commentListener = new CommentListener() {

					@Override
					public void onSuccess() {
						hideDialog();
						Toast.makeText(EventActivity.this, R.string.comment_added_successfully, Toast.LENGTH_SHORT).show();
						getData();
					}

					@Override
					public void onFailure(int code) {
						hideDialog();
						if (code == 552) {
							Toast.makeText(EventActivity.this, R.string.error_max_comments, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(EventActivity.this, R.string.error_server_error, Toast.LENGTH_SHORT).show();
						}
					}
				};
				User user = Utils.getUser(EventActivity.this);
				showDialog();
				RestApiClient.get().commentEvent(event.getId(), user.getId(), user.getUsername(), comment.getText().toString(), commentListener);

			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}
}
