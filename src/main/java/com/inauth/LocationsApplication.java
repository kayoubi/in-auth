package com.inauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.LatLng;
import com.inauth.domain.City;
import com.inauth.domain.Location;
import com.inauth.service.CityService;
import com.inauth.service.LocationService;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LocationsApplication extends SpringBootServletInitializer {
    @Autowired
    private LocationService locationService;
    @Autowired
    private CityService cityService;

    @Value("${google.api.key}")
    private String key;

    @Value("${cities}")
    private String[] cities;

    @PostConstruct
    public void init() throws Exception {
        fixture();
        cities(key, cities);
    }

    public static void main(String[] args) {
        SpringApplication.run(LocationsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LocationsApplication.class);
    }

    private void fixture() {
        locationService.deleteAll();
        System.out.print("DB loading ");

        for (int i = 0; i < 10000; i++) {
            double latitude = random(-90.00, 90.00);
            double longitude = random(-180.00, 180.00);

            System.out.print(i % 5 == 0 ? "." : "");
//            DecimalFormat df = new DecimalFormat("#.#####");
//            System.out.println("latitude:longitude --> " + df.format(latitude) + "," + df.format(longitude));
            locationService.save(new Location(longitude, latitude));
        }
        System.out.println("");
    }

    private double random(double min, double max) {
        return min + (Math.random() * ((max - min) + 1));
    }

    private void cities(String key, String[] cities) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(key);

        for (String city : cities) {
            String name = city.replace("_", ",");
            LatLng l = GeocodingApi.geocode(context, name).await()[0].geometry.location;
            City c = new City(name, new Location(l.lng, l.lat));
            cityService.createIfNotExist(c);
        }
    }
}
