package ru.stilsoft.treasuremount.model;

import java.util.UUID;

/**
 * Created  by dima  on 01.11.14.
 */
public class Location {

	public static final int
			LOCATION_STATE_NEW = 0,
			LOCATION_STATE_OPEN = 1,
			LOCATION_STATE_FINISHED = 2;

	private Long id;

	private double latitude;

	private double longitude;

	private double altitude;

	private int state = LOCATION_STATE_NEW;

	public Location() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}