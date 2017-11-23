package com.gymwebapp.repository;

import com.gymwebapp.domain.HasId;
import com.gymwebapp.domain.RepositoryException;

import java.util.List;

/**
 * Created by elisei on 20.11.2017.
 */
public interface CrudRepository<E extends HasId<ID>, ID> {
    void add(E entity) throws RepositoryException;
    void update(E entity) throws RepositoryException;
    void remove(ID id) throws RepositoryException;
    long size();
    E get(ID id);
    List<E> getAll() throws RepositoryException;
}
