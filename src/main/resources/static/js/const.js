/**
 * Created by elisei on 24.11.2017.
 */

var PAGES = {
    MANAGE_COURSES      : 'MANAGE_COURSES',
    MANAGE_COACHES      : 'MANAGE_COACHES',
    CLIENT_COURSES      : 'CLIENT_COURSES',
    CLIENT_COACHES      : 'CLIENT_COACHES',
    FEEDBACKS           : 'FEEDBACKS',
    COURSES             : 'COURSES',
    COACHES             : 'COACHES',
    PERSONAL_INFO       : 'PERSONAL_INFO',
    HOME                : 'HOME',
    LOGIN               : 'LOGIN',
    REGISTER            : 'REGISTER',
    CONTACT             : 'CONTACT',
    ABOUT               : 'ABOUT'
};

var PAGES_MAPPINGS_URL = {
    MANAGE_COURSES      : '/manageCourses.html',
    MANAGE_COACHES      : '/manageCoaches.html',
    CLIENT_COURSES      : '/courses.html',
    CLIENT_COACHES      : '/coaches.html',
    FEEDBACKS           : '/feedbacks.html',
    COURSES             : '/courses.html',
    COACHES             : '/coaches.html',
    PERSONAL_INFO       : '/personalInfo.html',
    HOME                : '/main.html',
    LOGIN               : '/loginPage.html',
    REGISTER            : '/register.html',
    CONTACT             : '/contact.html',
    ABOUT               : '/about.html',
    SCHEDULE            : '/schedule.html',
};

var PAGES_MAPPINGS_NAME = {
    MANAGE_COURSES      : 'MANAGE COURSES',
    MANAGE_COACHES      : 'MANAGE COACHES',
    CLIENT_COURSES      : 'COURSES',
    CLIENT_COACHES      : 'COACHES',
    FEEDBACKS           : 'FEEDBACKS',
    COURSES             : 'COURSES',
    COACHES             : 'COACHES',
    PERSONAL_INFO       : 'PERSONAL INFO',
    HOME                : 'HOME',
    LOGIN               : 'LOGIN',
    REGISTER            : 'REGISTER',
    CONTACT             : 'CONTACT US',
    ABOUT               : 'ABOUT',
    SCHEDULE            : 'SCHEDULE',
};



var PAGE_MANAGE_COURSES = 'MANAGE_COURSES';
var PAGE_MANAGE_COACHES = 'MANAGE_COACHES';
var PAGE_CLIENT_COURSES = 'CLIENT_COURSES';
var PAGE_CLIENT_COACHES = 'CLIENT_COACHES';
var PAGE_FEEDBACKS = 'FEEDBACKS';
var PAGE_COURSES = 'COURSES';
var PAGE_COACHES = 'COACHES';
var PAGE_PERSONAL_INFO = 'PERSONAL_INFO';
var PAGE_HOME = 'HOME';
var PAGE_LOGIN = 'LOGIN';
var PAGE_REGISTER = 'REGISTER';
var PAGE_CONTACT = 'CONTACT';
var PAGE_ABOUT  = 'ABOUT';
var PAGE_SCHEDULE = 'SCHEDULE';

var STATUSES = {
    STATUS_NOT_LOGGED_IN        :'STATUS_NOT_LOGGED_IN'    ,
    STATUS_PERMISSION_DENIED    :'STATUS_PERMISSION_DENIED',
    STATUS_FAILED               :'STATUS_FAILED'           ,
    STATUS_OK                   :'STATUS_OK'
};

var STATUS_NOT_LOGGED_IN      = 'STATUS_NOT_LOGGED_IN';
var STATUS_PERMISSION_DENIED  = 'STATUS_PERMISSION_DENIED';
var STATUS_FAILED             = 'STATUS_FAILED';
var STATUS_OK                 = 'STATUS_OK';


var APIS = {
    API_HAS_PERMISSION:"/cuser/hasPermission",
    API_PERMISSIONS: "/cuser/permissions",
    API_GET_COURSES : "/course",
    API_COURSE_FEEDBACK : "/course/{0}/feedback",
    API_COURSE_ATTENDED : "/course/{0}/attended",
    API_COURSE_ATTEND : "/course/{0}/attend",
    API_GET_CURRENT_USER : "/cuser",
    API_GET_COACHES : "/coaches",
    API_COURSE: "/course/{0}",
};


var HTTP_METHODS = {
    GET : "GET",
    POST: "POST",
    PUT: "PUT",
    DELETE: "DELETE"
};


var DIFFICULTY_LEVEL = {
    1:"easy",
    2:"easy-medium",
    3:"medium",
    4:"medium-hard",
    5:"hard"
};



var OBJECT_KEYS = {
    COURSE : [
        "id", "difficultyLevel", "startHour", "endHour", "startDate", "endDate", "maxPlaces",
        "title", "description"
    ],
    COURSE_FULL :[
        "id", "difficultyLevel", "startHour", "endHour", "startDate", "endDate", "maxPlaces",
        "title", "description", "numberOfParticipants", "maxPlaces"
    ],
    FEEDBACK : [
        "id", "starsCount", "summary", "details", "date",
        "author"
    ],
    CUSER : [
        "username", "email", "name", "birthDay"
    ],
    COACH : [
        "username", "email", "name", "birthDay"
    ]
};
