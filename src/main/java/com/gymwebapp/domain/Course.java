package com.gymwebapp.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue
    @Column(name = "course_id")
    private int course_id;

    @Column(name="difiicultyLevel")
    private int difficultyLevel;

    @Column(name = "startHour")
    private int startHour;

    @Column(name = "endHour")
    private int endHour;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "maxPlaces")
    private int maxPlaces;

    @ManyToOne
    private Coach teacher;

    @ManyToMany
    @JoinTable(
            name = "Course_participations"
            , joinColumns = { @JoinColumn(name = "course_id") }
            , inverseJoinColumns = { @JoinColumn(name = "username") }
    )
    private List<Client> clients;


    @OneToMany(mappedBy = "course")
    private List<CourseFeedback> feedbacks;
    public Course(){

    }

    public Course(int course_id, int difficultyLevel, int startHour, int endHour, Date startDate, Date endDate, int maxPlaces, Coach teacher) {
        this.course_id = course_id;
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
        this.teacher = teacher;
        this.clients = new ArrayList<>();
        //this.feedbacks = new ArrayList<>();
    }

//    public void addFeedback(CourseFeedback feedback)
//    {
//        feedbacks.add(feedback);
//    }

    public void addClient(Client client)
    {
        clients.add(client);
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
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

    public int getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(int maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public Coach getTeacher() {
        return teacher;
    }

    public void setTeacher(Coach teacher) {
        this.teacher = teacher;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }


//    public List<Feedback> getFeedbacks() {
//        return feedbacks;
//    }
//
//    public void setFeedbacks(List<Feedback> feedbacks) {
//        this.feedbacks = feedbacks;
//    }
}
