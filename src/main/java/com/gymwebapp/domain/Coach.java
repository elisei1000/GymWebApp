package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="Coach", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Coach extends User {

    @Column(name = "about")
    private String about;

    @OneToMany(mappedBy = "coach", orphanRemoval = true)
    private List<CoachFeedback> feedbacks;

    public Coach(String username, String password, String email, String name, Date birthDay, String about) {
        super(username, password, email, name, birthDay);
        this.about = about;
        this.feedbacks = new ArrayList<>();
    }

    public Coach(String username, String password){
        super(username, password);
    }

    @Override
    public String toString() {
        return super.toString() + "Coach{" +
                "feedbacks=" + feedbacks +
                '}';
    }

    public Coach(){

    }

    public List<CoachFeedback> getFeedbacks(){return this.feedbacks;}

    public void setFeedbacks(List<CoachFeedback> feedbacks){this.feedbacks = feedbacks;}


    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
