package ru.stilsoft.treasuremount.databasesupport;

import android.content.ContentValues;
import ru.stilsoft.treasuremount.model.Treasure;

/**
 * Created by fm on 01.11.14.
 */
public class TreasureWrapper {

    public static ContentValues getContentValues(Treasure treasure) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TreasureDatabaseHelper.column_id, treasure.getId());
        contentValues.put(TreasureDatabaseHelper.column_latitude, treasure.getLatitude());
        contentValues.put(TreasureDatabaseHelper.column_longitude, treasure.getLongitude());
        contentValues.put(TreasureDatabaseHelper.column_altitude, treasure.getAltitude());
        contentValues.put(TreasureDatabaseHelper.column_state, treasure.getState());
        contentValues.put(TreasureDatabaseHelper.column_showTreasure, treasure.getShowTreasure());
        contentValues.put(TreasureDatabaseHelper.column_lastChangedTime, treasure.getLastChangedTime());


        contentValues.put(TreasureDatabaseHelper.column_count, treasure.getCount());
        contentValues.put(TreasureDatabaseHelper.column_type, treasure.getType());
        contentValues.put(TreasureDatabaseHelper.column_treasureId, treasure.getTreasureId());

        return contentValues;
    }

    public static Treasure getTreasure(ContentValues contentValues) {
        Treasure treasure = new Treasure();

        treasure.setId(contentValues.getAsLong(TreasureDatabaseHelper.column_id));
        treasure.setLatitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_latitude));
        treasure.setLongitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_longitude));
        treasure.setAltitude(contentValues.getAsDouble(TreasureDatabaseHelper.column_altitude));
        treasure.setState((int) (long) (contentValues.getAsLong(TreasureDatabaseHelper.column_state)));
        treasure.setShowTreasure(contentValues.getAsBoolean(TreasureDatabaseHelper.column_showTreasure));
        treasure.setLastChangedTime(contentValues.getAsLong(TreasureDatabaseHelper.column_lastChangedTime));

        treasure.setCount(contentValues.getAsLong(TreasureDatabaseHelper.column_count));
        treasure.setType((int) (long) contentValues.getAsLong(TreasureDatabaseHelper.column_type));
        treasure.setTreasureId(contentValues.getAsLong(TreasureDatabaseHelper.column_treasureId));

        return treasure;
    }
}
