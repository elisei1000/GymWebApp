package com.gymwebapp.repository;

import com.gymwebapp.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
        User user = get(entity.getUsername());
        if(user != null)
            throw new RepositoryException("Username already exists!");
        entityManager.persist(entity);
    }

    @Override
    public void update(User entity) throws RepositoryException {
        User user = get(entity.getUsername());
        if(user == null)
            throw new RepositoryException("User doesn't exists!");
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setName(entity.getName());
        user.setBirthDay(entity.getBirthDay());
    }

    @Override
    public void remove(String s) throws RepositoryException {
        User user = get(s);
        if(user == null)
            throw new RepositoryException("User doesn't exists!");
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

    public List<Administrator> getAllAdministrators(){
        TypedQuery<Administrator> q = entityManager.createQuery("select a from Administrator a", Administrator.class);
        return q.getResultList();
    }
}