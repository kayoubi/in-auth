package com.inauth.model;

/**
 * @author Khaled Ayoubi
 */
public class DistanceDTO {
    private final String city;
    private final double distance;

    public DistanceDTO(String city, double distance) {
        this.city = city;
        this.distance = distance;
    }

    public String getCity() {
        return city;
    }

    public double getDistance() {
        return distance;
    }
}
