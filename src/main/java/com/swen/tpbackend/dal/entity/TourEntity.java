package com.swen.tpbackend.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tours")
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Column(nullable = true)
    private double distance; // in meters
    @Column(nullable = true)
    private double duration; // in seconds

    @Column(name="map_image_path")
    private String mapImagePath;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TourLogEntity> logs = new ArrayList<>();


    @Setter
    @Lob
    @Column(name = "geometry", columnDefinition = "TEXT")
    private String geometryJson; // will store as JSON

    @JsonIgnore // Don't send to frontend
    public String getGeometryJson() {
        return geometryJson;
    }

    // Convenience methods for (de)serializing as List<List<Double>>
    @Transient
    public List<List<Double>> getGeometry() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return geometryJson == null ? null : mapper.readValue(geometryJson, new TypeReference<>(){});
        } catch (Exception e) { return null; }
    }

    @Transient
    public void setGeometry(List<List<Double>> geometry) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.geometryJson = mapper.writeValueAsString(geometry);
        } catch (Exception e) {
            this.geometryJson = null;
        }
    }


    @Override
    public String toString() {
        return "TourEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fromLocation='" + fromLocation + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", transportType='" + transportType + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                '}';
    }

}
