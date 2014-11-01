package ru.stilsoft.treasuremount.model;

/**
 * Created  by dima  on 01.11.14.
 */
public class Treasure extends Location {

    public static final int
            TREASURE_TYPE_MONEY = 0,
            TREASURE_TYPE_EYE = 1,
            TREASURE_TYPE_TIME = 2;

    private long count;

    private int type;

    private Long treasureId;

    public Treasure() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getTreasureId() {
        return treasureId;
    }

    public void setTreasureId(Long treasureId) {
        this.treasureId = treasureId;
    }
}
