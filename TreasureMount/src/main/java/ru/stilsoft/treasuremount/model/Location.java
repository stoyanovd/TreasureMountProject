package ru.stilsoft.treasuremount.model;

import org.osmdroid.api.IGeoPoint;

/**
 * Created  by dima  on 01.11.14.
 */
public class Location implements IGeoPoint {

    public static final int
            LOCATION_STATE_NEW = 0,
            LOCATION_STATE_OPEN = 1,
            LOCATION_STATE_FINISHED = 2;

    private Long id;

    private double latitude;

    private double longitude;

    private double altitude;

    private int state = LOCATION_STATE_NEW;

    private long lastChangedTime;

    private int latitudeE6;

    private int longitudeE6;

    public Location() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getLatitudeE6() {
        return latitudeE6;
    }

    @Override
    public int getLongitudeE6() {
        return longitudeE6;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.latitudeE6 = (int) (latitude * 1E6);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.longitudeE6 = (int) (longitude * 1E6);
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


    public long getLastChangedTime() {
        return lastChangedTime;
    }

    public void setLastChangedTime(long lastChangedTime) {
        this.lastChangedTime = lastChangedTime;
    }
}