package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */


/*
   AR TREBUI SA FIE DENUMIT IN CONTINUARE
   USERRepository, pt ca o sa avem un singur UserRepo
 */


@org.springframework.stereotype.Repository
public class ClientRepository implements CrudRepository<User, String> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void add(User entity) throws RepositoryException {
        if(this.checkIfUsernameExists(entity)){
            throw new RepositoryException("Username already exists!");
        }else {
            entityManager.persist(entity);
        }
    }

    public boolean checkIfUserExists(User user){

        return (entityManager.find(User.class, user.getId()) != null);
    }

    public boolean checkIfUsernameExists(User user){
        TypedQuery<User> q  = entityManager.createQuery("select c from User c where c.id = :username", User.class);
        q.setParameter("username",user.getUsername());
        List<User> users = q.getResultList();

        if(users==null || users.size()==0){
              return false;
        }
         return true;
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
        TypedQuery<User> q = entityManager.createQuery("select u from User u", User.class);
        List<User> users = q.getResultList();

        return users.size();
    }

    @Override
    public User get(String s) throws RepositoryException{
        if(entityManager.find(User.class, s) == null)
        {
            throw new RepositoryException("User doesn't exist in db");
        }
        return entityManager.find(User.class, s);
    }

    @Override
    public List<User> getAll()throws RepositoryException {
        TypedQuery<User> q = entityManager.createQuery("select u from User u", User.class);
        List<User> users = q.getResultList();

        if(users == null)
            throw new RepositoryException("No users");

        return users;

    }
}