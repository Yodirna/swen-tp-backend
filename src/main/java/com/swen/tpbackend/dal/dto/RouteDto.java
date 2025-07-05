package com.swen.tpbackend.dal.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RouteDto {
    private double distance; // in meters
    private double duration; // in seconds
    private List<List<Double>> geometry; // List of [lng, lat]
}
