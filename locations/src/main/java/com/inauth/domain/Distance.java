package com.inauth.domain;

/**
 * @author Khaled Ayoubi
 */
public class Distance {
    private Long locationId;
    private String city;
    private Double distance;

    public Distance(Long locationId, String city, Double distance) {
        this.locationId = locationId;
        this.city = city;
        this.distance = Math.round(distance*100.0)/100.0;
    }

    public String getCity() {
        return city;
    }

    public Double getDistance() {
        return distance;
    }
}
