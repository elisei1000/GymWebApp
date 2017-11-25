package com.gymwebapp.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course implements HasId<Integer>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Integer id;

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

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

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

    public Course(int difficultyLevel, int startHour, int endHour, Date startDate, Date endDate, int maxPlaces, Coach teacher) {
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
        this.teacher = teacher;
        this.clients = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
    }

    public Course(String title, String description, int difficultyLevel, int startHour, int endHour, Date startDate, Date endDate, int maxPlaces, Coach teacher) {
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour = endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
        this.teacher = teacher;
        this.clients = new ArrayList<>();
        this.feedbacks = new ArrayList<>();
        this.title = title;
        this.description = description;
    }


    public Course(int difficultyLevel, int startHour, int endHour, Date startDate, Date endDate, int maxPlaces){
        this.difficultyLevel = difficultyLevel;
        this.startHour = startHour;
        this.endHour=endHour;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxPlaces = maxPlaces;
    }

    public void addClient(Client client)
    {
        clients.add(client);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", difficultyLevel=" + difficultyLevel +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", maxPlaces=" + maxPlaces +
                ", teacher=" + teacher +
                ", clients=" + clients +
                ", feedbacks=" + feedbacks +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer course_id) {
        this.id = course_id;
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


    public List<CourseFeedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<CourseFeedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
