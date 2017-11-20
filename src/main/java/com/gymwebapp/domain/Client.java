package com.gymwebapp.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by elisei on 20.11.2017.
 */
@Entity
@Table(name="Client", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class Client extends User {

    public Client(String username, String password, String email, String name, Date birthDay) {
        super(username, password, email, name, birthDay);
    }

    public Client(){

    }
}
