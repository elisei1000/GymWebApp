package com.gymwebapp.domain;

/**
 * Created by elisei on 20.11.2017.
 */
public interface HasId<ID> {
    ID getId();
    void setId(ID id);
}
