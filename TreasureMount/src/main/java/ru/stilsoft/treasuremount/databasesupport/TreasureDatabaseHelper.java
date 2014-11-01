package ru.stilsoft.treasuremount.databasesupport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fm on 01.11.14.
 */
public class TreasureDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "treasure_mount_database";

    public static final String TABLE_NAME_LOCATIONS = "locations_table";
    public static final String TABLE_NAME_TREASURES = "treasures_table";

    private static final int DATABASE_VERSION = 1;

    protected static final String column_id = "_ID";
    protected static final String column_latitude = "latitude";
    protected static final String column_longitude = "longitude";
    protected static final String column_altitude = "altitude";
    protected static final String column_state = "state";
    protected static final String column_lastChangedTime = "lastChangedTime";

    protected static final String column_count = "count";
    protected static final String column_type = "type";
    protected static final String column_treasureId = "treasureId";


    private static final String TABLE_TREASURES_CREATE = "create table " + TABLE_NAME_LOCATIONS + "( " +
            column_id + "integer primary key, " +
            column_latitude + "double, " +
            column_longitude + "double, " +
            column_altitude + "double, " +
            column_state + "integer, " +
            column_lastChangedTime + "integer, " +

            column_count + "integer, " +
            column_type + "integer, " +
            column_treasureId + "integer);";

    private static final String TABLE_LOCATIONS_CREATE = "create table " + TABLE_NAME_LOCATIONS + "( " +
            column_id + "integer primary key, " +
            column_latitude + "double, " +
            column_longitude + "double, " +
            column_altitude + "double, " +
            column_state + "integer, " +
            column_lastChangedTime + "integer);";


    public TreasureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_LOCATIONS_CREATE + TABLE_TREASURES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TreasureDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TREASURES);
        onCreate(database);
    }
}
