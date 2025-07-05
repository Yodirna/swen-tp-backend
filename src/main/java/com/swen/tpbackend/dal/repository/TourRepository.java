package com.swen.tpbackend.dal.repository;

import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.dal.entity.TourEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<TourEntity, Long> {
}
