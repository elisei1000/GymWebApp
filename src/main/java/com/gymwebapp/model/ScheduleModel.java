package com.gymwebapp.model;

import java.util.Date;

/**
 * Created by vasi on 06.01.2018.
 */
public class ScheduleModel {
    private Date startDate;
    private Date endDate;

    public ScheduleModel() {
    }

    public ScheduleModel(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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
