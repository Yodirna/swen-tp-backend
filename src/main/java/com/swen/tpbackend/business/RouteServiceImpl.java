package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.RouteDto;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {


    @Setter
    @Value("${openrouteservice.api-key}")
    private String orsApiKey;

    private final RestTemplate restTemplate;

    // Add a constructor for injection
    public RouteServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String ORS_URL = "https://api.openrouteservice.org/v2/directions/{profile}/geojson";
    private static final String ORS_GEOCODE_URL = "https://api.openrouteservice.org/geocode/search";

    @Override
    public RouteDto getRoute(String from, String to, String transportType) {
        // Step 1: Geocode addresses to coordinates
        List<Double> fromCoords = geocodeAddress(from);
        List<Double> toCoords = geocodeAddress(to);

        // Step 2: Call directions API
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", orsApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("coordinates", Arrays.asList(fromCoords, toCoords));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String profile = mapTransportType(transportType);
        ResponseEntity<Map> response = restTemplate.exchange(
                ORS_URL, HttpMethod.POST, entity, Map.class, profile
        );

        Map feature = (Map) ((List)((Map)response.getBody()).get("features")).get(0);
        Map properties = (Map) feature.get("properties");
        double distance = ((Number)((Map)((List)properties.get("segments")).get(0)).get("distance")).doubleValue();
        double duration = ((Number)((Map)((List)properties.get("segments")).get(0)).get("duration")).doubleValue();

        Map geometry = (Map) feature.get("geometry");
        List<List<Double>> coordinates = (List<List<Double>>) geometry.get("coordinates");

        return RouteDto.builder()
                .distance(distance)
                .duration(duration)
                .geometry(coordinates)
                .build();
    }

    private String mapTransportType(String input) {
        if (input == null) return "driving-car";
        switch (input.toLowerCase()) {
            case "car":
            case "driving-car":
                return "driving-car";
            case "hgv":
            case "driving-hgv":
                return "driving-hgv";
            case "cycling":
            case "cycling-regular":
            case "bike":
                return "cycling-regular";
            case "foot":
            case "walking":
            case "foot-walking":
                return "foot-walking";
            default:
                return "driving-car";
        }
    }


    private List<Double> geocodeAddress(String address) {
        String url = ORS_GEOCODE_URL + "?api_key=" + orsApiKey + "&text=" + address;
        Map result = restTemplate.getForObject(url, Map.class);
        List features = (List) result.get("features");
        if (features.isEmpty()) throw new RuntimeException("Address not found: " + address);
        Map geometry = (Map) ((Map)features.get(0)).get("geometry");
        return (List<Double>) geometry.get("coordinates"); // [lng, lat]
    }


}
