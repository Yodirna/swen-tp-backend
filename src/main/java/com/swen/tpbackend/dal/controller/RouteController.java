package com.swen.tpbackend.dal.controller;

import com.swen.tpbackend.business.RouteService;
import com.swen.tpbackend.dal.dto.RouteDto;
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
    public static class RouteRequest {
        private String from;
        private String to;
        private String transportType; // driving-car, foot-walking, etc.
        // getters and setters
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getTransportType() { return transportType; }
        public void setTransportType(String transportType) { this.transportType = transportType; }
    }
}

