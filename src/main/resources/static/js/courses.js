/**
 * Created by elisei on 24.11.2017.
 */
var courses = [];

function showCourses(){

}

function loadCourses(data){
    if(!("courses" in data)){
        showError("Invalid data received from server!", "Course field not found in JSON!");
        return;
    }

    for(var course in data.courses){
        if(!validateObject(course, OBJECT_KEYS.COURSE)){
            showError("Invalid courses received!", "Invalid course: " + JSON.stringify(course));
            return;
        }
    }

    courses = data.courses;
    showCourses();
}

function init(){
    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {});
    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
};


$(init);