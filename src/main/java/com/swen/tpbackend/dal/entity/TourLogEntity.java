package com.swen.tpbackend.dal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "tour_logs")
public class TourLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    @JsonBackReference
    private TourEntity tour;

    private LocalDateTime dateTime;
    private int rating;
    private String difficulty;
    private double totalTime;
    private double totalDistance;

    @Column(length = 1024)
    private String comment;

}
