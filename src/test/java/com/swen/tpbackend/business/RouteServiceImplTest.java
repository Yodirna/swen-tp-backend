package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.RouteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteServiceImplTest {

    @Mock private RestTemplate restTemplate;
    private RouteServiceImpl routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        routeService = new RouteServiceImpl(restTemplate);
        routeService.setOrsApiKey("fakekey");
    }

    @Test
    void getRoute_SuccessfulCalculation() {
        // Mock geocode responses
        Map<String, Object> geoResult = new HashMap<>();
        geoResult.put("features", List.of(
                Map.of("geometry", Map.of("coordinates", List.of(16.37, 48.21)))
        ));
        when(restTemplate.getForObject(contains("Vienna"), eq(Map.class))).thenReturn(geoResult);
        when(restTemplate.getForObject(contains("Prague"), eq(Map.class))).thenReturn(geoResult);

        // Mock directions response
        Map<String, Object> segment = Map.of("distance", 250000.0, "duration", 7200.0);
        Map<String, Object> properties = Map.of("segments", List.of(segment));
        Map<String, Object> geometry = Map.of("coordinates", List.of(List.of(16.37, 48.21), List.of(14.42, 50.08)));
        Map<String, Object> feature = Map.of("properties", properties, "geometry", geometry);
        Map<String, Object> body = Map.of("features", List.of(feature));
        ResponseEntity<Map> directionsResponse = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(Map.class), any(Object[].class)))
                .thenReturn(directionsResponse);

        RouteDto dto = routeService.getRoute("Vienna", "Prague", "car");

        assertNotNull(dto);
        assertEquals(250000.0, dto.getDistance());
        assertEquals(7200.0, dto.getDuration());
        assertEquals(2, dto.getGeometry().size());
    }
}
