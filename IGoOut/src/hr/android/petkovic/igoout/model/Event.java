package hr.android.petkovic.igoout.model;

import java.io.Serializable;

public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8360392662700591537L;
	private int id;
	private int locationId;
	private String name;
	private String startTime;
	private String summary;
	private String detailsUrl;
	private String pictureUrl;
	private float ratingAvg;
	private int[] comments;
	private int[] assignedInterest;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}

	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public float getRatingAvg() {
		return ratingAvg;
	}

	public void setRatingAvg(float ratingAvg) {
		this.ratingAvg = ratingAvg;
	}

	public int[] getComments() {
		return comments;
	}

	public void setComments(int[] comments) {
		this.comments = comments;
	}

	public int[] getAssignedInterest() {
		return assignedInterest;
	}

	public void setAssignedInterest(int[] assignedInterest) {
		this.assignedInterest = assignedInterest;
	}

}
