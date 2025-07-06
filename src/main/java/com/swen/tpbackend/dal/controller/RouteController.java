package com.swen.tpbackend.dal.controller;

import com.swen.tpbackend.business.RouteService;
import com.swen.tpbackend.dal.dto.RouteDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // Example: POST /api/routes
    @PostMapping
    public ResponseEntity<RouteDto> getRoute(@RequestBody RouteRequest request) {
        RouteDto route = routeService.getRoute(request.getFrom(), request.getTo(), request.getTransportType());
        return ResponseEntity.ok(route);
    }

    // Inner static class for request body
    @Setter
    @Getter
    public static class RouteRequest {
        // getters and setters
        private String from;
        private String to;
        private String transportType; // driving-car, foot-walking, etc.

    }
}

