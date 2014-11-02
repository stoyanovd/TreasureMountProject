package ru.stilsoft.treasuremount.databasesupport;

import android.content.ContentValues;
import ru.stilsoft.treasuremount.model.Location;
import ru.stilsoft.treasuremount.model.Statistics;

/**
 * Created by fm on 01.11.14.
 */
public class StatisticsWrapper {

    public static ContentValues getContentValues(Statistics statistics) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TreasureDatabaseHelper.column_id, statistics.getId());
        contentValues.put(TreasureDatabaseHelper.column_money, statistics.getMoney());

        return contentValues;
    }

    public static Statistics getStatistics(ContentValues contentValues) {
        Statistics statistics = new Statistics();

        statistics.setId(contentValues.getAsLong(TreasureDatabaseHelper.column_id));
        statistics.setMoney(contentValues.getAsLong(TreasureDatabaseHelper.column_money));

        return statistics;
    }
}
