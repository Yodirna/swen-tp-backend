package com.swen.tpbackend.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
