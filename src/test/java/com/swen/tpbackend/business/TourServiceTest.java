package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.dal.dto.TourMapper;
import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import com.swen.tpbackend.dal.repository.TourLogRepository;
import com.swen.tpbackend.dal.repository.TourRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourServiceTest {

    @Mock private TourRepository tourRepository;
    @Mock private TourLogRepository tourLogRepository;
    @InjectMocks private TourService tourService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourService = new TourService(tourRepository, tourLogRepository);
    }

    @Test
    void getTour_Success() {
        TourEntity entity = new TourEntity();
        entity.setId(1L);
        when(tourRepository.findById(1L)).thenReturn(Optional.of(entity));
        TourEntity found = tourService.getTour(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void getTour_NotFound() {
        when(tourRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> tourService.getTour(1L));
    }

    @Test
    void addTour_Success() {
        TourDto dto = TourDto.builder().name("Test Tour").build();
        TourEntity entity = new TourMapper().toEntity(dto);
        TourEntity savedEntity = new TourEntity();
        savedEntity.setId(42L); savedEntity.setName("Test Tour");
        when(tourRepository.save(any())).thenReturn(savedEntity);

        TourDto result = tourService.addTour(dto);

        assertEquals("Test Tour", result.getName());
        assertEquals(42L, result.getId());
    }

    @Test
    void updateTour_Success() {
        TourEntity updated = new TourEntity();
        updated.setId(5L);
        when(tourRepository.existsById(5L)).thenReturn(true);
        when(tourRepository.save(updated)).thenReturn(updated);
        TourEntity result = tourService.updateTour(updated);
        assertEquals(5L, result.getId());
    }

    @Test
    void updateTour_NotFound() {
        TourEntity updated = new TourEntity();
        updated.setId(5L);
        when(tourRepository.existsById(5L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> tourService.updateTour(updated));
    }

    @Test
    void deleteTour_Success() {
        when(tourRepository.existsById(11L)).thenReturn(true);
        doNothing().when(tourRepository).deleteById(11L);
        assertDoesNotThrow(() -> tourService.deleteTour(11L));
        verify(tourRepository).deleteById(11L);
    }

    @Test
    void deleteTour_NotFound() {
        when(tourRepository.existsById(22L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> tourService.deleteTour(22L));
    }

    @Test
    void computePopularity_CalculatesCorrectly() {
        when(tourLogRepository.countByTourId(7L)).thenReturn(4);
        int pop = tourService.computePopularity(7L);
        assertEquals(4, pop);
    }

    @Test
    void isChildFriendly_True() {
        TourLogEntity log = new TourLogEntity();
        log.setDifficulty("1");
        log.setTotalTime(60.0);
        TourEntity tour = new TourEntity();
        tour.setDistance(4000.0);
        tour.setId(123L);

        when(tourLogRepository.findByTourId(123L)).thenReturn(List.of(log));
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tour));
        assertTrue(tourService.isChildFriendly(123L));
    }

    @Test
    void isChildFriendly_False_HighDifficulty() {
        TourLogEntity log = new TourLogEntity();
        log.setDifficulty("4"); // too high
        log.setTotalTime(80.0);
        TourEntity tour = new TourEntity();
        tour.setDistance(4000.0);
        tour.setId(124L);

        when(tourLogRepository.findByTourId(124L)).thenReturn(List.of(log));
        when(tourRepository.findById(124L)).thenReturn(Optional.of(tour));
        assertFalse(tourService.isChildFriendly(124L));
    }

    @Test
    void isChildFriendly_False_NoLogs() {
        when(tourLogRepository.findByTourId(125L)).thenReturn(List.of());
        assertFalse(tourService.isChildFriendly(125L));
    }

    @Test
    void searchTours_ByNameDescription() {
        TourEntity t1 = new TourEntity(); t1.setId(1L); t1.setName("Alpine Adventure"); t1.setDescription("Mountain fun");
        TourEntity t2 = new TourEntity(); t2.setId(2L); t2.setName("City Walk"); t2.setDescription("urban tour");
        when(tourRepository.findAll()).thenReturn(List.of(t1, t2));
        when(tourLogRepository.countByTourId(any())).thenReturn(0);
        when(tourLogRepository.findByTourId(any())).thenReturn(List.of());

        List<TourEntity> result = tourService.searchTours("alpine");
        assertEquals(1, result.size());
        assertEquals("Alpine Adventure", result.get(0).getName());

        result = tourService.searchTours("urban");
        assertEquals(1, result.size());
        assertEquals("City Walk", result.get(0).getName());
    }
}
