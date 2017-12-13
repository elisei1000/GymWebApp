package com.gymwebapp.service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public List<CourseModel> getAll() {
        List<CourseModel> courseModelList = new ArrayList<>();

        for (Course course : courseRepository.getAll()) {
            String teacher = null;
            if (course.getTeacher() != null) {
                teacher = course.getTeacher().getUsername();
            }
            courseModelList.add(new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(), course.getClients().size(), teacher, course.getTitle(), course.getDescription()));

        }

        return courseModelList;
    }

    @Transactional
    public CourseModel getCourse(Integer id) {
        Course course = courseRepository.get(id);

        if (course == null) {
            return new CourseModel();
        } else {
            String teacher=null;
            if(course.getTeacher()!=null)
                teacher=course.getTeacher().getUsername();
            return new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(),course.getClients().size(),teacher,course.getTitle(),course.getDescription());
        }
    }

    @Transactional
    public void addCourse(CourseModel courseModel,Coach teacher) throws RepositoryException {
        Course course=new Course(courseModel.getTitle(),courseModel.getDescription(),courseModel.getDifficultyLevel(),courseModel.getStartHour(),courseModel.getEndHour(),courseModel.getStartDate(),courseModel.getEndDate(),courseModel.getMaxPlaces(),teacher);

        courseRepository.add(course);
    }

    @Transactional
    public void modifyCourse(CourseModel courseModel,Coach teacher) throws RepositoryException {
        Course course = courseRepository.get(courseModel.getId());
        course.setTitle(courseModel.getTitle());
        course.setDescription(courseModel.getDescription());
        course.setDifficultyLevel(courseModel.getDifficultyLevel());
        course.setStartHour(courseModel.getStartHour());
        course.setEndHour(courseModel.getEndHour());
        course.setStartDate(courseModel.getStartDate());
        course.setEndDate(courseModel.getEndDate());
        course.setMaxPlaces(courseModel.getMaxPlaces());
        course.setTeacher(teacher);

        courseRepository.update(course);
    }

    @Transactional
    public void attendUserToCourse(Integer id,Client client) throws RepositoryException {
        Course course = courseRepository.get(id);
        if (course==null){
            throw new RepositoryException("");
        }
        List<Client> clients=course.getClients();

        clients.add(client);

        courseRepository.update(course);
    }

    @Transactional
    public void deleteCourse(Integer id) throws RepositoryException {
        courseRepository.remove(id);
    }
}
