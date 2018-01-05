package com.gymwebapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class FeedbackTest {

    private Feedback feedback;
    private Client author;
    private Date date;
    private Subscription subscription;

    @Before
    public void setUp() throws Exception {
        date = new Date();
        subscription = new Subscription(1,date,date);
        author = new Client("u1","p1","e1","n1",date, subscription);
        feedback = new Feedback(1,"s1","d1", date, author);
    }

    @Test
    public void getId() {
        assertEquals(feedback.getId(), null);
    }

    @Test
    public void setId() {
        feedback.setId(1);
        assertEquals(feedback.getId().toString(), "1");
    }

    @Test
    public void getStarsCount() {
        assertEquals(feedback.getStarsCount(), 1);
    }

    @Test
    public void setStarsCount() {
        feedback.setStarsCount(2);
        assertEquals(feedback.getStarsCount(), 2);
    }

    @Test
    public void getSummary() {
        assertEquals(feedback.getSummary(), "s1");
    }

    @Test
    public void setSummary() {
        feedback.setSummary("s2");
        assertEquals(feedback.getSummary(), "s2");
    }

    @Test
    public void getDetails() {
        assertEquals(feedback.getDetails(), "d1");
    }

    @Test
    public void setDetails() {
        feedback.setDetails("d2");
        assertEquals(feedback.getDetails(), "d2");
    }

    @Test
    public void getDate() {
        assertEquals(feedback.getDate(), date);
    }

    @Test
    public void setDate() throws ParseException {
        String target = "Thu Sep 28 20:29:30 JST 2000";
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date testDate = df.parse(target);
        feedback.setDate(testDate);
        assertEquals(feedback.getDate().toString(),"Thu Sep 28 14:29:30 EEST 2000");
    }

    @Test
    public void getAuthor() {
        assertEquals(feedback.getAuthor().getUsername(), "u1");
    }

    @Test
    public void setAuthor() {
        Client testAuthor = new Client("u2","p2","e2","n2", date, subscription);
        feedback.setAuthor(testAuthor);
        assertEquals(feedback.getAuthor().getUsername(), "u2");
    }
}