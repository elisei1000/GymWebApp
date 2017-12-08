/**
 * Created by elisei on 24.11.2017.
 */
var courses = {};

var coursesDiv;
var courseDialog;
var showDialog;
var canAddFeedbacks = false;

function courseClick(){
    var courseDiv = $(this).parent().parent().parent();
    var courseId = courseDiv.data("courseId");

    var course = courses[courseId];
    courseDialog.content.title.text(course.title);
    courseDialog.content.description.text(course.description);
    courseDialog.content.difficulty.text(
        "Difficulty: {0}".format(DIFFICULTY_LEVEL[course.difficultyLevel]));

    var url = "url({0})".format($(this).parent().parent().find('img').attr('src'));
    console.log(url);
    courseDialog.content.image.css('backgroundImage',
        url);
    showDialog();
}

function showCourses(){
    for(var id in courses){
        var course = courses[id];

        var courseThumb = $('<div ></div>');
        courseThumb.data("courseId", course.id);
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
    var course, index;
    if(!("courses" in data)){
        showError("Invalid data received from server!", "Course field not found in JSON!");
        return;
    }

    for(index in data.courses){
        course = data.courses[index];
        if(!validateObject(course, OBJECT_KEYS.COURSE)){
            showError("Invalid courses received!", "Invalid course: " + JSON.stringify(course));
            return;
        }
    }
    courses = {};
    for(index in data.courses){
        course = data.courses[index];
        courses[course.id] = course;
    }
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
    courseDialog.content.image = courseDialog.content.children(".image");
    courseDialog.content.closeButton = courseDialog.content.children(".close");
    courseDialog.content.title = courseDialog.content.find(".title");
    courseDialog.content.description = courseDialog.content.children(".description");
    courseDialog.content.difficulty = courseDialog.content.find(".difficulty");
    courseDialog.content.myFeedback = courseDialog.content.children(".myFeedback");
    courseDialog.content.feedbacks = courseDialog.content.children(".feedbacks");
}

function init(){
    coursesDiv = $('#coursesGrid');
    if(coursesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }



    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_CLIENT_COURSES},
        function() {
            canAddFeedbacks = true;
            initDialog();
            callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
        },
        function(){
            initDialog();
            callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
        }
    );
};


$(init);