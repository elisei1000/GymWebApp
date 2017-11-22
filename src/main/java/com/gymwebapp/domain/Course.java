package com.gymwebapp.domain;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Course")
public class Course {

    @Id
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

    @Column(name = "teacher")
    private Coach teacher;

    @ManyToMany(mappedBy = "courses")
    private List<Client> clients;

    @OneToMany(mappedBy = "course")
    private List<CourseFeedback> feedbacks;


}
