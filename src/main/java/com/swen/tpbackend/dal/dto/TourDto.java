package com.swen.tpbackend.dal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourDto {
    private Long id;
    private String name;
    private String description;
    private String mapImagePath;
    private String transportType;
    private String fromLocation;
    private String toLocation;
    private double distance;
    private double duration;
    private int popularity;
    private boolean childFriendly;
    @JsonIgnore
    private List<List<Double>> geometry;
}
