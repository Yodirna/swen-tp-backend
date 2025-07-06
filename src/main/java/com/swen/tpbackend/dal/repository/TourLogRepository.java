package com.swen.tpbackend.dal.repository;

import com.swen.tpbackend.dal.entity.TourLogEntity;
import com.swen.tpbackend.dal.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourLogRepository extends JpaRepository<TourLogEntity, Long> {
    int countByTourId(Long tourId);
    List<TourLogEntity> findByTourId(Long tourId);
}
