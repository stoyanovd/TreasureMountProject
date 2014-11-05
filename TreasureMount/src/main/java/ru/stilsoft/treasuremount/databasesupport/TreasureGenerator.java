package ru.stilsoft.treasuremount.databasesupport;

import org.osmdroid.util.GeoPoint;
import ru.stilsoft.treasuremount.model.Location;
import ru.stilsoft.treasuremount.model.Treasure;

/**
 * Created  by dima  on 02.11.14.
 */
public class TreasureGenerator {
	public static final int NUMBER_OF_TREASURES_ON_LOCATION = 30;

	public static final int NUMBER_OF_NEAREST_TREASURES_ON_LOCATION = 15;
	public static final int NUMBER_OF_FAR_TREASURES_ON_LOCATION = NUMBER_OF_TREASURES_ON_LOCATION - NUMBER_OF_NEAREST_TREASURES_ON_LOCATION;

	public static final double NEAREST_AVERAGE_DISTANCE = 40.0;
	public static final double FAR_AVERAGE_DISTANCE = 70.0;

	// Average distance between treasures in nearest circle = NEAREST_AVERAGE_DISTANCE / 2 (depends on NUMBER_OF_NEAREST_TREASURES_ON_LOCATION too)
	//  and so on for far circle
	public static final double RANDOM_MOVE_IN_ANGLE = 2 * Math.PI / 180;
	public static final double AVERAGE_DISTANCE_ERROR_METERS = 5.0;

	public static final double TIME_BONUS_CHANCE = 0.15;
	public static final double EYE_BONUS_CHANCE = 0.15;
	public static final double MONEY_BONUS_CHANCE = 0.70;

	public static final double MONEY_CHANCE_100 = 0.25;
	public static final double MONEY_CHANCE_50 = 0.40;
	public static final double MONEY_CHANCE_20 = 0.35;

	public static Treasure generateNewTreasure(Location location, double distance, double angle, int j) {
		Treasure treasure = new Treasure();

		treasure.setId(location.getId() * NUMBER_OF_TREASURES_ON_LOCATION + j);

		double randomizedAngle = angle + getRandomOffsetWithLimit(RANDOM_MOVE_IN_ANGLE);
		double randomizedDistance = distance + getRandomOffsetWithLimit(AVERAGE_DISTANCE_ERROR_METERS);

		GeoPoint locationGeoPoint = new GeoPoint(location.getLatitudeE6(), location.getLongitudeE6());
		GeoPoint treasureGeoPoint = locationGeoPoint.destinationPoint(randomizedDistance, (float) randomizedAngle);

		treasure.setLatitude(treasureGeoPoint.getLatitude());
		treasure.setLongitude(treasureGeoPoint.getLongitude());

		treasure.setAltitude(0.0);
		treasure.setState(Treasure.LOCATION_STATE_NEW);
		treasure.setLastChangedTime(System.currentTimeMillis());

		treasure.setCount(0);
		treasure.setType(getRandomType());
		if (treasure.getType() == Treasure.TREASURE_TYPE_MONEY) {
			treasure.setCount(getRandomMoney());
		} else if (treasure.getType() == Treasure.TREASURE_TYPE_TIME) {
			treasure.setCount(2);
		}

		treasure.setTreasureId(location.getId());
		return treasure;
	}

	private static int getRandomType() {
		double type = Math.random();
		if (type < TIME_BONUS_CHANCE) {
			return Treasure.TREASURE_TYPE_TIME;
		}
		type -= TIME_BONUS_CHANCE;
		if (type < EYE_BONUS_CHANCE) {
			return Treasure.TREASURE_TYPE_EYE;
		}
		return Treasure.TREASURE_TYPE_MONEY;
	}

	private static int getRandomMoney() {
		double type = Math.random();
		if (type < MONEY_CHANCE_100) {
			return 100;
		}
		type -= MONEY_CHANCE_100;
		if (type < MONEY_CHANCE_50) {
			return 50;
		}
		return 20;
	}

	private static double getRandomOffsetWithLimit(double limit) {
		return (Math.random() - 0.5) * 2 * limit;
	}

}
