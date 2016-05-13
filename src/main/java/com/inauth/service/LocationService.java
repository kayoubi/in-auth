package com.inauth.service;

import com.inauth.domain.Distance;
import com.inauth.domain.Location;

import java.util.List;
import java.util.Set;

/**
 * @author Khaled Ayoubi
 */
public interface LocationService {
    List<Location> findAll();

    Set<Long> findAllInUSA();

    boolean isInUs(Long locationId);

    Location findByLonAndLat(double lng, double lat);

    Location save(Location location);

    void deleteAll();

    List<Distance> findDistances(Long id);
}
