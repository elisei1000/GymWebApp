package com.gymwebapp.model;

import java.util.Date;

public class CoachModel extends UserModel {

    private String about;

    CoachModel() {
    }

    public CoachModel(Integer id, String username, String password, String email, String name, Date birthDay, String about) {
        super(id, username, password, email, name, birthDay);
        this.about = about;
    }

    public CoachModel(String username, String password, String email, String name, Date birthDay, String about) {
        super(username, password, email, name, birthDay);
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
