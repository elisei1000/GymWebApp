package com.gymwebapp.domain;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="Coach", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Coach extends User {

    @OneToMany(mappedBy = "coach")
    private List<CoachFeedback> feedbacks;

    public Coach(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
        this.feedbacks = new ArrayList<>();
    }

    public Coach(){

    }

    public List<CoachFeedback> getFeedbacks(){return this.feedbacks;}

    public void setFeedbacks(List<CoachFeedback> feedbacks){this.feedbacks = feedbacks;}


}
