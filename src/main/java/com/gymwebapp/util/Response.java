package com.gymwebapp.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;

public class Response {

    @JsonIgnore
    private Status status;
    private List<String> errors;
    private HashMap<String, Object> data;

    public Response(){

    }

    public Response(Status status, List<String> errors) {
        this.data = new HashMap<>();
        this.data.put(Const.RESPONSE_STATUS_NAME, status);
        this.status = status;
        this.errors = errors;
    }

    public Response(Status status, List<String> errors, Pair<String, Object> ... pairs){
        this.data = new HashMap<>();
        this.data.put(Const.RESPONSE_STATUS_NAME, status);
        this.status = status;
        this.errors = errors;
        for(Pair<String, Object> pair : pairs){
            this.data.put(pair.getFirst(), pair.getSecond());
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HashMap<String, Object> getData(){return data;}
    public void setData(HashMap<String, Object> data){this.data = data;}

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}