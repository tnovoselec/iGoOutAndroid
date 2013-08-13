package hr.android.petkovic.igoout.api;

import hr.android.petkovic.igoout.model.Location;

import java.util.ArrayList;

public interface LocationsListener {

	void onLocationsReady(ArrayList<Location> locations);

}
