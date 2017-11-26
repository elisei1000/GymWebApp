package com.gymwebapp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Feedback")
@Inheritance(strategy= InheritanceType.JOINED)
public class Feedback implements HasId<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feedback_id")
    private Integer id;

    @Column(name = "starsCount")
    private int starsCount;

    @Column(name = "summary")
    private String summary;

    @Column(name = "details")
    private String details;

    @Column(name = "date")
    private Date date;

    @OneToOne
    private Client author;

    public Feedback(){

    }

    public Feedback( int starsCount, String summary, String details, Date date, Client author) {
        this.starsCount = starsCount;
        this.summary = summary;
        this.details = details;
        this.date = date;
        this.author = author;
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

    public Client getAuthor() {
        return author;
    }

    public void setAuthor(Client author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", starsCount=" + starsCount +
                ", summary='" + summary + '\'' +
                ", details='" + details + '\'' +
                ", date=" + date +
                ", author=" + author +
                '}';
    }
}
