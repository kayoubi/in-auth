package com.inauth.domain;

/**
 * @author Khaled Ayoubi
 */
public class City {
    private final String name;
    private final Location location;

    public City(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
