package com.gymwebapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ClientTest {
    private Client author;
    private Coach coach;
    private Date testDate;
    private Subscription subscription;

    @Before
    public void setUp() throws Exception {
        testDate = new Date();
        subscription = new Subscription(1,testDate,testDate);
        author = new Client("u1","p1","e1","n1", testDate, subscription);
        coach = new Coach("u1","p1","e1","n1", testDate);
        Course c1 = new Course("t1","d1",1,1,1,testDate,testDate,1, coach);
        author.getCourses().add(c1);
    }

    @Test
    public void getCourses() {
        assertEquals(author.getCourses().size(), 1);
        assertEquals(author.getCourses().get(0).getTitle(), "t1");
    }

    @Test
    public void setCourses() {
        List<Course> testCourses = new ArrayList<>();
        Course c1 = new Course("t1","d1",1,1,1,testDate,testDate,1, coach);
        Course c2 = new Course("t2","d2",2,2,2,testDate,testDate,2, coach);
        testCourses.add(c1);
        testCourses.add(c2);
        author.setCourses(testCourses);
        assertEquals(author.getCourses().size(), 2);
        assertEquals(author.getCourses().get(1).getTitle(), "t2");
    }

    @Test
    public void getSubscription() {
        assertEquals(author.getSubscription().getId().toString(), "1");
    }

    @Test
    public void setSubscription() {
        Subscription testSubscription = new Subscription(2,testDate,testDate);
        author.setSubscription(testSubscription);
        assertEquals(testSubscription.getId().toString(), "2");
    }
}