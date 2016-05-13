package com.inauth.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Khaled Ayoubi
 */
@Entity
public class Location {
    @Id
    private final Long id;
    private final double lng;
    private final double lat;

    public Location(double lng, double lat) {
        this(null, lng, lat);
    }

    public Location(Long id, double lng, double lat) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return "Location{" +
            "lng=" + lng +
            ", lat=" + lat +
            '}';
    }
}
