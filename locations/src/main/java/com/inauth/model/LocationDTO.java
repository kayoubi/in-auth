package com.inauth.model;

import com.inauth.domain.Distance;

import java.util.List;

/**
 * @author Khaled Ayoubi
 */
public class LocationDTO {
    private double lng;
    private double lat;
    private boolean inUS;
    private List<Distance> distances;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public boolean isInUS() {
        return inUS;
    }

    public void setInUS(boolean inUS) {
        this.inUS = inUS;
    }

    public List<Distance> getDistances() {
        return distances;
    }

    public void setDistances(List<Distance> distances) {
        this.distances = distances;
    }
}

