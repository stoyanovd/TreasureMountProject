package ru.stilsoft.treasuremount.databasesupport;

import android.content.ContentValues;
import ru.stilsoft.treasuremount.model.Location;

/**
 * Created by fm on 01.11.14.
 */
public class LocationWrapper {

    public static ContentValues getContentValues(Location location) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TreasureDatabaseHelper.column_id, location.getId());
        contentValues.put(TreasureDatabaseHelper.column_latitude, location.getLatitude());
        contentValues.put(TreasureDatabaseHelper.column_longitude, location.getLongitude());
        contentValues.put(TreasureDatabaseHelper.column_altitude, location.getAltitude());
        contentValues.put(TreasureDatabaseHelper.column_state, location.getState());
        contentValues.put(TreasureDatabaseHelper.column_showTreasure, location.getShowTreasure());
        contentValues.put(TreasureDatabaseHelper.column_lastChangedTime, location.getLastChangedTime());

        return contentValues;
    }

    public static Location getLocation(ContentValues contentValues) {
        Location location = new Location();

        location.setId(contentValues.getAsLong(TreasureDatabaseHelper.column_id));
        location.setLatitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_latitude));
        location.setLongitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_longitude));
        location.setAltitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_altitude));
        location.setState((int) (long) (contentValues.getAsLong(TreasureDatabaseHelper.column_state)));
        location.setShowTreasure(contentValues.getAsBoolean(TreasureDatabaseHelper.column_showTreasure));
        location.setLastChangedTime(contentValues.getAsLong(TreasureDatabaseHelper.column_lastChangedTime));

        return location;
    }
}
