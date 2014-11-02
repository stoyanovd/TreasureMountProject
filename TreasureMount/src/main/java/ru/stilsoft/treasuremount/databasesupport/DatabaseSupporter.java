package ru.stilsoft.treasuremount.databasesupport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import ru.stilsoft.treasuremount.model.Location;
import ru.stilsoft.treasuremount.model.Treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fm on 02.11.14.
 */
public class DatabaseSupporter {

    public static List<Treasure> getTreasuresByMainLocation(Location location) {
        List<Treasure> list = new ArrayList<>();
        Cursor cursor = DatabaseInitializer.sqLiteDatabase.query(TreasureDatabaseHelper.TABLE_NAME_TREASURES, null,
                TreasureDatabaseHelper.column_treasureId + "=" + location.getId(), null, null, null, null);;
        while (cursor.moveToNext()) {
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            list.add(TreasureWrapper.getTreasure(contentValues));
        }
        cursor.close();
        return list;
    }

    public static List<Location> getMainLocations() {
        List<Location> list = new ArrayList<>();
        Cursor cursor = DatabaseInitializer.sqLiteDatabase.query(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS, null,
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            list.add(LocationWrapper.getLocation(contentValues));
        }
        cursor.close();
        return list;
    }

    public static void updateMainLocationInDatabase(Location location) {
        DatabaseInitializer.sqLiteDatabase.update(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS,
                LocationWrapper.getContentValues(location),
                TreasureDatabaseHelper.column_id + "=" + location.getId(), null);
    }

    public static void updateTreasureInDatabase(Treasure treasure) {
        DatabaseInitializer.sqLiteDatabase.update(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS,
                LocationWrapper.getContentValues(treasure),
                TreasureDatabaseHelper.column_id + "=" + treasure.getId(), null);
    }
}
