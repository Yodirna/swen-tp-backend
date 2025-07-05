package com.swen.tpbackend.dal.dto;

import com.swen.tpbackend.dal.entity.TourEntity;
import com.swen.tpbackend.dal.entity.TourLogEntity;
import org.springframework.stereotype.Component;

@Component
public class TourMapper
{
    public TourMapper() {}
    // in TourService (or extract to a Mapper class)
    public TourEntity toEntity(TourDto dto) {
        TourEntity e = new TourEntity();
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setFromLocation(dto.getFromLocation());
        e.setToLocation(dto.getToLocation());
        e.setTransportType(dto.getTransportType());
        e.setMapImagePath(dto.getMapImagePath());
        return e;
    }

    public TourDto toDto(TourEntity e) {
        TourDto dto = new TourDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setDescription(e.getDescription());
        dto.setFromLocation(e.getFromLocation());
        dto.setToLocation(e.getToLocation());
        dto.setTransportType(e.getTransportType());
        dto.setMapImagePath(e.getMapImagePath());
        return dto;
    }

    public TourLogEntity toLogEntity(TourLogDto dto) {
        TourLogEntity e = new TourLogEntity();
        e.setId(dto.getId());
        e.setDateTime(dto.getDateTime());
        e.setRating(dto.getRating());
        e.setDifficulty(dto.getDifficulty());
        e.setTotalTime(dto.getTotalTime());
        e.setTotalDistance(dto.getTotalDistance());
        e.setComment(dto.getComment());
        return e;
    }

    public TourLogDto mapToDto(TourLogEntity log) {
        TourLogDto dto = new TourLogDto(log.getId(),log.getDateTime(),log.getRating(),log.getDifficulty(),log.getTotalTime(),log.getTotalDistance(),log.getComment());
        return dto;
    }


}
