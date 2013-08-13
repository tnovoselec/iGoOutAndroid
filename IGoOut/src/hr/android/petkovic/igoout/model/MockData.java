package hr.android.petkovic.igoout.model;

import java.util.ArrayList;

public class MockData {

	public static ArrayList<Location> getLocations() {
		ArrayList<Location> locations = new ArrayList<Location>();
		Location loc = new Location();
		loc.setAddress("Kneza Mislava 20");
		loc.setLat(45.80);
		loc.setLng(15.85);
		loc.setName("Beertija");
		loc.setPhoneNumber("01 222 333");
		loc.setPictureUrl("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTBVEBlV9602jWLDXHlxz1CE1csbXxKlWsM_9PfFe3JoHq-hs6t");
		loc.setSummary("Dojdi se napit buog te jeba!");
		loc.setType("Bar");
		loc.setWebsite("http://www.ratebeer.com/p/the-beertija-zagreb/30680/");
		loc.setWorkingHours("8:00 - 23:00");
		locations.add(loc);

		loc = new Location();
		loc.setAddress("Branimirova 22");
		loc.setLat(45.85);
		loc.setLng(15.95);
		loc.setName("Cinestar");
		loc.setPhoneNumber("01 222 2533");
		loc.setPictureUrl("http://payload.cargocollective.com/1/0/12188/127616/cinestar1.jpg");
		loc.setSummary("Najbolji izbor filmova!");
		loc.setType("Nightclub");
		loc.setWebsite("http://www.blitz-cinestar.hr/");
		loc.setWorkingHours("10:00 - 23:00");
		locations.add(loc);
		return locations;
	}

	public static ArrayList<Event> getEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		Event event = new Event();
		event.setName("Lan Party");
		event.setAssignedInterest(new int[] { 0, 4, 6 });
		event.setDetailsUrl("www.bug.hr");
		event.setPictureUrl("https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTx1pqhazUXvYCMdirNaGkHcf7kEOPpfPpWpXV2bHQE5-SfLpi6bg");
		event.setRatingAvg(4.5f);
		event.setStartTime("20:00");
		event.setSummary("Vuco, kurve i janjetina");
		events.add(event);

		event = new Event();
		event.setName("Pijanka");
		event.setAssignedInterest(new int[] { 2, 3, 8 });
		event.setDetailsUrl("www.net.hr");
		event.setPictureUrl("http://hooplaha.com/wp-content/uploads/2012/12/Lager-Day.jpg");
		event.setRatingAvg(5.0f);
		event.setStartTime("21:00");
		event.setSummary("Sto sam boze tako proklet, jucer pijan danas opet...");
		events.add(event);
		return events;
	}

	public static ArrayList<Comment> getComments() {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Comment comment = new Comment();
		comment.setUsername("Pero Peric");
		comment.setComment("Ti boga kaj smo se ftrgli!");
		comments.add(comment);

		comment = new Comment();
		comment.setUsername("Muka Mukic");
		comment.setComment("To ce mnogo da te kosta!");
		comments.add(comment);
		return comments;
	}

}
