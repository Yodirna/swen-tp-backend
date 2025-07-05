package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.dal.dto.TourMapper;
import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.List;

@Setter
@Getter
@Service
public class TourService {
    private final TourRepository tourRepository;
    public TourService(TourRepository repo) { this.tourRepository = repo; }

    public TourEntity getTour(Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found"));
    }

    public TourDto addTour(TourDto tourDto) {
        TourMapper mapper = new TourMapper();
        // 1) map DTO → Entity
        TourEntity toSave = mapper.toEntity(tourDto);

        // 2) save Entity
        TourEntity saved = tourRepository.save(toSave);

        // 3) map back to DTO and return
        return mapper.toDto(saved);
    }

    public TourEntity updateTour(TourEntity updated) {
        // Optionally: verify it exists first
        if (!tourRepository.existsById(updated.getId())) {
            throw new EntityNotFoundException("Cannot update non-existent tour");
        }
        return tourRepository.save(updated);
    }

    public List<TourEntity> getAllTours() {
        return tourRepository.findAll();
    }

    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new EntityNotFoundException("Tour not found");
        }
        tourRepository.deleteById(id);
    }

}

