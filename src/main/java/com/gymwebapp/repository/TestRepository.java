package com.gymwebapp.repository;

import com.gymwebapp.domain.Test;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by elisei on 15.11.2017.
 */
@org.springframework.stereotype.Repository
public class TestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public TestRepository(){

    }

    public void add(Test test){
        entityManager.persist(test);
    }


    public List<Test> getAll(){
        Query q  = entityManager.createQuery("select c from com.gymwebapp.domain.Test c");
        List<Test> tests = q.getResultList();
        return tests;
    }

    public void delete(Integer id) {
        Test test = entityManager.find(Test.class, id);
        if(test != null)
            entityManager.remove(test);
    }

    public void update(Integer id, Test test) {
        Test test1 = entityManager.find(Test.class, id);
        if(test1 != null) {
            test1.setName(test.getName());
            entityManager.merge(test1);
        }
    }
}
