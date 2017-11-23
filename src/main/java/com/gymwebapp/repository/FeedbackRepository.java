package com.gymwebapp.repository;

import com.gymwebapp.domain.Feedback;
import com.gymwebapp.domain.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class FeedbackRepository implements CrudRepository<Feedback, Integer> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Feedback entity) throws RepositoryException {
        Feedback feedback = get(entity.getId());
        if(feedback != null)
            throw new RepositoryException("Feedback already exists");
        entityManager.persist(entity);
    }

    @Override
    public void update(Feedback entity) throws RepositoryException {
        Feedback feedback = get(entity.getId());
        if(feedback == null)
            throw new RepositoryException("Feedback doesn't exist");
        entityManager.merge(entity);
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        Feedback feedback = get(integer);
        if(feedback == null)
            throw new RepositoryException("Feedback doesn't exist");
        entityManager.remove(feedback);
    }

    @Override
    public long size() {
        TypedQuery<Feedback> q = entityManager.createQuery("select f from Feedback f", Feedback.class);
        return q.getResultList().size();
    }

    @Override
    public Feedback get(Integer integer){
        return entityManager.find(Feedback.class, integer);
    }

    @Override
    public List<Feedback> getAll() throws RepositoryException {
        TypedQuery<Feedback> q = entityManager.createQuery("select f from Feedback f", Feedback.class);
        return q.getResultList();
    }
}