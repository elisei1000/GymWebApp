package com.gymwebapp.repository;

import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@org.springframework.stereotype.Repository
public class CourseRepository implements CrudRepository<Course, Integer>{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void add(Course entity) throws RepositoryException {
        Course course = get(entity.getId());
        if(course != null)
            throw new RepositoryException("Course already exists");
        entityManager.persist(entity);
    }

    @Override
    public void update(Course entity) throws RepositoryException {
        Course course = get(entity.getId());
        if(course == null)
            throw new RepositoryException("Course doesn't exist");
        entityManager.merge(entity);
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        Course course = get(integer);
        if(course == null)
            throw new RepositoryException("Course doesn't exist");
        entityManager.remove(course);
    }

    @Override
    public long size() {
        TypedQuery<Course> q = entityManager.createQuery("select c from Course c", Course.class);
        return q.getResultList().size();
    }

    @Override
    public Course get(Integer integer) {
        return entityManager.find(Course.class, integer);
    }

    @Override
    public List<Course> getAll() {
        TypedQuery<Course> q = entityManager.createQuery("select c from Course c", Course.class);
        return q.getResultList();
    }

}