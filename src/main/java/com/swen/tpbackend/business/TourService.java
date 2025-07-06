package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.dal.dto.TourMapper;
import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import com.swen.tpbackend.dal.repository.TourLogRepository;
import com.swen.tpbackend.dal.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Setter
@Getter
@Service
public class TourService {
    private final TourRepository tourRepository;
    private TourLogRepository tourLogRepository;
    private static final Logger logger = LogManager.getLogger(TourService.class);

    @Autowired
    public TourService(TourRepository tourRepository, TourLogRepository tourLogRepository) {
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogRepository;
    }

    public TourEntity getTour(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found"));
    }

    public TourDto addTour(TourDto tourDto) {
        TourMapper mapper = new TourMapper();
        // 1) map DTO → Entity
        TourEntity toSave = mapper.toEntity(tourDto);
        logger.info("Saving tour... ");

        // 2) save Entity
        TourEntity saved = tourRepository.save(toSave);
        logger.info("Saved tour: " + saved);

        // Print
        System.out.println(saved); // For Debugging

        // 3) map back to DTO and return
        return mapper.toDto(saved);
    }

    public TourEntity updateTour(TourEntity updated) {
        // Optionally: verify it exists first
        if (!tourRepository.existsById(updated.getId())) {
            throw new EntityNotFoundException("Cannot update non-existent tour");
        }
        logger.info("Updating tour " + updated);
        return tourRepository.save(updated);
    }

    public List<TourEntity> getAllTours() {
        logger.info("Getting all tours");
        return tourRepository.findAll();
    }

    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour not found");
        }
        logger.info("Deleting tour " + id);
        tourRepository.deleteById(id);
    }

    public int computePopularity(Long tourId) {
        return tourLogRepository.countByTourId(tourId);
    }

    public boolean isChildFriendly(Long tourId) {
        List<TourLogEntity> logs = tourLogRepository.findByTourId(tourId);
        if (logs.isEmpty()) return false; // Or some sensible default

        double avgDifficulty = logs.stream().mapToInt(log -> Integer.parseInt(log.getDifficulty())).average().orElse(0);
        double avgDuration = logs.stream().mapToDouble(TourLogEntity::getTotalTime).average().orElse(0);
        double distance = tourRepository.findById(tourId).map(TourEntity::getDistance).orElse(0.0);

        // Example rules:
        return avgDifficulty <= 2 && avgDuration <= 120 && distance <= 5000; // Customize thresholds
    }



    public List<TourEntity> searchTours(String searchText) {
        List<TourEntity> allTours = tourRepository.findAll();
        return allTours.stream().filter(tour -> {
            String haystack = (tour.getName() + " " +
                    tour.getDescription() + " " +
                    (computePopularity(tour.getId()) > 10 ? "popular" : "") + " " +
                    (isChildFriendly(tour.getId()) ? "child friendly" : "")
            ).toLowerCase();
            return haystack.contains(searchText.toLowerCase());
        }).collect(Collectors.toList());
    }

}

