package ru.stilsoft.treasuremount.model;

/**
 * Created  by dima  on 01.11.14.
 */
public class Statistics {

    private Long id;

    private long money;

    public Statistics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}