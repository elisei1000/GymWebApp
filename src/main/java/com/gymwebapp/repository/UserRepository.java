package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
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
        if(this.checkIfUsernameExists(entity)){
            throw new RepositoryException("Username already exists!");
        }else {
            entityManager.persist(entity);
        }
    }

    public boolean checkIfUserExists(User user){
        Query q  = entityManager.createQuery("select c from User c where c.id = :username and c.password = :password");
        q.setParameter("username",user.getUsername());
        q.setParameter("password",user.getPassword());
        List<Client> users = q.getResultList();

        if(users==null || users.size()==0){
            return false;
        }
        return true;
    }

    public boolean checkIfUsernameExists(User user){
        Query q  = entityManager.createQuery("select c from User c where c.id = :username");
        q.setParameter("username",user.getUsername());
        List<Client> users = q.getResultList();

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
        return 0;
    }

    @Override
    public User get(String s) {
        return entityManager.find(User.class, s);
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}