package com.gymwebapp.domain;


import javax.persistence.*;

@Entity
@Table(name="Feedback", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class CourseFeedback {

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public CourseFeedback(Course course) {
        this.course = course;
    }

    public CourseFeedback(){

    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
