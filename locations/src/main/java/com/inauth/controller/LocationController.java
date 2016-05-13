package com.inauth.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.inauth.domain.Distance;
import com.inauth.domain.Location;
import com.inauth.model.LocationDTO;
import com.inauth.service.LocationService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Khaled Ayoubi
 */
@RestController
@RequestMapping(LocationController.BASE_URI)
public class LocationController {
    protected static final String BASE_URI = "/inauth/locations";

    @Autowired
    private LocationService locationService;

    @Value("${cities}")
    private String[] cities;

    @RequestMapping(method = RequestMethod.GET)
    public List<LocationDTO> getAll(@RequestParam(required = false) String format, HttpServletResponse response) throws Exception {
        Set<Long> inUS = locationService.findAllInUSA();
        List<LocationDTO> result = locationService.findAll()
            .stream()
            .map(l -> toDTO(l, inUS.contains(l.getId())))
            .collect(Collectors.toList());
        if (null != format && format.equals("xls")) {
            toXLS(response, result);
            return null;
        }
        return result;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public LocationDTO search(@RequestParam String lng, @RequestParam String lat,
                              @RequestParam(required = false) String format, HttpServletResponse response) throws Exception {
        Location l = locationService.findByLonAndLat(Double.valueOf(lng), Double.valueOf(lat));
        LocationDTO result = l != null ? toDTO(l, locationService.isInUs(l.getId())) : null;
        if (null != format && format.equals("xls")) {
            toXLS(response, Collections.singletonList(result));
            return null;
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> save(@RequestBody LocationDTO location, UriComponentsBuilder uriComponentsBuilder) {
        Location l = locationService.save(new Location(location.getLng(), location.getLat()));
        
        UriComponents uriComponents = uriComponentsBuilder
            .path(BASE_URI + "/search?lng={lng}&lat={lat}")
            .buildAndExpand(l.getLng(), l.getLat());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    private LocationDTO toDTO(Location location, boolean inUSA) {
        LocationDTO dto = new LocationDTO();
        dto.setLat(location.getLat());
        dto.setLng(location.getLng());
        dto.setInUS(inUSA);
        if (!inUSA) {
            dto.setDistances(locationService.findDistances(location.getId()));
        }
        return dto;
    }

    private void toXLS(HttpServletResponse response, List<LocationDTO> locations) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment; filename=result.xls");

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("locations");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("lng");
        header.createCell(1).setCellValue("lat");
        header.createCell(2).setCellValue("In USA");
        int i = 3;
        for (String city : cities) {
            header.createCell(i++).setCellValue("Distance from " + city.replace("_", ", "));
        }

        for (int j = 0; j < locations.size(); j++) {
            Row row = sheet.createRow(j + 1);
            row.createCell(0).setCellValue(locations.get(j).getLng());
            row.createCell(1).setCellValue(locations.get(j).getLat());
            row.createCell(2).setCellValue(locations.get(j).isInUS());
            if (null != locations.get(j).getDistances()) {
                int k = 3;
                for (Distance distance : locations.get(j).getDistances()) {
                    row.createCell(k++).setCellValue(distance.getDistance());
                }
            }
        }

        wb.write(response.getOutputStream());

    }
}
