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

var PAGES_MAPPINGS = {
    MANAGE_COURSES      : '/manageCourses.html',
    MANAGE_COACHES      : '/manageCoaches.html',
    CLIENT_COURSES      : '/courses.html',
    CLIENT_COACHES      : '/coches.html',
    FEEDBACKS           : '/feedbacks.html',
    COURSES             : '/courses.html',
    COACHES             : '/coaches.html',
    PERSONAL_INFO       : '/personalInfo.html',
    HOME                : '/main.html',
    LOGIN               : '/loginPage.html',
    REGISTER            : '/registerPage.html',
    CONTACT             : '/contact.html',
    ABOUT               : '/about.html',
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
    API_GET_COURSES : "/course/courses",
    API_COURSE_FEEDBACK : "/course/{0}/feedback",
    API_GET_CURRENT_USER : "/cuser",
    API_GET_COACHES : "/coaches"
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
        "id", "difficultyLevel", "startHour", "endHour", "startDate", "endDate", "maxPlaces", "title", "description"
    ],
    FEEDBACK : [
        "id", "starsCount", "summary", "details", "date",
        "author"
    ],
    CUSER : [
        "username", "email", "name", "birthDay"
    ],
    MANAGE_COACHES : [
        "id", "username", "password", "email", "name", "birthday"
    ]
};
