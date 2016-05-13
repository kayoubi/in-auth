package com.inauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.inauth.domain.City;
import com.inauth.repository.CityRepository;

/**
 * @author Khaled Ayoubi
 */
@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public void createIfNotExist(City city) {
        try {
            cityRepository.save(city);
        } catch (DuplicateKeyException exception) {
            System.out.println("duplicate city creation: " + city.getName());
        }
    }
}
