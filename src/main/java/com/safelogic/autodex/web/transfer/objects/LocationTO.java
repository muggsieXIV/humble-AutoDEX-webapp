package com.safelogic.autodex.web.transfer.objects;

public class LocationTO {

	private String homeLatitude;
	private String homeLongitude;
	private String latitude;
	private String longtitude;
	
	private boolean notifyContacts;
	
	public LocationTO(){
		
	}

	public String getHomeLatitude() {
		return homeLatitude;
	}

	public void setHomeLatitude(String homeLatitude) {
		this.homeLatitude = homeLatitude;
	}

	public String getHomeLongitude() {
		return homeLongitude;
	}

	public void setHomeLongitude(String homeLongitude) {
		this.homeLongitude = homeLongitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public boolean isNotifyContacts() {
		return notifyContacts;
	}

	public void setNotifyContacts(boolean notifyContacts) {
		this.notifyContacts = notifyContacts;
	}

}
