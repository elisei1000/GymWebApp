package com.gymwebapp.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
@Table(name="Client", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Client extends User {

    @OneToOne
    private Subscription subscription;


    @ManyToMany
    @JoinTable(
            name = "Course_participations"
            , joinColumns = { @JoinColumn(name = "username") }
            , inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    private List<Course> courses;

    public Client(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
        this.courses = new ArrayList<>();

    }

    public Client(String username, String password, String email, String name, Date birthDay, Subscription subscription) {
        super(username, password, email, name, birthDay);
        this.subscription = subscription;
        this.courses = new ArrayList<>();

    }

    public Client(){

    }

    public List<Course> getCourses(){return this.courses;}

    public void setCourses(List<Course> courses){this.courses = courses;}

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

//   public List<Feedback> getFeedbacks(){return this.feedbacks;}
//
//    public void setFeedbacks(List<Feedback> feedbacks){this.feedbacks = feedbacks;}
}
