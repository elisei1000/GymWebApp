/**
 * Created by elisei on 24.11.2017.
 */
var courses = {};

var coursesDiv;
var courseDialog;
var showDialog, showLoader, closeLoader;
var user;

var maxId = 0;
var POPUP_STATE = {
    EDIT:"EDIT",
    ADD:"ADD"
};

var popupState = POPUP_STATE.EDIT;

function showFeedbacks(feedbacks){
    var feedback;
    courseDialog.content.feedbacks.children().not(".titled").remove();
    for(var index in feedbacks){
        if(index > 0)
            courseDialog.content.feedbacks.append($("<hr>"));

        feedback = feedbacks[index];

        var feedbackDiv = $("<div></div>");
        feedbackDiv.addClass("feedback");

        var identityDiv  =  $("<div></div>");
        identityDiv.addClass("identity")
            .append(
                $("<div></div></div>").addClass("image")
            )
            .append(
                $("<div></div>").addClass("username").text(feedback.author)
            );
        feedbackDiv.append(identityDiv);

        var content = $("<div></div>");
        var stars = $("<div></div>").addClass("stars");
        for(var i = 1; i <= 5; i++){
            stars.append(
                $("<div></div>").addClass("star{0}".format(
                    (i<=feedback.starsCount?" filled":"")
                ))
            );
        }
        content.addClass("content")
            .append(stars)
            .append(
                $("<div></div>").addClass("summary").text(feedback.summary)
            )
            .append(
                $("<div></div>").addClass("details").text(feedback.details)
            );
        feedbackDiv.append(content);
        courseDialog.content.feedbacks.append(feedbackDiv);
    }
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

function showCoursePopup(course){
    if(popupState == POPUP_STATE.ADD){
        course = new Object();
        course.id = maxId;
        course.title = "";
        course.description = "";
        course.difficultyLevel = 1;
        course.startHour = 10;
        course.endHour = 13;
        course.startDate = "2017-12-12";
        course.endDate = "2017-12-13";
        course.numberOfParticipants = 0;
        course.maxPlaces = 30;
        course.teacher = null;
    }
    var courseId = course.id;
    courseDialog.content.courseId.text(courseId);
    courseDialog.content.title.val(course.title);
    courseDialog.content.time.startTime.val('{0}:00'.format(course.startHour));
    courseDialog.content.time.endTime.val('{0}:00'.format(course.endHour));
    courseDialog.content.date.startDate.val(formatDate(course.startDate));
    courseDialog.content.date.endDate.val(formatDate(course.endDate));
    courseDialog.content.participants.freePlaces.val(course.maxPlaces - course.numberOfParticipants);
    courseDialog.content.participants.occupiedPlaces.text(course.numberOfParticipants);
    courseDialog.content.teacher.children(".username").val(course.teacher != null?course.teacher:"");
    courseDialog.content.description.val(course.description);
    courseDialog.content.difficulty.select.val(course.difficultyLevel);
    var url = $(this).parent().parent().children('.courseImage').css('background-image');
    courseDialog.content.image.css('backgroundImage', url);
    if(popupState === POPUP_STATE.ADD){
        courseDialog.content.addButton.show();
        courseDialog.content.updateButton.hide();
        courseDialog.content.deleteButton.hide();
        courseDialog.content.feedbacks.hide();
        closeLoader();
        showDialog();
    }
    else{
        courseDialog.content.addButton.hide();
        courseDialog.content.updateButton.show();
        courseDialog.content.deleteButton.show();
        showLoader();
        $(function(){
            callServer(APIS.API_COURSE_FEEDBACK.format(courseId), HTTP_METHODS.GET, {},
                //onsuccess
                function(data){
                    if(!loadFeedbacks(data)) return;
                    showDialog();
                    closeLoader();
                });
        })
    }

}

function showCourses(){
    var courseThumb, content, divImage;
    coursesDiv.empty();
    for(var id in courses){
        var course = courses[id];

        courseThumb = $('<div ></div>');
        courseThumb.data("courseId", course.id);
        courseThumb.addClass("course_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");

        if(course.id > maxId){
            maxId = course.id
        }

        content = $('<div></div>');
        content.addClass("content");

        divImage = $('<div></div>');
        divImage.addClass('courseImage')
            .css('background-image', 'url(images/box-{0}.jpg)'.format(Math.floor(Math.random() * 3) + 1));
        content.append(divImage);

        var divDifficulty = $("<div></div>");
        divDifficulty.html("Difficulty: <span>{0}</span>".format(DIFFICULTY_LEVEL[course.difficultyLevel]))
                .addClass("difficulty");
        content.append(divDifficulty);

        var divInfo = $("<div></div>");
        divInfo.addClass("info")
            .append(
                $("<div></div>").addClass("title").html(course.title).click(function(){
                        var courseDiv = $(this).parent().parent().parent();
                        var courseId = courseDiv.data("courseId");

                        callServer(APIS.API_COURSE.format(courseId), HTTP_METHODS.GET,{}, loadCourse);
                    }
                )
            )
            .append(
                $("<div></div>").addClass("description").html("<p>{0}</p>".format(course.description))
            ).append(
                $("<div>Edit course</div>").addClass("title edit-button").click(function(){
                          var courseDiv = $(this).parent().parent().parent();
                          var courseId = courseDiv.data("courseId");
                          callServer(APIS.API_COURSE.format(courseId), HTTP_METHODS.GET,{}, loadCourse);
                })
            );
        content.append(divInfo);

        courseThumb.append(content);
        coursesDiv.append(courseThumb);
    }
    //empty one for add

    courseThumb = $('<div ></div>');
    courseThumb.data("courseId", maxId + 1);
    courseThumb.addClass("course_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");
    content = $('<div></div>');
    content.addClass("content");
    divImage = $('<button></button>');
    divImage.addClass('courseImage')
        .css('background-image', 'url(images/box-add.png)');
    divImage.css('background-size','100%').css('border','none').click(function(){
        popupState = POPUP_STATE.ADD;
        showCoursePopup();
    });
    content.append(divImage);
    courseThumb.append(content);
    coursesDiv.append(courseThumb);
}

function loadCourse(data){
    if(!("course" in data)){
        showError("Invalid data received from server!",
        "Course not in JSON: {0}".format(JSON.stringify(data)));
        return false;
    }

    if (!validateObject(data.course, OBJECT_KEYS.COURSE_FULL)){
        showError("Invalid data received from server!",
            "Course hasn't all necessary fields : {0}".format(JSON.stringify(data.course)));
        return false;
    }
    var course = data.course;
    popupState = POPUP_STATE.EDIT;
    showCoursePopup(course);
}



function loadUser(data){
    if(!("user" in data)){
        showError("Invalid data received from server!",
            "User not in JSON: {0}".format(JSON.stringify(data)));
        return false;
    }
    if(!validateObject(data.user, OBJECT_KEYS.CUSER)){
        showError("Invalid data received from server!",
            "User hasn't all necessary fields! {0}".format(
                JSON.stringify(data.user)
            ));
        return false;
    }
    user = data.user;
    return true;
}

function loadFeedbacks(data){
    var feedback, index, feedbackList;
    if(!("feedbacks" in data)){
        showError("Invalid data received from server",
            "Feedbacks is not in JSON: {0}".format(data))
        return false;
    }
    feedbackList = [];
    for(index in data.feedbacks){
        feedback = data.feedbacks[index];
        if(!validateObject(feedback, OBJECT_KEYS.FEEDBACKS)) {
            showError("Invalid feedback received!", "Invalid feedback: " + JSON.stringify(feedback));
            return false;
        }
        feedbackList.push(feedback);
    }
    if(feedbackList.length === 0){
        courseDialog.content.feedbacks.hide();
    }
    else{
        courseDialog.content.feedbacks.show();
        showFeedbacks(feedbackList);
    }
     return true;
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
            showError("Invalid courses received!", "Invalid course: {0}".format(JSON.stringify(course)));
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


function getCourse(){
    var course = {
        id          : 0,
        title       :   courseDialog.content.title.val(),
        description :   courseDialog.content.description.val(),
        difficultyLevel  :   courseDialog.content.difficulty.select.val(),
        startHour   :   courseDialog.content.time.startTime.val().split(":")[0],
        endHour     :   courseDialog.content.time.endTime.val().split(":")[0],
        teacher     :   courseDialog.content.teacher.username.val(),
        startDate   :   courseDialog.content.date.startDate.val().split(":")[0],
        endDate     :   courseDialog.content.date.endDate.val(),
        maxPlaces   :   courseDialog.content.participants.freePlaces.val()
    };
    if(!validateObject(course, OBJECT_KEYS.COURSE)){
        showError("Script error. ", "Not all values in course");
        return undefined;
    }
    var errors = [];

    if(!course.title || course.title === '')
        errors.push("Title must be non-empty!");
    if(!course.description || course.description === '')
        errors.push("Description must be non-empty!");
    try{
        course.difficultyLevel = parseInt(course.difficultyLevel);
        if(course.difficultyLevel <= 0 || course.difficultyLevel > 5)
            throw new Error();
    }
    catch(e) {
        errors.push("Invalid difficulty Level. You must select something");
    }
    try{
        course.startHour = parseInt(course.startHour);
    }
    catch(e){
        errors.push("Invalid start hour!");
    }

    try{
        course.endHour = parseInt(course.endHour);
    }
    catch(e){
        errors.push("Invalid end hour!");
    }


    try{
        course.maxPlaces = parseInt(course.maxPlaces);
        if(course.maxPlaces <= 0)
            throw new Error();
    }
    catch (e){
        errors.push("Invalid max Places");
    }

    if(!course.teacher || course.teacher === '')
        errors.push("Teacher must be non-empty!");

    if(errors.length > 0){
        var error = errors.join("\n");
        showError(error, error);
        return undefined
    }

    return course;
}

function initDialog(){
    var closeDialog = function(){
        courseDialog.real.slideUp("fast");
        courseDialog.fadeOut(function(){
            $(document.body).css('overflow', 'auto');
        });
    };

    showDialog = function(){
        courseDialog.real.slideDown("fast");
        $(document.body).css('overflow', 'hidden');
        courseDialog.fadeIn();
    };


    closeLoader = function(){
        courseDialog.loading.hide();
    };

    showLoader = function(){
        courseDialog.loading.show();
    };
    courseDialog = $("#feedbackDialog");
    courseDialog.click(closeDialog);
    courseDialog.find(".close-button").click(closeDialog);
    courseDialog.real = courseDialog.children(".popup-dialog");
    courseDialog.real.click(function(){event.preventDefault();return false;})
    courseDialog.loading = courseDialog.real.children(".loading");
    courseDialog.content = courseDialog.real.children(".popup-content");
    courseDialog.content.courseId = courseDialog.content.find('.courseId');
    courseDialog.content.image = courseDialog.content.children(".bannerImage");
    courseDialog.content.closeButton = courseDialog.content.children(".close-button");
    courseDialog.content.title = courseDialog.content.find(".title");
    courseDialog.content.children(".description").val(courseDialog.content.children(".description").val());
    courseDialog.content.difficulty = courseDialog.content.find(".difficulty");
    courseDialog.content.difficulty.select = courseDialog.content.difficulty.find("select");
    $.each(DIFFICULTY_LEVEL, function(index) {
        courseDialog.content.difficulty.select.append($("<option />").text(this.toString()).val(index));
    });
    courseDialog.content.difficulty.select.val(0);

    courseDialog.content.addButton = courseDialog.content.find(".button.add");
    courseDialog.content.addButton.click(function(){
        var courseId = courseDialog.content.courseId.text();
        var course = getCourse();
        if(course === undefined) return;
        callServer(APIS.API_GET_COURSES, HTTP_METHODS.POST, course, function(data){
            if(!("course" in data)){
                showError("Invalid response received from server!",
                    "Invalid response received from server\n{0}".format(JSON.stringify(data)));
                return;
            }

            if(!validateObject(data.course, OBJECT_KEYS.COURSE))
            {
                showError("Invalid response received from server!",
                    "Invalid response received from server\n{0}".format(JSON.stringify(data)));
                return;
            }

            courses[data.course.id] = data.course;
            showCourses();
            closeDialog();
        })
    });
    courseDialog.content.updateButton = courseDialog.content.find(".button.update");
    courseDialog.content.updateButton.click(function(){
        var courseId = courseDialog.content.courseId.text();
        var course = getCourse();
        if(course === undefined) return;
        course.id = courseId;
        callServer(APIS.API_COURSE.format(courseId), HTTP_METHODS.PUT, course, function(data){
            courses[courseId] = course;
            showCourses();
            closeDialog();
        });
    });
    courseDialog.content.deleteButton = courseDialog.content.find(".button.delete");
    courseDialog.content.deleteButton.click(function(){
        var courseId = courseDialog.content.courseId.text();
        callServer(APIS.API_COURSE.format(courseId), HTTP_METHODS.DELETE, {},  function(data){
            delete courses[courseId];
            showCourses();
            closeDialog();
        })
    });

    courseDialog.content.time = courseDialog.content.children('.time');
    courseDialog.content.time.startTime = courseDialog.content.time.children('.start');
    courseDialog.content.time.endTime = courseDialog.content.time.children('.end');

    courseDialog.content.date = courseDialog.content.children('.date');
    courseDialog.content.date.startDate = courseDialog.content.date.children('.start');
    courseDialog.content.date.endDate = courseDialog.content.date.children('.end');

    courseDialog.content.participants = courseDialog.content.children(".participants");
    courseDialog.content.participants.occupiedPlaces = courseDialog.content.participants.children(".occupiedPlaces");
    courseDialog.content.participants.freePlaces = courseDialog.content.participants.children(".freePlaces");
    courseDialog.content.teacher  = courseDialog.content.find('.teacher');
    courseDialog.content.teacher.username = courseDialog.content.teacher.find('.username');
    courseDialog.content.description = courseDialog.content.find('.description');
    courseDialog.content.feedbacks = courseDialog.content.children(".feedbacks");
}

function init(){
    coursesDiv = $('#coursesGrid');
    if(coursesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_MANAGE_COURSES},
        function() {
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET,
                {}, function(data){
                    if(!loadUser(data))
                        return;

                    initDialog();
                    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses);
                })
        }
    );
};

$(init);