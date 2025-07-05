package com.swen.tpbackend.dal.dto;

import com.swen.tpbackend.dal.entity.TourLogEntity;

import java.time.LocalDateTime;

public class TourLogDto {
    private Long id;
    private LocalDateTime dateTime;
    private int rating;
    private String difficulty;
    private double totalTime;
    private double totalDistance;
    private String comment;

    public TourLogDto(Long id, LocalDateTime dateTime, int rating, String difficulty,
                      double totalTime, double totalDistance, String comment) {
        this.id = id;
        this.dateTime = dateTime;
        this.rating = rating;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.comment = comment;
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public double getTotalTime() { return totalTime; }
    public void setTotalTime(double totalTime) { this.totalTime = totalTime; }

    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    /** Map a JPA entity to this DTO */
    public static TourLogDto fromEntity(TourLogEntity e) {
        return new TourLogDto(
                e.getId(),
                e.getDateTime(),
                e.getRating(),
                e.getDifficulty(),
                e.getTotalTime(),
                e.getTotalDistance(),
                e.getComment()
        );
    }

    /** Create a fresh entity from this DTO (use in POST) */
    public TourLogEntity toEntity() {
        TourLogEntity e = new TourLogEntity();
        // id left null so JPA will generate it
        e.setDateTime(this.dateTime);
        e.setRating(this.rating);
        e.setDifficulty(this.difficulty);
        e.setTotalTime(this.totalTime);
        e.setTotalDistance(this.totalDistance);
        e.setComment(this.comment);
        return e;
    }

    /** Apply this DTO’s fields onto an existing entity (use in PUT) */
    public void updateEntity(TourLogEntity e) {
        e.setDateTime(this.dateTime);
        e.setRating(this.rating);
        e.setDifficulty(this.difficulty);
        e.setTotalTime(this.totalTime);
        e.setTotalDistance(this.totalDistance);
        e.setComment(this.comment);
    }
}
