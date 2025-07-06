// TourController.java
package com.swen.tpbackend.dal.controller;

import com.swen.tpbackend.business.TourService;
import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.dal.dto.TourMapper;
import com.swen.tpbackend.dal.entity.TourEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.net.URI;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;
    private static final Logger logger = LogManager.getLogger(TourService.class);

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<TourDto> getTour(@PathVariable("tourId") Long tourId) {
        TourEntity entity = tourService.getTour(tourId);
        int popularity = tourService.computePopularity(tourId);
        boolean childFriendly = tourService.isChildFriendly(tourId);
        // map entity → change to use TourMapper later
        TourDto dto = TourDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .mapImagePath(entity.getMapImagePath())
                .transportType(entity.getTransportType())
                .fromLocation(entity.getFromLocation())
                .toLocation(entity.getToLocation())
                .popularity(popularity)
                .childFriendly(childFriendly)
                .build();
        logger.info("Sending Tour to frontend: " + dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{tourId}")
    public ResponseEntity<TourDto> updateTour(@PathVariable("tourId") Long tourId, @RequestBody TourDto tourDto) {
        // TourMapper instance to convert DTO into Entity
        TourMapper mapper = new TourMapper();
        TourEntity entity = mapper.toEntity(tourDto);
        // ID from pathvariable
        entity.setId(tourId);
        tourService.updateTour(entity);
        int popularity = tourService.computePopularity(entity.getId());
        boolean childFriendly = tourService.isChildFriendly(entity.getId());
        return ResponseEntity.ok(mapper.toDto(entity, popularity, childFriendly));
    }

    @PostMapping("/add")
    public ResponseEntity<TourDto> createTour(@RequestBody TourDto tourDto) {
        // Save the tour
        TourDto created = tourService.addTour(tourDto);

        // Build URI: /tours/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()             // takes "/tours"
                .path("/{id}")                    // appends "/{id}"
                .buildAndExpand(created.getId())  // replaces {id}
                .toUri();


        logger.info("Sending Tour to frontend: " + created);
        // Return 201 Created with Location header + body
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTours() {
        List<TourEntity> entities = tourService.getAllTours();
        TourMapper mapper = new TourMapper();
        List<TourDto> dtos = entities.stream()
                .map(entity -> {
                    int popularity = tourService.computePopularity(entity.getId());
                    boolean childFriendly = tourService.isChildFriendly(entity.getId());
                    return mapper.toDto(entity, popularity, childFriendly);
                })
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<TourDto> deleteTour(@PathVariable Long tourId) {
        tourService.deleteTour(tourId);
        return ResponseEntity.noContent().build();
    }


}
