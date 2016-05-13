package com.inauth.repository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.inauth.domain.Distance;
import com.inauth.domain.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Khaled Ayoubi
 */
public class LocationRepositoryImpl implements LocationRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Location get(Long id) {
        List<Location> r = jdbcTemplate.query(
            "select id, ST_AsGeoJSON(geom) from inauth.locations where id = ?",
            new Object[] {id},
            new LocationRowMapper()
        );
        return r.size() > 0 ? r.get(0) : null;
    }

    @Override
    public List<Location> findAll() {
        return jdbcTemplate.query(
            "select id, ST_AsGeoJSON(geom) from inauth.locations",
            new LocationRowMapper()
        );
    }

    @Override
    public Set<Long> findAllInUSA(Set<Long> ids) {
        String sql = "select l.id, st_contains(s.geom_4269, l.geom) as tellme, s.name from inauth.locations l, inauth.tl_2012_states s";
        Object[] params = new Object[] {};
        if (null != ids && !ids.isEmpty()) {
            sql += " where l.id in (" + buildInClause(ids.size()) + ")";
            params = ids.toArray();
        }
        Set<Long> result = new HashSet<>();
        jdbcTemplate.query(
            sql,
            params,
            resultSet -> {
                if (resultSet.getBoolean(2)) {
                    result.add(resultSet.getLong(1));
                }
            }
        );
        return result;
    }

    @Override
    public List<Distance> findDistances(Long id) {
        return jdbcTemplate.query(
            "select l.id, c.name, ST_Distance(l.geom::geometry, c.geom::geometry) from inauth.locations as l, inauth.cities as c where l.id = ?",
            new Object[]{id},
            (resultSet, i) -> new Distance(resultSet.getLong(1), resultSet.getString(2), resultSet.getDouble(3) * 62.137)
        );
    }

    public Map<Long, List<Distance>> findDistancesAll() {
        Map<Long, List<Distance>> result = new Hashtable<>();
        jdbcTemplate.query(
            "select l.id, c.name, ST_Distance(l.geom::geometry, c.geom::geometry) from inauth.locations as l, inauth.cities as c",
            resultSet -> {
                Long locationId = resultSet.getLong(1);
                result.computeIfAbsent(locationId, l -> new ArrayList<>());
                result.get(locationId).add(new Distance(resultSet.getLong(1), resultSet.getString(2), resultSet.getDouble(3) * 62.137));
            }
        );
        return result;
    }

    @Override
    public Location findByLonAndLat(double lng, double lat) {
        List<Location> r = jdbcTemplate.query("select id, ST_AsGeoJSON(geom) FROM inauth.LOCATIONS where geom = " +
            "(ST_GeomFromText('POINT(" + lng + " " +  lat + ")', 4269))",
            new LocationRowMapper());

        return r.size() > 0 ? r.get(0) : null;
    }

    @Override
    public Long save(Location location) {
        String sql = "INSERT INTO inauth.LOCATIONS (geom) values " +
            "(ST_GeomFromText('POINT(" + location.getLng() + " " + location.getLat() + ")', 4269))";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            return con.prepareStatement(sql, new String[] {"id"});
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private String buildInClause(int batchSize) {
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < batchSize; i++) {
            inClause.append('?');
            if (i + 1 < batchSize) {
                inClause.append(',');
            }
        }
        return inClause.toString();
    }

    private class LocationRowMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong(1);
            String j = resultSet.getString(2);
            JSONObject json = new JSONObject(j);
            return new Location(id, json.getJSONArray("coordinates").getDouble(0), json.getJSONArray("coordinates").getDouble(1));
        }
    }
}
