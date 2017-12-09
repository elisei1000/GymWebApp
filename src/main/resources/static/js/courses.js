/**
 * Created by elisei on 24.11.2017.
 */
var courses = {};

var coursesDiv;
var courseDialog;
var showDialog, showLoader, closeLoader;
var canHaveFeedback = false;
var user, myFeedback, feedbackState;

var FEEDBACK_STATE = {
    EDIT:"EDIT",
    DONE:"DONE"
};

function courseClick(){
    var courseDiv = $(this).parent().parent().parent();
    var courseId = courseDiv.data("courseId");

    var course = courses[courseId];
    courseDialog.content.title.text(course.title);
    courseDialog.content.description.text(course.description);
    courseDialog.content.difficulty.text(
        "Difficulty: {0}".format(DIFFICULTY_LEVEL[course.difficultyLevel])
    );

    var url = $(this).parent().parent().children('.courseImage').css('background-image');
    courseDialog.content.image.css('backgroundImage', url);
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

function showMyFeedback(){
    var feedback = myFeedback;
    switch (feedbackState){
        case FEEDBACK_STATE.EDIT:
        {
            courseDialog.content.myFeedback.edit.show();
            courseDialog.content.myFeedback.done.hide();
            break;
        }
        case FEEDBACK_STATE.DONE:
        {
            courseDialog.content.myFeedback.edit.hide();
            courseDialog.content.myFeedback.done.show();
            courseDialog.content.myFeedback.done.username.text(feedback.author);
            courseDialog.content.myFeedback.done.details.text(feedback.details);
            courseDialog.content.myFeedback.done.summary.text(feedback.summary);
            courseDialog.content.myFeedback.done.stars.children().each(
                function(index){
                    if(index < myFeedback.starsCount)
                        $(this).addClass('filled');
                    else
                        $(this).removeClass('filled');
            });
            break;
        }
    }
}

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



function showCourses(){
    for(var id in courses){
        var course = courses[id];

        var courseThumb = $('<div ></div>');
        courseThumb.data("courseId", course.id);
        courseThumb.addClass("course_thumbnail col-xs-6 col-sm-4 col-md-3 wow animated fadeInUp ");

        var content = $('<div></div>');
        content.addClass("content");

        var divImage = $('<div></div>');
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
    myFeedback = undefined;
    for(index in data.feedbacks){
        feedback = data.feedbacks[index];
        if(!validateObject(feedback, OBJECT_KEYS.FEEDBACKS)) {
            showError("Invalid feedback received!", "Invalid feedback: " + JSON.stringify(feedback));
            return false;
        }
        if(canHaveFeedback && feedback.author === user.username)
            myFeedback = feedback;
        else
            feedbackList.push(feedback);
    }
    if(feedbackList.length === 0){
        courseDialog.content.feedbacks.hide();
    }
    else{
        courseDialog.content.feedbacks.show();
        showFeedbacks(feedbackList);
    }

    if(canHaveFeedback){
        if(myFeedback !== undefined)
            feedbackState = FEEDBACK_STATE.DONE;
        else
            feedbackState = FEEDBACK_STATE.EDIT;
        showMyFeedback();
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
    courseDialog = $("#coursePopup");
    courseDialog.click(closeDialog);
    courseDialog.find(".close-button").click(closeDialog);
    courseDialog.real = courseDialog.children(".popup-dialog");
    courseDialog.real.click(function(){event.preventDefault();return false;})
    courseDialog.loading = courseDialog.real.children(".loading");
    courseDialog.content = courseDialog.real.children(".popup-content");
    courseDialog.content.image = courseDialog.content.children(".bannerImage");
    courseDialog.content.closeButton = courseDialog.content.children(".close-button");
    courseDialog.content.title = courseDialog.content.find(".title");
    courseDialog.content.description = courseDialog.content.children(".description");
    courseDialog.content.difficulty = courseDialog.content.find(".difficulty");
    if(canHaveFeedback){
        courseDialog.content.myFeedback = courseDialog.content.children(".myFeedback");
        courseDialog.content.myFeedback.end = courseDialog.content.myFeedback.children(".end");
        courseDialog.content.myFeedback.done = courseDialog.content.myFeedback.children(".done");
        courseDialog.content.myFeedback.done.username = courseDialog.content.myFeedback.done.find(".username");
        courseDialog.content.myFeedback.done.details = courseDialog.content.myFeedback.done.find(".details");
        courseDialog.content.myFeedback.done.summary = courseDialog.content.myFeedback.done.find(".summary");
        courseDialog.content.myFeedback.done.stars = courseDialog.content.myFeedback.done.find(".stars");
        courseDialog.content.myFeedback.edit = courseDialog.content.myFeedback.children(".edit");
    }
    else{
        courseDialog.content.children(".myFeedback").remove();
    }
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
            canHaveFeedback = true;
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET,
                {}, function(data){
                    if(!loadUser(data))
                        return;

                    initDialog();
                    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses);
                })
        },
        function(){
            canHaveFeedback = false;
            initDialog();
            callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
        }
    );
};


$(init);