package com.gymwebapp.domain;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="Coach", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Coach extends User {

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoachFeedback> feedbacks;

    public Coach(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
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


}
