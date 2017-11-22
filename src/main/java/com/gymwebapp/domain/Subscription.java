package com.gymwebapp.domain;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Subscription")
public class Subscription implements HasId<Integer>{

    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;

    @Column(name="start_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    public Subscription(){

    }

    public Subscription(int id, Date startDate, Date endDate){
        this.id = id;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
