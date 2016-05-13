package com.inauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.inauth.domain.Distance;
import com.inauth.domain.Location;
import com.inauth.repository.LocationRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Khaled Ayoubi
 */
@Service
public class LocationService {
    @Value("${google.api.key}")
    private String key;

    @Autowired
    private LocationRepository locationRepository;

    public boolean within(Location location, String country) {
        try {
            GeoApiContext context = new GeoApiContext().setApiKey(key);
            GeocodingResult[] result = GeocodingApi.reverseGeocode(context, new LatLng(location.getLat(), location.getLng())).await();
            if (result.length > 0) {
                System.out.println(result[0].formattedAddress);
                return result[0].formattedAddress.contains(country);
            }
            System.out.println("couldn't find address for: " + location);
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Set<Long> findAllInUSA() {
        return locationRepository.findAllInUSA(null);
    }

    public boolean isInUs(Long locationId) {
        return locationRepository.findAllInUSA(Stream.of(locationId).collect(Collectors.toSet())).contains(locationId);
    }

    public Location findByLonAndLat(double lng, double lat){
        return locationRepository.findByLonAndLat(lng, lat);
    }

    public Location save(Location location) {
        Location l = locationRepository.findByLonAndLat(location.getLng(), location.getLat());
        if (null != l) {
            throw new DuplicateKeyException("Duplicate location: " + location);
        }
        Long id = locationRepository.save(location);
        return locationRepository.get(id);
    }

    public List<Distance> findDistances(Long id) {
        return locationRepository.findDistances(id);
    }
}
