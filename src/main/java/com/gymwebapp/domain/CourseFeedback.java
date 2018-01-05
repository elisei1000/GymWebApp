package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="CourseFeedback", uniqueConstraints = @UniqueConstraint(columnNames = {"feedback_id"}))
public class CourseFeedback extends Feedback{

    @OneToOne
    @JoinColumn(name="course_id", referencedColumnName = "course_id")
    private Course course;

    public CourseFeedback(int starsCount, String summary, String details, Date date, Client author, Course course) {
        super(starsCount, summary, details, date, author);
        this.course = course;
    }

    public CourseFeedback(int starsCount, String summary, String details, Date date, Client author) {
        super(starsCount, summary, details, date, author);
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
