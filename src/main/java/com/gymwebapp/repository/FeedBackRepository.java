package com.gymwebapp.repository;

import com.gymwebapp.domain.*;

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

        try {
            Feedback feedback = get(id);
            if (feedback.getId() == null || feedback == null)
                return false;
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
    }

    @Override
    public void add(Feedback entity) throws RepositoryException {
        entityManager.persist(entity);
    }

    @Override
    public void update(Feedback entity) throws RepositoryException {
        if(!checkIfFeedBackExists(entity)){
            throw new RepositoryException("Feedback doesn't exists!");
        }
        entityManager.merge(entity);
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        if(!checkIfIdExists(integer)){
            throw new RepositoryException("Feedback doesn't exists!");
        }
        entityManager.remove(entityManager.find(Feedback.class, integer));
    }

    public Integer getLastGeneratedValue()
    {
        TypedQuery<Integer> q = entityManager.createQuery("select max(id) from Feedback c ", Integer.class);
        return q.getSingleResult();
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

    public List<Feedback> getAllClientFeedbacks(Client client){
        TypedQuery<Feedback> q = entityManager.createQuery("select f from Feedback f where f.author=?1", Feedback.class);
        q.setParameter(1, client);
        return q.getResultList();
    }

    public List<CourseFeedback> getAllCourseFeedbacks(){
        TypedQuery<CourseFeedback> q = entityManager.createQuery("select f from CourseFeedback f", CourseFeedback.class);
        return q.getResultList();
    }

    public List<CoachFeedback> getAllCoachFeedbacks(){
        TypedQuery<CoachFeedback> q = entityManager.createQuery("select f from CoachFeedback f", CoachFeedback.class);
        return q.getResultList();
    }
}
