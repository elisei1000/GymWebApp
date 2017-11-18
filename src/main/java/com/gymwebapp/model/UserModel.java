package com.gymwebapp.model;

import java.util.Date;

public class UserModel {

    private Integer id;
    private String username;
    private String password;
    private String email;
    private String name;
    private Date birthDay;

    public UserModel(){
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserModel(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserModel(String username, String password, String email, String name, Date birthDay) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.birthDay = birthDay;
    }

    public UserModel(Integer id, String username, String password, String email, String name, Date birthDay) {
        this.id = id;
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
