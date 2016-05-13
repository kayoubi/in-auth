package com.inauth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.inauth.domain.City;

/**
 * @author Khaled Ayoubi
 */
@Repository
public class CityRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(City city) {
        jdbcTemplate.execute("INSERT INTO inauth.cities (name, geom) values " +
            "('" + city.getName() + "', ST_GeomFromText('POINT(" + city.getLocation().getLng() + " " + city.getLocation().getLat() + ")', 4269))");
    }
}
