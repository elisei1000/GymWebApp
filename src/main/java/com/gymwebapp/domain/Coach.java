package com.gymwebapp.domain;



import javax.persistence.*;
import java.util.Date;

/**
 * Created by elisei on 20.11.2017.
 */
@Entity
@Table(name="Coach", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Coach extends User {

    public Coach(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
    }

    public Coach(){

    }


    static private Coach empty(){
        return new Coach();
    }

}
