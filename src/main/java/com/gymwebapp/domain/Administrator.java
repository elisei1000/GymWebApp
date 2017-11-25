package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Administrator", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Administrator extends User{

    public Administrator(String username, String password, String email, String name, Date birthDay){
        super(username, password, email, name, birthDay);
    }

}
