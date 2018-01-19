package com.gymwebapp.service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.model.ScheduleModel;
import com.gymwebapp.repository.CourseRepository;
import com.gymwebapp.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public List<CourseModel> getAll() {
        List<CourseModel> courseModelList = new ArrayList<>();

        for (Course course : courseRepository.getAll()) {
            String teacher = null;
            if (course.getTeacher() != null) {
                teacher = course.getTeacher().getUsername();
            }
            courseModelList.add(new CourseModel(course.getId()
                    , course.getDifficultyLevel()
                    , course.getStartHour()
                    , course.getEndHour()
                    , course.getStartDate()
                    , course.getEndDate()
                    , course.getMaxPlaces()
                    , course.getClients().size()
                    , teacher
                    , course.getTitle()
                    , course.getDescription()));

        }

        return courseModelList;
    }

    @Transactional
    public CourseModel getCourse(Integer id) {
        Course course = courseRepository.get(id);

        if (course == null) {
            return new CourseModel();
        } else {
            String teacher = null;
            if (course.getTeacher() != null)
                teacher = course.getTeacher().getUsername();
            return new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(), course.getClients().size(), teacher, course.getTitle(), course.getDescription());
        }
    }

    @Transactional
    public CourseModel addCourse(CourseModel courseModel, Coach teacher) throws RepositoryException {
        Course course = new Course(courseModel.getTitle(), courseModel.getDescription(), courseModel.getDifficultyLevel(), courseModel.getStartHour(), courseModel.getEndHour(), courseModel.getStartDate(), courseModel.getEndDate(), courseModel.getMaxPlaces(), teacher);

        courseRepository.add(course);

        courseModel.setId(course.getId());

        return courseModel;
    }

    @Transactional
    public void modifyCourse(CourseModel courseModel, Coach teacher) throws RepositoryException {
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
    public boolean checkAttendUserToCourse(Integer id, Client client) throws RepositoryException {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new RepositoryException("Given course doesn't exist!");
        }
        List<Client> clients = course.getClients();

        for (Client c:clients) {
            if (c.getUsername().compareTo(client.getUsername()) == 0) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void attendUserToCourse(Integer id, Client client) throws RepositoryException {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new RepositoryException("Given course doesn't exists!");
        }
        List<Client> clients = course.getClients();

        if(course.getMaxPlaces()==clients.size()){
            throw new RepositoryException("Number of participants is maximum!");
        }

        for (Client c:clients){
            if(c.getUsername().compareTo(client.getUsername())==0){
                throw new RepositoryException("The client is already attended to the course!");
            }
        }

        clients.add(client);

        courseRepository.update(course);
    }

    @Transactional
    public void deleteCourse(Integer id) throws RepositoryException {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new RepositoryException("");
        } else {
            courseRepository.remove(id);
        }
    }

    @Transactional
    public List<String> addFeedback(Integer id, FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        Course course = courseRepository.get(id);

        if (course == null) {
            errors.add("Given course doesn't exists!");
            return errors;
        }

        List<CourseFeedback> feedbacks = course.getFeedbacks();

        for (CourseFeedback feedback : feedbacks) {
            String author = "";
            if (feedback.getAuthor() != null) {
                author = feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                errors.add("The user already has given feedback!");
                return errors;
            }
        }

        Client client=userService.getClient(feedbackModel.getAuthor());

        Feedback feedback = new CourseFeedback(feedbackModel.getStarsCount(), feedbackModel.getSummary(), feedbackModel.getDetails(), feedbackModel.getDate(), client, course);

        try {
            feedBackRepository.add(feedback);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }

    @Transactional
    public List<String> modifyFeedback(Integer id, FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        Course course = courseRepository.get(id);

        if (course == null) {
            errors.add("Given course doesn't exists!");
            return errors;
        }

        List<CourseFeedback> feedbacks = course.getFeedbacks();

        Feedback feedbackModified = null;

        for (CourseFeedback feedback : feedbacks) {
            String author = "";
            if (feedback.getAuthor() != null) {
                author = feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                feedbackModified = feedback;
            }
        }

        if (feedbackModified == null) {
            errors.add("User didn't give feedback!");
            return errors;
        }

        feedbackModified.setDate(feedbackModel.getDate());
        feedbackModified.setDetails(feedbackModel.getDetails());
        feedbackModified.setStarsCount(feedbackModel.getStarsCount());
        feedbackModified.setSummary(feedbackModel.getSummary());

        try {
            feedBackRepository.update(feedbackModified);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }

    @Transactional
    public List<String> deleteFeedback(Integer id,String username) {
        List<String> errors = new ArrayList<>();

        Course course = courseRepository.get(id);

        if (course == null) {
            errors.add("Given course doesn't exists");
            return errors;
        }

        List<CourseFeedback> feedbacks = course.getFeedbacks();

        Integer idFeedback = null;

        for(Feedback feedback:feedbacks){
            if(feedback.getAuthor().getUsername().compareTo(username)==0) {
                idFeedback = feedback.getId();
                break;
            }
        }

        if(idFeedback==null){
            errors.add("Feedback doesn't exists!");
            return errors;
        }

        try {
            feedBackRepository.remove(idFeedback);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }


    @Transactional
    public Integer getLastId(){
        return courseRepository.getLastGeneratedValue();
    }

    @Transactional
    public List<Client> getAllClientsForCourse(Integer id){
        return courseRepository.get(id).getClients();
    }

    @Transactional
    public long size(){
        return courseRepository.size();
    }

    @Transactional
    public List<List<Course>> getSchedule(ScheduleModel scheduleModel) {
        List<Course> courses = courseRepository.getAll();
        List<List<Course>> schedule_courses = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            schedule_courses.add(new ArrayList<>());

        Calendar calendar = Calendar.getInstance();
        for (Course course : courses) {
            if (course.getStartDate().compareTo(scheduleModel.getStartDate()) >= 0 && course.getEndDate().compareTo(scheduleModel.getEndDate()) <= 0) {
                calendar.setTime(course.getStartDate());
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if (day == 1)
                    schedule_courses.get(6).add(course);
                else
                    schedule_courses.get(day - 2).add(course);
            }
        }
        return schedule_courses;
    }
}
