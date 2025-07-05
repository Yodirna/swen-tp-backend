package com.swen.tpbackend.dal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tours")
public class TourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name="from_location")
    private String fromLocation;

    @Column(name="to_location")
    private String toLocation;

    private String transportType;

    private double distance; // in meters
    private double duration; // in seconds

    @Column(name="map_image_path")
    private String mapImagePath;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TourLogEntity> logs = new ArrayList<>();

}
