package com.gymwebapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class UserTest {

    private Date date;
    private User user;

    @Before
    public void setUp() throws Exception {
        date = new Date();
        user = new User("u1","p1","e1","n1", date);
    }

    @Test
    public void getId() {
        assertEquals(user.getId(),"u1");
    }

    @Test
    public void setId() {
        user.setId("u2");
        assertEquals(user.getId(),"u2");
    }

    @Test
    public void getUsername() {
        assertEquals(user.getUsername(),"u1");
    }

    @Test
    public void setUsername() {
        user.setUsername("u2");
        assertEquals(user.getUsername(),"u2");
    }

    @Test
    public void getPassword() {
        assertEquals(user.getPassword(),"p1");
    }

    @Test
    public void setPassword() {
        user.setPassword("p2");
        assertEquals(user.getPassword(),"p2");
    }

    @Test
    public void getEmail() {
        assertEquals(user.getEmail(),"e1");
    }

    @Test
    public void setEmail() {
        user.setEmail("e2");
        assertEquals(user.getEmail(),"e2");
    }

    @Test
    public void getName() {
        assertEquals(user.getName(),"n1");
    }

    @Test
    public void setName() {
        user.setName("n2");
        assertEquals(user.getName(),"n2");
    }

    @Test
    public void getBirthDay() {
        assertEquals(user.getBirthDay(),date);
    }

    @Test
    public void setBirthDay() throws ParseException {
        String target = "Thu Sep 28 20:29:30 JST 2000";
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date testDate = df.parse(target);
        user.setBirthDay(testDate);
        assertEquals(user.getBirthDay().toString(),"Thu Sep 28 14:29:30 EEST 2000");
    }
}