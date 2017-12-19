package com.gymwebapp.repository;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.RepositoryException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;

@org.springframework.stereotype.Repository
public class CourseRepository implements CrudRepository<Course, Integer>{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void add(Course entity) throws RepositoryException {

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

    public Integer getLastGeneratedValue()
    {
        TypedQuery<Integer> q = entityManager.createQuery("select max(id) from Course c ", Integer.class);
        return q.getSingleResult();
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

    public List<Course> getAllCoursesForCoach(Coach coach){
        TypedQuery<Course> q = entityManager.createQuery("select c from Course c where c.teacher= ?1", Course.class);
        q.setParameter(1, coach);
        return q.getResultList();
    }

}