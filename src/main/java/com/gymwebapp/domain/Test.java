package com.gymwebapp.domain;


import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by elisei on 15.11.2017.
 */
@Entity
@Table(name="Test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name", unique = true)
    private String name;

    public Integer getId() {
        return id;
    }

    public Test(){

    }

    public Test(String name){

        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
