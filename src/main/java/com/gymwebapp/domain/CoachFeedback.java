package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="CoachFeedback", uniqueConstraints = @UniqueConstraint(columnNames = {"feedback_id"}))
public class CoachFeedback extends Feedback {

    @OneToOne
    @JoinColumn(name="coach_username", referencedColumnName = "username")
    private Coach coach;

    public CoachFeedback() {

    }

    public CoachFeedback( int starsCount, String summary, String details, Date date, Client author, Coach coach) {
        super( starsCount, summary, details, date, author);
        this.coach = coach;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

}
