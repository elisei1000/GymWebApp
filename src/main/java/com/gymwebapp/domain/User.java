package com.gymwebapp.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by david on 18.11.2017.
 */
@Entity
@Table(name="User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="username", unique = true)
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="birthday")
    private Date birthDay;

    public User(){
    }

    public User(Integer id,String username, String password){
        this.id=id;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String email, String name, Date birthDay) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDay = birthDay;
    }

    public User(Integer id,String username, String password, String email, String name, Date birthDay) {
        this.id=id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDay = birthDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
