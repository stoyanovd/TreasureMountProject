package ru.stilsoft.treasuremount.databasesupport;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import ru.stilsoft.treasuremount.R;
import ru.stilsoft.treasuremount.model.Location;
import ru.stilsoft.treasuremount.model.Statistics;
import ru.stilsoft.treasuremount.model.Treasure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by fm on 01.11.14.
 */
public class DatabaseInitializer {

	public static final int NUMBER_OF_MAIN_LOCATIONS = 5;

	public static TreasureDatabaseHelper treasureDatabaseHelper;
	public static SQLiteDatabase sqLiteDatabase;
	public static boolean isInitialized = false;

	public static void initializeDatabases(Context context) {
		if (treasureDatabaseHelper == null) {
			treasureDatabaseHelper = new TreasureDatabaseHelper(context);
		}
		sqLiteDatabase = treasureDatabaseHelper.getWritableDatabase();
		//treasureDatabaseHelper.onlyDrop(sqLiteDatabase);

		checkInitialization();

		if (!isInitialized) {
			getMainLocationsAndGenerateTreasures(context);
		}
	}

	public static void closeDatabases() {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.close();
		}
		if (treasureDatabaseHelper != null) {
			treasureDatabaseHelper.close();
		}
	}

	public static void checkInitialization() {
		Cursor cursor = sqLiteDatabase.query(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS, null, null, null, null, null, null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0)
			isInitialized = true;
		cursor.close();
	}

	private static void getMainLocationsAndGenerateTreasures(Context context) {
		double[] read = readAnXML(context);
		//Double[] read = {0.0, 0.0, 1.0, 1.0, 2.0, 2.0, 3.0, 3.0, 4.0, 4.0};
		/*
		for (int i = 0; i < NUMBER_OF_MAIN_LOCATIONS; i++) {
			if (read[i] == null) {
				Toast.makeText(context, R.string.xml_file_not_found_exception, Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		*/

		long curTime = System.currentTimeMillis();

		int count = NUMBER_OF_MAIN_LOCATIONS * 2;
		for (int i = 0; i < count; i += 2) {
			Location location = new Location();
			location.setLatitude(read[i]);
			location.setLongitude(read[i + 1]);
			location.setLastChangedTime(curTime);
			location.setId((long) i);
			location.setState(Location.LOCATION_STATE_NEW);
			location.setAltitude(0.0);

			sqLiteDatabase.insert(TreasureDatabaseHelper.TABLE_NAME_LOCATIONS, null,
					LocationWrapper.getContentValues(location));

			for (int j = 0; j < TreasureGenerator.NUMBER_OF_NEAREST_TREASURES_ON_LOCATION; j++) {
				Treasure treasure = TreasureGenerator.generateNewTreasure(location, TreasureGenerator.NEAREST_AVERAGE_DISTANCE,
						2 * Math.PI * j / TreasureGenerator.NUMBER_OF_NEAREST_TREASURES_ON_LOCATION, j);
				sqLiteDatabase.insert(TreasureDatabaseHelper.TABLE_NAME_TREASURES, null,
						TreasureWrapper.getContentValues(treasure));
			}
			for (int j = 0; j < TreasureGenerator.NUMBER_OF_FAR_TREASURES_ON_LOCATION; j++) {
				Treasure treasure = TreasureGenerator.generateNewTreasure(location, TreasureGenerator.FAR_AVERAGE_DISTANCE,
						2 * Math.PI * j / TreasureGenerator.NUMBER_OF_FAR_TREASURES_ON_LOCATION, j + TreasureGenerator.NUMBER_OF_NEAREST_TREASURES_ON_LOCATION);
				sqLiteDatabase.insert(TreasureDatabaseHelper.TABLE_NAME_TREASURES, null,
						TreasureWrapper.getContentValues(treasure));
			}
		}

		sqLiteDatabase.insert(TreasureDatabaseHelper.TABLE_NAME_STATISTICS, null,
				StatisticsWrapper.getContentValues(new Statistics()));

		isInitialized = true;
	}

	private static double[] readAnXML(Context context) {

		try {
			File file = new File(Environment.getExternalStorageDirectory(), "/TreasureMount/" + "main_locations.txt");
			//File file = new File("/TreasureMount/main_locations.txt");
			if (!file.exists()) {
				System.out.println();
			}
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			//FileInputStream fileInputStream = context.openFileInput("main_locations.txt");
			StringBuilder text = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if (c == ';' || c == '.' || (c >= '0' && c <= '9')) {
						text.append(c);
					}
				}
			}
			//fileInputStream.close();
			bufferedReader.close();
			String[] doubles = text.toString().split(";");
			double[] ans = new double[doubles.length];
			for (int i = 0; i < doubles.length; ++i) {
				ans[i] = Double.parseDouble(doubles[i]);
			}
			return ans;

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("readXML error", e.getMessage(), e);
			Toast.makeText(context, R.string.xml_file_not_found_exception, Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
