package com.gymwebapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CoachTest {
    private Coach coach;
    private Client author;
    private Date date;

    @Before
    public void setUp() throws Exception {
        date = new Date();
        coach = new Coach("u1","p1","e1","n1", date);
        author = new Client("u2","p2","e2","n2",date);
        CoachFeedback f1 = new CoachFeedback(1,"s1","d1", date, author, coach);
        coach.getFeedbacks().add(f1);
    }

    @Test
    public void getFeedBacks() {
        assertEquals(coach.getFeedbacks().get(0).getDetails(),"d1");
    }

    @Test
    public void setFeedBacks() {
        List<CoachFeedback> feedBacksTest = new ArrayList<>();
        CoachFeedback f1 = new CoachFeedback(1,"s1","d1", date, author, coach);
        CoachFeedback f2 = new CoachFeedback(2,"s2","d2", date, author, coach);
        feedBacksTest.add(f1);
        feedBacksTest.add(f2);
        assertEquals(coach.getFeedbacks().size(), 1);
        coach.setFeedbacks(feedBacksTest);
        assertEquals(coach.getFeedbacks().size(), 2);
        assertEquals(coach.getFeedbacks().get(1).getDetails(), "d2");
    }
}