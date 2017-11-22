package com.gymwebapp.repository;

import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */

@org.springframework.stereotype.Repository
public class UserRepository implements CrudRepository<User, String> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void add(User entity) throws RepositoryException {
        if(entityManager.find(User.class, entity.getId()) != null){
            throw new RepositoryException("User exists in db");
        }
        entityManager.persist(entity);
    }

    @Override
    public void update(User entity) throws RepositoryException {
        if(entityManager.find(User.class, entity.getId()) == null)
        {
            throw new RepositoryException("User doesn't exist");
        }
        entityManager.merge(entityManager.find(User.class, entity.getId()));
    }

    @Override
    public void remove(String s) throws RepositoryException {
        if(entityManager.find(User.class, s) == null){
            throw new RepositoryException("User doesn't exist in db");
        }
        entityManager.remove(entityManager.find(User.class, s));
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public User get(String s) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}