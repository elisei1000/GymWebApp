package com.gymwebapp.util;

import java.util.List;

public class Data {

    private Status status;
    private List<String> errors;

    public Data(){

    }

    public Data(Status status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
