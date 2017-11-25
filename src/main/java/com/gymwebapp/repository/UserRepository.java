package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */

@org.springframework.stereotype.Repository
public class UserRepository implements CrudRepository<User, String> {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void add(User entity) throws RepositoryException {
        User user = get(entity.getUsername());
        if(user != null)
            throw new RepositoryException("Username already exists!");
        entityManager.persist(entity);
    }

    @Override
    public void update(User entity) throws RepositoryException {
        User user = get(entity.getUsername());
        if(user == null)
            throw new RepositoryException("User doesn't exist");
        entityManager.merge(entity);
    }

    @Override
    public void remove(String s) throws RepositoryException {
        User user = get(s);
        if(user == null)
            throw new RepositoryException("User doesn't exist in db");
        entityManager.remove(user);
    }

    @Override
    public long size() {
        TypedQuery<User> q = entityManager.createQuery("select u from User u", User.class);
        return q.getResultList().size();
    }

    @Override
    public User get(String s){
        return entityManager.find(User.class, s);
    }

    @Override
    public List<User> getAll(){
        TypedQuery<User> q = entityManager.createQuery("select u from User u", User.class);
        return q.getResultList();
    }

    public Boolean checkUserPassword(User user){
        User dbUser = get(user.getUsername());
        if(dbUser == null || dbUser.getPassword() ==null || !dbUser.getPassword().equals(user.getPassword()))
            return false;
        return true;
    }

    public List<Client> getAllClients(){
        TypedQuery<Client> q = entityManager.createQuery("select c from Client c", Client.class);
        return q.getResultList();
    }

    public List<Coach> getAllCoaches(){
        TypedQuery<Coach> q = entityManager.createQuery("select c from Coach c", Coach.class);
        return q.getResultList();
    }
}