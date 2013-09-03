package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.model.Comment;

import java.util.ArrayList;

public interface CommentsListener {
	void onCommentsReady(ArrayList<Comment> comments);
}
