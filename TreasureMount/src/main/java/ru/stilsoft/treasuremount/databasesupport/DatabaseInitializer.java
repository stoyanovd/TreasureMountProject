package ru.stilsoft.treasuremount.databasesupport;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import ru.stilsoft.treasuremount.R;
import ru.stilsoft.treasuremount.model.Location;

import java.io.FileInputStream;
import java.io.IOException;

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
            getMainLocationsFromXML(context);
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

    public static void getMainLocationsFromXML(Context context) {
        //TODO from file
        //getMainLocationsFromXML(context);
        double[] read = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};   //new long[2 * MAIN_LOCATION_NUMBER];
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

    private static Double[] readAnXML(Context context) {

        try {
            FileInputStream fileInputStream = context.openFileInput("main_locations.txt");
            byte[] bytes = new byte[1000];
            StringBuilder text = new StringBuilder();
            int read = 0;
            while ((read = fileInputStream.read(bytes)) != -1) {
                for (int i = 0; i < read; i++) {
                    if (bytes[i] > 127)
                        throw new IOException("Error in txt with Main Locations.");
                    else text.append((char) bytes[i]);
                }
            }
            fileInputStream.close();
            String[] doubles = text.toString().split(";");
            Double[] ans = new Double[doubles.length];
            for (int i = 0; i < doubles.length; i++)
                ans[i] = Double.parseDouble(doubles[i]);
            return ans;

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.xml_file_not_found_exception, Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
