package com.gymwebapp.repository;

import com.gymwebapp.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */

@org.springframework.stereotype.Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepository(){
    }

    public boolean checkIfUsernameExists(User user){
        Query q  = entityManager.createQuery("select c from User c where c.username = :username");
        q.setParameter("username",user.getUsername());
        List<User> users = q.getResultList();

        if(users==null || users.size()==0){
            return false;
        }
        return true;
    }

    public boolean checkIfUserExists(User user){
        Query q  = entityManager.createQuery("select c from User c where c.username = :username and c.password = :password");
        q.setParameter("username",user.getUsername());
        q.setParameter("password",user.getPassword());
        List<User> users = q.getResultList();

        if(users==null || users.size()==0){
            return false;
        }
        return true;
    }

    public void add(User user){
        entityManager.persist(user);
    }

    public void update(Integer id, User user) {
        User user1 = entityManager.find(User.class, id);
        if(user1 != null) {
            user1.setName(user.getName());
            entityManager.merge(user1);
        }
    }
}