package com.gymwebapp.domain;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by elisei on 20.11.2017.
 */
@Entity
@Table(name="Coach", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Coach extends User {

//    private List<Feedback> feedbacks;

    public Coach(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
//        this.feedbacks = new ArrayList<>();
    }

    public Coach(){

    }

//    public List<Feedback> getFeedbacks(){return this.feedbacks;}

//    public void setFeedbacks(List<Feedback> feedbacks){this.feedbacks = feedbacks;}


}
