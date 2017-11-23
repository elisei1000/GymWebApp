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


    public boolean checkIfCourseExists(Course course) {

        return (entityManager.find(Course.class, course.getId()) != null);
    }

    public boolean checkIfIdExists(Integer id){
        return (entityManager.find(Course.class, id) != null);
    }

    @Override
    public void add(Course entity) throws RepositoryException {
        if(this.checkIfCourseExists(entity))
        {
            throw new RepositoryException("Course already exists");
        }

        entityManager.persist(entity);
    }

    @Override
    public void update(Course entity) throws RepositoryException {
        if(!this.checkIfCourseExists(entity))
        {
            throw new RepositoryException("Course doesn't exist");
        }

        entityManager.merge(entityManager.find(Course.class, entity.getId()));
    }

    @Override
    public void remove(Integer integer) throws RepositoryException {
        if(!this.checkIfIdExists(integer))
        {
            throw new RepositoryException("Course doesn't exist");
        }

        entityManager.remove(entityManager.find(Course.class, integer));
    }

    @Override
    public long size() {
        TypedQuery<Course> q = entityManager.createQuery("select c from Course c", Course.class);
        List<Course> courses = q.getResultList();

        return courses.size();
    }

    @Override
    public Course get(Integer integer) throws RepositoryException {
        return entityManager.find(Course.class, integer);
    }

    @Override
    public List<Course> getAll() throws RepositoryException {
        TypedQuery<Course> q = entityManager.createQuery("select c from Course c", Course.class);
        List<Course> courses = q.getResultList();

        if(courses==null)
            throw new RepositoryException("No courses");

        return courses;
    }
}
