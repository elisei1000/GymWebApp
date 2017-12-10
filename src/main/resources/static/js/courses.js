/**
 * Created by elisei on 24.11.2017.
 */
var courses = [];

var coursesDiv;
var courseDialog;
var showDialog;

function courseClick(){
    showDialog();
}

    function showCourses(){
        for(var index in courses){
            var course = courses[index];

            var courseThumb = $('<div ></div>');
            courseThumb.addClass("course_thumbnail col-xs-6 col-sm-4 col-md-3 wow animated fadeInUp ");

            var content = $('<div></div>');
            content.addClass("content");

            var divImage = $('<div></div>');
            divImage.addClass('image').append($('<img />')
                .attr('src', 'images/box-{0}.jpg'.format(Math.floor(Math.random() * 3) + 1)));
            content.append(divImage);

            var divDifficulty = $("<div></div>");
            divDifficulty.html("Difficulty: <span>{0}</span>".format(DIFFICULTY_LEVEL[course.difficultyLevel]))
                    .addClass("difficulty");
            content.append(divDifficulty);

            var divInfo = $("<div></div>");
            divInfo.addClass("info")
                .append(
                    $("<div></div>").addClass("title").html(course.title).click(courseClick)
                )
                .append(
                    $("<div></div>").addClass("description").html("<p>{0}</p>".format(course.description))
                );


            content.append(divInfo);

            courseThumb.append(content);
            coursesDiv.append(courseThumb);
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



function initDialog(){
    var closeDialog = function(){
        courseDialog.real.slideUp("fast");
        courseDialog.fadeOut();
    };

    showDialog = function(){
      courseDialog.real.slideDown("fast");
      courseDialog.fadeIn();
    };
    courseDialog = $("#coursePopup");
    courseDialog.click(closeDialog);
    courseDialog.find(".close-button").click(closeDialog);
    courseDialog.real = courseDialog.children(".popup-dialog");
    courseDialog.real.click(function(){return false;})
    courseDialog.content = courseDialog.real.children(".popup-content");
    courseDialog.closeButton = courseDialog.content.children(".close");
}

function init(){
    coursesDiv = $('#coursesGrid');
    if(coursesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }

    initDialog();

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_COURSES});
    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
};


$(init);