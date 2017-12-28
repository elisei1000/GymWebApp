package com.gymwebapp.model;

import java.util.Date;

/**
 * Created by vasi on 05.12.2017.
 */
public class FeedbackModel {
    private Integer id;
    private int starsCount;
    private String summary;
    private String details;
    private Date date;
    private String author;

    public FeedbackModel() {
    }

    public FeedbackModel(Integer id, int starsCount, String summary, String details, Date date, String author) {
        this.id = id;
        this.starsCount = starsCount;
        this.summary = summary;
        this.details = details;
        this.date = date;
        this.author = author;
    }

    public FeedbackModel(int starsCount, String summary, String details) {
        this.starsCount = starsCount;
        this.summary = summary;
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
