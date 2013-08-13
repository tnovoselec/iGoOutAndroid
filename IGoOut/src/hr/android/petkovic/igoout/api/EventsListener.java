package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.model.Event;

import java.util.ArrayList;

public interface EventsListener {

	void onEventsReady(ArrayList<Event> events);

}
