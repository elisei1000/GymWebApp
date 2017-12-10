package com.gymwebapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class SubscriptionTest {

    private Subscription subscription;
    private Date startDate;
    private Date endDate;

    @Before
    public void setUp() throws Exception {
        startDate = new Date();
        endDate = new Date();
        subscription = new Subscription(1,startDate, endDate);
    }

    @Test
    public void getId() {
        assertEquals(subscription.getId().toString(),"1");
    }

    @Test
    public void setId() {
        subscription.setId(2);
        assertEquals(subscription.getId().toString(),"2");
    }

    @Test
    public void getStartDate() {
        assertEquals(subscription.getStartDate(),startDate);
    }

    @Test
    public void setStartDate() throws ParseException {
        String target = "Thu Sep 28 20:29:30 JST 2000";
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date testDate = df.parse(target);
        subscription.setStartDate(testDate);
        assertEquals(subscription.getStartDate().toString(),"Thu Sep 28 14:29:30 EEST 2000");
    }

    @Test
    public void getEndDate() {
        assertEquals(subscription.getEndDate(),endDate);
    }

    @Test
    public void setEndDate() throws ParseException {
        String target = "Thu Sep 28 20:29:30 JST 2000";
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date testDate = df.parse(target);
        subscription.setEndDate(testDate);
        assertEquals(subscription.getEndDate().toString(),"Thu Sep 28 14:29:30 EEST 2000");
    }
}