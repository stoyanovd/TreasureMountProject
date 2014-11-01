package ru.stilsoft.treasuremount.databasesupport;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ru.stilsoft.treasuremount.model.Location;

/**
 * Created by fm on 01.11.14.
 */
public class DatabaseInitializer {

    public static final int MAIN_LOCATION_NUMBER = 5;

    public static TreasureDatabaseHelper treasureDatabaseHelper;
    public static SQLiteDatabase sqLiteDatabase;
    public static boolean isInitialized = false;

    public static void initializeDatabases(Context context) {
        if (treasureDatabaseHelper == null)
            treasureDatabaseHelper = new TreasureDatabaseHelper(context);
        sqLiteDatabase = treasureDatabaseHelper.getReadableDatabase();
        if (!isInitialized) {
            checkInitialization();
        }
        if (!isInitialized) {
            getMainLocationsFromXML();
        }
    }

    public static void closeDatabases() {
        sqLiteDatabase.close();
        treasureDatabaseHelper.close();
    }

    public static void checkInitialization() {
        Cursor cursor = sqLiteDatabase.query(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext())
            isInitialized = true;
        cursor.close();
    }

    public static void getMainLocationsFromXML() {
        //TODO from file
        long[] read = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};   //new long[2 * MAIN_LOCATION_NUMBER];
        long curTime = System.currentTimeMillis();

        for (int i = 0; i < MAIN_LOCATION_NUMBER; i += 2) {
            Location location = new Location();
            location.setLatitude(read[i * 2]);
            location.setLongitude(read[i * 2 + 1]);
            location.setLastChangedTime(curTime);
            sqLiteDatabase.insert(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS, null,
                    LocationWrapper.getContentValues(location));
        }
        isInitialized = true;
    }
}
