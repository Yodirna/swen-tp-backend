package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import com.swen.tpbackend.dal.repository.TourLogRepository;
import com.swen.tpbackend.dal.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TourLogService {
    private final TourLogRepository logRepo;
    private final TourRepository tourRepository;

    @Autowired
    public TourLogService(TourLogRepository logRepo, TourRepository tourRepository) {
        this.logRepo = logRepo;
        this.tourRepository = tourRepository;
    }

    /** Add a new log to a tour */
    public TourLogEntity addLog(TourEntity tour,
                                LocalDateTime dateTime,
                                int rating,
                                String difficulty,
                                double totalTime,
                                double totalDistance,
                                String comment)
    {
        TourLogEntity log = new TourLogEntity();
        log.setTour(tour);
        log.setDateTime(dateTime);
        log.setRating(rating);
        log.setDifficulty(difficulty);
        log.setTotalTime(totalTime);
        log.setTotalDistance(totalDistance);
        log.setComment(comment);
        return logRepo.save(log);
    }

    /** Update an existing log */
    public TourLogEntity updateLog(TourLogEntity log) {
        // assume fields already changed on the entity
        return logRepo.save(log);
    }



    /** Remove a log */
    public void deleteLog(TourLogEntity log) {
        logRepo.delete(log);
    }

    /**
     * List all logs for a tour
     */
    public Optional<TourLogEntity> getLogsForTour(Long tourId) {
        return logRepo.findById(tourId);
    }

    public TourEntity getTourById(Long tourId) {
        return tourRepository.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with ID: " + tourId));
    }

    public TourLogEntity getLogById(Long logId) {
        return logRepo.getById(logId);
    }
}
