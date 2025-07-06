package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import com.swen.tpbackend.dal.repository.TourLogRepository;
import com.swen.tpbackend.dal.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TourLogServiceTest {

    @Mock private TourLogRepository logRepo;
    @Mock private TourRepository tourRepository;
    @InjectMocks private TourLogService logService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logService = new TourLogService(logRepo, tourRepository);
    }

    @Test
    void addLog_Success() {
        TourEntity tour = new TourEntity(); tour.setId(2L);
        TourLogEntity log = new TourLogEntity();
        log.setTour(tour);
        when(logRepo.save(any())).thenReturn(log);

        TourLogEntity saved = logService.addLog(tour, LocalDateTime.now(), 5, "2", 120.0, 8.2, "Nice log");

        assertNotNull(saved);
        assertEquals(tour, saved.getTour());
    }

    @Test
    void deleteLog_Success() {
        TourLogEntity log = new TourLogEntity();
        doNothing().when(logRepo).delete(log);
        assertDoesNotThrow(() -> logService.deleteLog(log));
        verify(logRepo).delete(log);
    }
}
