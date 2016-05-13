package com.inauth.model;

import com.inauth.domain.Distance;

import java.util.List;

/**
 * @author Khaled Ayoubi
 */
public class DetailedLocationDTO extends LocationDTO {
    private boolean inUS;
    private List<Distance> distances;

    public DetailedLocationDTO(double lng, double lat) {
        super(lng, lat);
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
