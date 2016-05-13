package com.inauth.repository;

import org.springframework.data.repository.Repository;

import com.inauth.domain.Distance;
import com.inauth.domain.Location;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Khaled Ayoubi
 */
public interface LocationRepository extends Repository<Location, Long> {
    Location get(Long id);

    List<Location> findAll();

    Set<Long> findAllInUSA(Set<Long> ids);

    List<Distance> findDistances(Long id);

    Map<Long, List<Distance>> findDistancesAll();

    Location findByLonAndLat(double lng, double lat);

    Long save(Location location);

    void deleteAll();
}
