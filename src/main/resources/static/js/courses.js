/**
 * Created by elisei on 24.11.2017.
 */
var courses = [];

var coursesDiv;

function showCourses(){
    for(var index in courses){
        var course = courses[index];

        var courseDiv = $("<div class='course_thumbnail'></div>");
        courseDiv.data("gymwebapp-courseid", course.id);

        var nameDiv = $("<div class='text'></div>");
        courseDiv.append(nameDiv);

        var imageDiv = $("<div class='image'></div>");
        courseDiv.append(imageDiv);

        var difficultyDiv  = $("<div class='difficulty'></div>");
        courseDiv.append(difficultyDiv);

        coursesDiv.append(courseDiv);
    }
}

function loadCourses(data){
    if(!("courses" in data)){
        showError("Invalid data received from server!", "Course field not found in JSON!");
        return;
    }

    for(var index in data.courses){
        var course = data.courses[index];
        if(!validateObject(course, OBJECT_KEYS.COURSE)){
            showError("Invalid courses received!", "Invalid course: " + JSON.stringify(course));
            return;
        }
    }

    courses = data.courses;
    showCourses();
}

function init(){
    coursesDiv = $('#courses');
    if(coursesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {});
    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
};


$(init);