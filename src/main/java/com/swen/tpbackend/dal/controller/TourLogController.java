package com.swen.tpbackend.dal.controller;

import com.swen.tpbackend.business.TourService;
import com.swen.tpbackend.dal.dto.TourLogDto;
import com.swen.tpbackend.business.TourLogService;
import com.swen.tpbackend.dal.dto.TourMapper;
import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours/{tourId}/logs")
public class TourLogController {

    private final TourMapper tourMapper;
    private final TourLogService logService;

    public TourLogController(TourLogService logService, TourMapper tourMapper) {
        this.logService = logService;
        this.tourMapper = tourMapper;
    }

    @GetMapping
    public List<TourLogDto> getLogs(@PathVariable Long tourId) {
        TourEntity tour = logService.getTourById(tourId);

        return tour.getLogs().stream()
                .map(tourMapper::mapToDto)//Changed recently
                .toList();
    }

    @PostMapping
    public List<TourLogDto> createLog(@PathVariable Long tourId,
                                      @RequestBody TourLogDto dto) {
        TourEntity tour = logService.getTourById(tourId);

        // Add the new log
        logService.addLog(
                tour,
                dto.getDateTime(),
                dto.getRating(),
                dto.getDifficulty(),
                dto.getTotalTime(),
                dto.getTotalDistance(),
                dto.getComment()
        );

        // Map and return all logs for the tour
        return tour.getLogs().stream()
                .map(tourMapper::mapToDto) //Changed recently
                .toList();
    }


    @PutMapping("/{logId}")
    public TourLogDto updateLog(@PathVariable Long tourId,
                                @PathVariable Long logId,
                                @RequestBody TourLogDto dto) {
        dto.setId(logId);
        TourMapper mapper = new TourMapper();
        TourLogEntity entity = mapper.toLogEntity(dto);
        logService.updateLog(entity);
        return (dto);
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long tourId,
                                          @PathVariable Long logId) {
        TourLogEntity log = logService.getLogById(logId);

        // check that the log belongs to the given tour
        if (!log.getTour().getId().equals(tourId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // or throw a custom exception
        }

        logService.deleteLog(log);
        return ResponseEntity.noContent().build();
    }
}
