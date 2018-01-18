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


function showMyFeedback(){
    var feedback = myFeedback;
    switch (feedbackState){
        case FEEDBACK_STATE.EDIT:
        {
            courseDialog.content.myFeedback.edit.show();
            courseDialog.content.myFeedback.done.hide();
            courseDialog.content.myFeedback.edit.summary.val(feedback !== undefined ? feedback.summary:"");
            courseDialog.content.myFeedback.edit.details.val(feedback!== undefined ? feedback.details:"");
            courseDialog.content.myFeedback.edit.stars.children().each(
                function(index){
                    if(myFeedback!== undefined && index < myFeedback.starsCount)
                        $(this).addClass('filled');
                    else
                        $(this).removeClass('filled');
                });
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

function showCourseInPopup(course){
    var courseId = course.id;
    courseDialog.content.courseId.text(courseId);
    courseDialog.content.title.text(course.title);
    courseDialog.content.time.startTime.text('{0}:00'.format(course.startHour));
    courseDialog.content.time.endTime.text('{0}:00'.format(course.endHour));
    courseDialog.content.date.startDate.text(new Date(course.startDate).toLocaleDateString('ro-RO'));
    courseDialog.content.date.endDate.text(new Date(course.endDate).toLocaleDateString('ro-RO'));
    courseDialog.content.participants.freePlaces.text(course.maxPlaces - course.numberOfParticipants);
    courseDialog.content.participants.occupiedPlaces.text(course.numberOfParticipants);
    if(canHaveFeedback){
        callServer(APIS.API_COURSE_ATTENDED.format(courseId), HTTP_METHODS.GET, {},
            function(data){
                if(!("attended" in data)){
                    showError("Invalid response from server",
                        "Invalid response from server for attended url: {0}".format(JSON.stringify(data)));
                    return;
                }
                var attended = data.attended;
                if(attended)
                    courseDialog.content.participants.attendButton.addClass("disabled").text("Already attended");
                else
                if(course.maxPlaces - course.numberOfParticipants > 0)
                    courseDialog.content.participants.attendButton.removeClass("disabled").text("Attend");
                else
                    courseDialog.content.participants.attendButton.addClass("disabled").text("No free places available")
            }
        );
    }

    if(course.teacher !== undefined && course.teacher !== null)
    {
        courseDialog.content.teacher.show();
        courseDialog.content.teacher.username.text(course.teacher);
    }
    else{
        courseDialog.content.teacher.hide();
    }
    courseDialog.content.description.text(course.description);
    courseDialog.content.difficulty.text(
        "Difficulty: {0}".format(DIFFICULTY_LEVEL[course.difficultyLevel])
    );
    var url = getImage(courseId);
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

function getImage(id){
    return 'url(course/{0}/image)'.format(id);
}

function showCourses(){
    for(var id in courses){
        var course = courses[id];

        var courseThumb = $('<div ></div>');
        courseThumb.data("courseId", course.id);
        courseThumb.addClass("course_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");

        var content = $('<div></div>');
        content.addClass("content");

        var divImage = $('<div></div>');
        divImage.addClass('courseImage')
            .css('background-image', getImage(id));
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
            )
            .append(
                $("<div class='floatingButton'><i class='glyphicon glyphicon-option-horizontal'></i></div>")
                    .click(function(){
                        var courseDiv = $(this).parent().parent().parent();
                        var courseId = courseDiv.data("courseId");
                        callServer(APIS.API_COURSE.format(courseId), HTTP_METHODS.GET,{}, loadCourse);
                    })
            );
        content.append(divInfo);

        courseThumb.append(content);
        coursesDiv.append(courseThumb);
    }
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
    showCourseInPopup(course);
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
            "Feedbacks is not in JSON: {0}".format(data));
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
        {
            courseDialog.content.myFeedback.edit.cancelButton.removeClass("hide");
            feedbackState = FEEDBACK_STATE.DONE;
        }
        else
        {
            courseDialog.content.myFeedback.edit.cancelButton.addClass("hide");
            feedbackState = FEEDBACK_STATE.EDIT;
        }
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
    courseDialog = $("#feedbackDialog");
    courseDialog.click(closeDialog);
    courseDialog.find(".close-button").click(closeDialog);
    courseDialog.real = courseDialog.children(".popup-dialog");
    courseDialog.real.click(function(event){event.preventDefault();return false;})
    courseDialog.loading = courseDialog.real.children(".loading");
    courseDialog.content = courseDialog.real.children(".popup-content");
    courseDialog.content.courseId = courseDialog.content.find('.courseId');
    courseDialog.content.image = courseDialog.content.children(".bannerImage");
    courseDialog.content.closeButton = courseDialog.content.children(".close-button");
    courseDialog.content.title = courseDialog.content.find(".title");
    courseDialog.content.description = courseDialog.content.children(".description");
    courseDialog.content.difficulty = courseDialog.content.find(".difficulty");
    courseDialog.content.time = courseDialog.content.children('.time');
    courseDialog.content.time.startTime = courseDialog.content.time.children('.start');
    courseDialog.content.time.endTime = courseDialog.content.time.children('.end');
    courseDialog.content.date = courseDialog.content.children('.date');
    courseDialog.content.date.startDate = courseDialog.content.date.children('.start');
    courseDialog.content.date.endDate = courseDialog.content.date.children('.end');
    courseDialog.content.participants = courseDialog.content.children(".participants");
    courseDialog.content.participants.occupiedPlaces = courseDialog.content.participants.children(".occupiedPlaces");
    courseDialog.content.participants.freePlaces = courseDialog.content.participants.children(".freePlaces");
    courseDialog.content.participants.attendButton = courseDialog.content.participants.children(".button.attend");
    courseDialog.content.teacher  = courseDialog.content.find('.teacher');
    courseDialog.content.teacher.username = courseDialog.content.teacher.find('.username');
    if(canHaveFeedback){
        courseDialog.content.participants.attendButton.click(function(){
            if(!($(this).hasClass("disabled")))
            {
                var courseId = courseDialog.content.courseId.text();
                callServer(APIS.API_COURSE_ATTEND.format(courseId), HTTP_METHODS.PUT, {}, function(){
                    courses[courseId].numberOfParticipants += 1;
                    courseDialog.content.participants.occupiedPlaces.text(courses[courseId].numberOfParticipants);
                    courseDialog.content.participants.freePlaces.text(
                        courses[courseId].maxPlaces - courses[courseId].numberOfParticipants);
                    courseDialog.content.participants.attendButton.addClass("disabled").text("Already attended")
                })
            }
        });

        courseDialog.content.myFeedback = courseDialog.content.children(".myFeedback");
        courseDialog.content.myFeedback.end = courseDialog.content.myFeedback.children(".end");
        courseDialog.content.myFeedback.done = courseDialog.content.myFeedback.children(".done");
        courseDialog.content.myFeedback.done.username = courseDialog.content.myFeedback.done.find(".username");
        courseDialog.content.myFeedback.done.details = courseDialog.content.myFeedback.done.find(".details");
        courseDialog.content.myFeedback.done.summary = courseDialog.content.myFeedback.done.find(".summary");
        courseDialog.content.myFeedback.done.stars = courseDialog.content.myFeedback.done.find(".stars");
        courseDialog.content.myFeedback.done.editButton = courseDialog.content.myFeedback.done.find(".button.edit");
        courseDialog.content.myFeedback.done.editButton.click(function(){
           feedbackState = FEEDBACK_STATE.EDIT;
           courseDialog.content.myFeedback.edit.cancelButton.removeClass("hide");
           showMyFeedback();
        });
        courseDialog.content.myFeedback.done.deleteButton = courseDialog.content.myFeedback.done.find(".button.delete");
        courseDialog.content.myFeedback.done.deleteButton.click(function(){
            var courseId = courseDialog.content.courseId.text();
            callServer(APIS.API_COURSE_FEEDBACK.format(courseId), HTTP_METHODS.DELETE, {}, function(){
                feedbackState = FEEDBACK_STATE.EDIT;
                courseDialog.content.myFeedback.edit.cancelButton.addClass("hide");
                myFeedback = undefined;
                showMyFeedback();
            });
        });

        courseDialog.content.myFeedback.edit = courseDialog.content.myFeedback.children(".edit");
        courseDialog.content.myFeedback.edit.summary = courseDialog.content.myFeedback.edit.find('#summaryFeedbackInput');
        courseDialog.content.myFeedback.edit.details = courseDialog.content.myFeedback.edit.find('#detailsFeedbackInput');
        courseDialog.content.myFeedback.edit.stars = courseDialog.content.myFeedback.edit.find('.stars');
        courseDialog.content.myFeedback.edit.stars.mouseleave(function(){
            $(this).children(".star").removeClass('temporaryFilled temporaryUnFilled');
        });
        courseDialog.content.myFeedback.edit.stars.find('.star.changeable').hover(function(){
            var parentIndex =  $(this).index();
            $(this).parent().children(".star.changeable").each(function(index) {
                if (index <= parentIndex)
                    $(this).addClass('temporaryFilled').removeClass("temporaryUnFilled");
                else
                    $(this).removeClass('temporaryFilled').addClass('temporaryUnFilled');
            })
        },
        function(){
            $(this).parent().children(".star").removeClass('temporaryFilled temporaryUnFilled');
        }).click(function(){
            $(this).parent().children(".star.temporaryFilled").addClass('filled').removeClass("temporaryFilled");
            $(this).parent().children(".star.temporaryUnFilled").removeClass('temporaryUnFilled filled');
        });
        courseDialog.content.myFeedback.edit.cancelButton = courseDialog.content.myFeedback.edit.find(".button.cancel");
        courseDialog.content.myFeedback.edit.cancelButton.click(function(){
           feedbackState = FEEDBACK_STATE.DONE;
           showMyFeedback();
        });
        courseDialog.content.myFeedback.edit.submitButton = courseDialog.content.myFeedback.edit.find(".button.submit");
        courseDialog.content.myFeedback.edit.submitButton.click(function(){
           var feedback = {};
           var courseId = courseDialog.content.courseId.text();
           var date  = new Date().getTime();
           feedback.details = courseDialog.content.myFeedback.edit.details.val();
           feedback.summary = courseDialog.content.myFeedback.edit.summary.val();
           feedback.starsCount = 0;
           var stars = courseDialog.content.myFeedback.edit.stars.children(".star");
           for(var i =0; i<stars.length; i++){
               var star = $(stars.get(i));
               if(!star.hasClass("filled"))
                   break;
               feedback.starsCount++;
           }
           console.log(JSON.stringify(feedback));
           if(feedback.details === undefined || feedback.details === ''){
               showError("Details must be non empty!", "Invalid feedback: {0}".format(JSON.stringify(feedback)));
               return;
           }
           if(feedback.summary === undefined || feedback.summary === ""){
               showError("Summary must be non empty!", "Invalid feedback: {0}".format(JSON.stringify(feedback)));
               return;
           }

           callServer(APIS.API_COURSE_FEEDBACK.format(courseId), myFeedback !== undefined?HTTP_METHODS.PUT : HTTP_METHODS.POST,
                feedback, function(){
                    feedbackState = FEEDBACK_STATE.DONE;
                    myFeedback = feedback;
                    myFeedback.date = date;
                    myFeedback.author = user.username;
                    showMyFeedback();
               });
        });
    }
    else{
        courseDialog.content.children(".myFeedback").remove();
        courseDialog.content.participants.attendButton.remove();
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
            //onsuccess
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
            //onnotloggedin
            canHaveFeedback = false;
            initDialog();
            callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
        },
        function(){
            //permission denied
            callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page:PAGE_COURSES},function(){
                canHaveFeedback = false;
                initDialog();
                callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses);
            })
        }
    );
};


$(init);