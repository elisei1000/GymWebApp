package com.gymwebapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @author = Nimrod Foldvari
 */
public class SubscriptionModel {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
