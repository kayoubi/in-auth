package com.inauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.LatLng;
import com.inauth.domain.City;
import com.inauth.domain.Location;
import com.inauth.service.CityService;
import com.inauth.service.LocationService;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LocationsApplication {
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

	private void fixture() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double minLng = -180.00;
        double maxLng = 180.00;
        for (int i = 0; i < 10000; i++) {
            double latitude = minLat + (Math.random() * ((maxLat - minLat) + 1));
            double longitude = minLng + (Math.random() * ((maxLng - minLng) + 1));

//            DecimalFormat df = new DecimalFormat("#.#####");
//            System.out.println("latitude:longitude --> " + df.format(latitude) + "," + df.format(longitude));
            locationService.save(new Location(longitude, latitude));
        }
    }

    private void cities(String key, String[] cities) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey(key);

        for(String city : cities) {
            String name = city.replace("_", ",");
            LatLng l = GeocodingApi.geocode(context, name).await()[0].geometry.location;
            City c = new City(name, new Location(l.lng, l.lat));
            cityService.createIfNotExist(c);
        }
    }
}
