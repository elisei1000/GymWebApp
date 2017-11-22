package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Feedback", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class CoachFeedback extends Feedback {

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Coach coach;

    public CoachFeedback() {

    }

    public CoachFeedback(int id, int starsCount, String summary, String details, Date date, Client author, Coach coach) {
        super(id, starsCount, summary, details, date, author);
        this.coach = coach;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

}
