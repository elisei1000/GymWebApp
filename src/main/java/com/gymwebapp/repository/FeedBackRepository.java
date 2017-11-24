package com.gymwebapp.repository;

import com.gymwebapp.domain.Feedback;
import com.gymwebapp.domain.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@org.springframework.stereotype.Repository
public class FeedBackRepository implements CrudRepository<Feedback, Integer> {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean checkIfFeedBackExists(Feedback feedback){

        return (entityManager.find(Feedback.class, feedback.getId()) != null);
    }

    public boolean checkIfIdExists(Integer id){

        Feedback feedback = entityManager.find(Feedback.class, id);
        return feedback.getId()!=null;
    }

    @Override
    public void add(Feedback entity) throws RepositoryException {
        if(checkIfFeedBackExists(entity)){
            throw new RepositoryException("Feedback already exists");
        }
        entityManager.persist(entity);
    }

    @Override
    public void update(Feedback entity) throws RepositoryException {
        if(!checkIfFeedBackExists(entity)){
            throw new RepositoryException("Feedback doesn't exist");
        }

        entityManager.merge(entity);
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        if(!checkIfIdExists(integer)){
            throw new RepositoryException("Feedback doesn't exist");
        }
        entityManager.remove(entityManager.find(Feedback.class, integer));
    }

    @Override
    public long size() {
        TypedQuery<Feedback> q = entityManager.createQuery("select f from Feedback f", Feedback.class);
        return q.getResultList().size();
    }

    @Override
    public Feedback get(Integer integer) {
        return entityManager.find(Feedback.class, integer);
    }

    @Override
    public List<Feedback> getAll() {
        TypedQuery<Feedback> q = entityManager.createQuery("select f from Feedback f", Feedback.class);
        return q.getResultList();
    }
}
