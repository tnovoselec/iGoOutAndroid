package hr.android.petkovic.igoout.adapter;

import hr.android.petkovic.igoout.R;
import hr.android.petkovic.igoout.model.Comment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {

	private ArrayList<Comment> comments;
	private LayoutInflater inflater;

	public CommentsAdapter(Context context, ArrayList<Comment> comments) {
		this.comments = comments;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return comments != null ? comments.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return comments != null ? comments.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = inflater.inflate(R.layout.comment_list_item, parent, false);
		}
		Comment comm = comments.get(position);
		TextView name = (TextView) v.findViewById(R.id.comment_list_item_name);
		TextView comment = (TextView) v.findViewById(R.id.comment_list_item_comment);
		name.setText(comm.getUsername());
		comment.setText(comm.getComment());

		return v;
	}

}
