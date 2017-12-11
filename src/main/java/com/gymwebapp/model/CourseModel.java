package com.gymwebapp.model;

import java.util.Date;

/**
 * Created by david on 30.11.2017.
 */
public class CourseModel {

    private Integer id;
    private Integer difficultyLevel;
    private Integer startHour;
    private Integer endHour;
    private Date startDate;
    private Date endDate;
    private Integer maxPlaces;
    private Integer numberOfParticipants;
    private String teacher;
    private String title;
    private String description;

    public CourseModel() {
    }

    public CourseModel(Integer id, Integer difficultyLevel, Integer startHour, Integer endHour, Date startDate, Date endDate, Integer maxPlaces) {
        this.id = id;
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
    }

    public CourseModel(Integer id, Integer difficultyLevel, Integer startHour, Integer endHour, Date startDate, Date endDate, Integer maxPlaces, Integer numberOfParticipants, String teacher, String title, String description) {
        this.id = id;
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
        this.numberOfParticipants = numberOfParticipants;
        this.teacher = teacher;
        this.title = title;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
