package com.gymwebapp.repository;

import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.Subscription;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@org.springframework.stereotype.Repository
public class SubscriptionRepository implements CrudRepository<Subscription, Integer> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Subscription entity) throws RepositoryException {
        if(checkIfSubscriptionExists(entity.getId()))
            throw new RepositoryException("Subscription already exists");
        entityManager.persist(entity);
    }

    @Override
    public void update(Subscription entity) throws RepositoryException {
        Subscription resultFind = entityManager.find(Subscription.class, entity.getId());
        if(resultFind==null)
            throw new RepositoryException("Subscription doesn't exists");
        resultFind.setEndDate(entity.getEndDate());
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        if(!checkIfSubscriptionExists(integer))
            throw new RepositoryException("Subscription doesn't exists");
        entityManager.remove(entityManager.find(Subscription.class, integer));
    }

    @Override
    public long size() {
        TypedQuery<Subscription> q = entityManager.createQuery("select s from Subscription s", Subscription.class);
        return q.getResultList().size();
    }

    @Override
    public Subscription get(Integer integer) {
        return entityManager.find(Subscription.class, integer);
    }

    @Override
    public List<Subscription> getAll() throws RepositoryException {
        TypedQuery<Subscription> q = entityManager.createQuery("select s from Subscription s", Subscription.class);
        return q.getResultList();
    }

    public boolean checkIfSubscriptionExists(Integer integer){
        return entityManager.find(Subscription.class, integer)!=null;
    }
}
