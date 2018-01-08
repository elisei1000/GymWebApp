/**
 * Created by elisei on 24.11.2017.
 */
var courses = {};

var coursesDiv;
var courseDialog, courseAddDialog;
var showDialog, showLoader, closeLoader, showAddDialog, closeAddLoader, showAddLoader;
var canHaveFeedback = false;
var user, myFeedback, feedbackState;

var maxId = 0;

var FEEDBACK_STATE = {
    EDIT:"EDIT",
    DONE:"DONE"
};


function showMyFeedback(){
}

function showFeedbacks(feedbacks){

}

function showAddCourseInPopup(course){
    var courseId = course.id;
    courseAddDialog.content.courseId.text(courseId);
    courseAddDialog.content.title.val(course.title);

    courseAddDialog.content.time.children(".start").val('{0}:00'.format(course.startHour));
    courseAddDialog.content.time.children(".end").val('{0}:00'.format(course.endHour));

    courseAddDialog.content.date.children(".start").val(new Date(course.startDate).toLocaleDateString('ro-RO'));
    courseAddDialog.content.date.children(".end").val(new Date(course.endDate).toLocaleDateString('ro-RO'));

    console.log(course);
    courseAddDialog.content.participants.freePlaces.val(course.maxPlaces - course.numberOfParticipants);
    courseAddDialog.content.participants.occupiedPlaces.text(course.numberOfParticipants);
    if(course.teacher !== undefined && course.teacher != null)
    {
        courseAddDialog.content.teacher.show();
        courseAddDialog.content.teacher.children(".username").val(course.teacher);
    }
    else{
        courseAddDialog.content.teacher.hide();
    }
    courseAddDialog.content.find(".description").val(course.description);
    courseAddDialog.content.find(".difficulty").val(
        "Difficulty: {0}".format(DIFFICULTY_LEVEL[course.difficultyLevel]));
    var url = $(this).parent().parent().children('.courseImage').css('background-image');
    courseAddDialog.content.image.css('backgroundImage', url);
    showAddLoader();
    showAddDialog();
    closeAddLoader();
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


function showCourseInPopup(course){
    var courseId = course.id;
    courseDialog.content.courseId.text(courseId);
    courseDialog.content.title.val(course.title);

    courseDialog.content.time.children(".start").val('{0}:00'.format(course.startHour));
    courseDialog.content.time.children(".end").val('{0}:00'.format(course.endHour));

    debugger;
    courseDialog.content.date.children(".start").val(formatDate(course.startDate));
    courseDialog.content.date.children(".end").val(formatDate(course.endDate));

    console.log(course);
    courseDialog.content.participants.freePlaces.val(course.maxPlaces - course.numberOfParticipants);
    courseDialog.content.participants.occupiedPlaces.text(course.numberOfParticipants);
    if(course.teacher !== undefined && course.teacher != null)
    {
        courseDialog.content.teacher.show();
        courseDialog.content.teacher.children(".username").val(course.teacher);
    }
    else{
        courseDialog.content.teacher.hide();
    }
    courseDialog.content.find(".description").val(course.description);
    courseDialog.content.find(".difficulty").val(
        "Difficulty: {0}".format(DIFFICULTY_LEVEL[course.difficultyLevel]));
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

function showCourses(){
    for(var id in courses){
        var course = courses[id];

        var courseThumb = $('<div ></div>');
        courseThumb.data("courseId", course.id);
        courseThumb.addClass("course_thumbnail col-xs-6 col-sm-4 col-md-3 wow animated fadeInUp ");

        if(course.id > maxId){
            maxId = course.id
        }

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

            var courseThumb = $('<div ></div>');
            courseThumb.data("courseId", maxId + 1);
            courseThumb.addClass("course_thumbnail col-xs-6 col-sm-4 col-md-3 wow animated fadeInUp ");

            var content = $('<div></div>');
            content.addClass("content");

            var divImage = $('<button></button>');
            divImage.addClass('courseImage')
                .css('background-image', 'url(images/box-add.png)');
            divImage.css('background-size','100%').css('border','none').click(function(){

                    var data = courses[maxId];
                    data.description = "";
                    data.title = "";
                    data.startDate = "";
                    data.endDate = "";
                    data.startHour = 0;
                    data.endHour = 0;
                    data.freePlaces = "";
                    data.teacher = "";
                    data.difficultyLevel = "";
                    loadCourseAdd(data);

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
    showCourseInPopup(course);
}



function loadCourseAdd(data){

    showAddCourseInPopup(data);
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

function initAddDialog(){
    var closeAddDialog = function(){
        courseAddDialog.real.slideUp("fast");
        courseAddDialog.fadeOut(function(){
            $(document.body).css('overflow', 'auto');
        });
    };

     showAddDialog = function(){
                courseAddDialog.real.slideDown("fast");
                $(document.body).css('overflow', 'hidden');
                courseAddDialog.fadeIn();
        };


    closeAddLoader = function(){
        courseAddDialog.loading.hide();
    };

    showAddLoader = function(){
        courseAddDialog.loading.show();
    };

    courseAddDialog = $("#courseAddPopup");
    courseAddDialog.click(closeAddDialog);
    courseAddDialog.find(".close-button").click(closeAddDialog);
    courseAddDialog.real = courseAddDialog.children(".popup-dialog");
    courseAddDialog.real.click(function(){event.preventDefault();return false;})
    courseAddDialog.loading = courseAddDialog.real.children(".loading");
    courseAddDialog.content = courseAddDialog.real.children(".popup-content");
        var courseContent = courseAddDialog.content;
        courseContent.courseId = courseContent.find('.courseId');
        courseContent.image = courseContent.children(".bannerImage");
        courseContent.closeButton = courseContent.children(".close-button");
        courseContent.title = courseContent.find(".title");
        courseContent.children(".description").val(courseContent.children(".description").val());
        courseContent.find(".difficulty").val(courseContent.find(".difficulty").val());

        courseContent.time = courseContent.children('.time');
        courseContent.time.find('.start').val(courseContent.time.find('.start').val());
        courseContent.time.find('.end').val(courseContent.time.find('.end').val());


        courseContent.date = courseDialog.content.children('.date');
        courseContent.date.find('.start').val(courseContent.date.find('.start').val());
        courseContent.date.find('.end').val(courseContent.date.find('.end').val());

        courseContent.participants = courseContent.children(".participants");
        courseContent.participants.occupiedPlaces = courseContent.participants.children(".occupiedPlaces");
        courseContent.participants.freePlaces = courseContent.participants.children(".freePlaces");
        courseContent.teacher  = courseContent.find('.teacher');
        courseContent.teacher.find('.username').val(courseContent.teacher.find('.username').val());

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
    courseDialog.content.courseId = courseDialog.content.find('.courseId');
    courseDialog.content.image = courseDialog.content.children(".bannerImage");
    courseDialog.content.closeButton = courseDialog.content.children(".close-button");
    courseDialog.content.title = courseDialog.content.find(".title");
    courseDialog.content.children(".description").val(courseDialog.content.children(".description").val());
    //courseDialog.content.difficulty = courseDialog.content.find(".difficulty");

    //courseDialog.content.find(".difficulty").val(courseDialog.content.find(".difficulty").val());
    var result = DIFFICULTY_LEVEL;
    var $dropdown = $("#dropdown");
    $.each(result, function() {
        $dropdown.append($("<option />").text(this.toString()));
    });

    courseDialog.content.time = courseDialog.content.children('.time');
    //courseDialog.content.time.startTime = courseDialog.content.time.children('.start');
    //courseDialog.content.time.endTime = courseDialog.content.time.children('.end');
    courseDialog.content.time.find('.start').val(courseDialog.content.time.find('.start').val());
    courseDialog.content.time.find('.end').val(courseDialog.content.time.find('.end').val());


    courseDialog.content.date = courseDialog.content.children('.date');
    //courseDialog.content.date.startDate = courseDialog.content.date.children('.start');
    //courseDialog.content.date.endDate = courseDialog.content.date.children('.end');
    courseDialog.content.date.find('.start').val(courseDialog.content.date.find('.start').val());
    courseDialog.content.date.find('.end').val(courseDialog.content.date.find('.end').val());

    courseDialog.content.participants = courseDialog.content.children(".participants");
    courseDialog.content.participants.occupiedPlaces = courseDialog.content.participants.children(".occupiedPlaces");
    courseDialog.content.participants.freePlaces = courseDialog.content.participants.children(".freePlaces");
    courseDialog.content.teacher  = courseDialog.content.find('.teacher');
    courseDialog.content.teacher.find('.username').val(courseDialog.content.teacher.find('.username').val());
    if(canHaveFeedback){
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
                    initAddDialog();
                    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses);
                })
        },
        function(){
            canHaveFeedback = false;
            initDialog();
            initAddDialog();
            callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses)
        }
    );
};


$( "#modifyBtn" ).click(function() {

        var courseD = courseDialog.content;
        //nou curs cu datele alea
        var a = Date.parse(courseD.children('.date').find('.start').val());
        var id = courseD.find('.courseId').text();
         $.ajax({

                url : "course/" + id,
                method : HTTP_METHODS.PUT,
                data : JSON.stringify( { "id": courseD.find('.courseId').text(),
                "difficultyLevel": 1,
                "startHour": courseD.time.find('.start').val().split(':')[0],
                "endHour": courseD.time.find('.end').val().split(':')[0],
                "startDate": a,
                "endDate": a,
                "maxPlaces": courseD.participants.children(".freePlaces").val(),
                "teacher": courseD.teacher.find(".username").val(),
                "title": courseD.find(".title").val(),
                "description": courseD.find(".description").val() } ),
                processData: false,
                dataType: "text",
                contentType: "application/json; charset=utf-8",
                error: function(jqXHR, textStatus, errorThrown) {

                    showError("Cannot communicate with server!",  errorThrown)
                },
                success: function(data) {
                                               if(data.includes("STATUS_FAILED")){
                                                    var errString = data.split('[')[1].split(']')[0].split(',');
                                                    for(var s in errString){
                                                       $( "#errors" ).append('<label>' + errString[s] +'</label></br>');
                                                    }
                                                }
                                                else{
                                               courseAddDialog.real.slideUp("fast");
                                                  courseAddDialog.fadeOut(function(){
                                                      $(document.body).css('overflow', 'auto');
                                                  });

                                            location.reload();
                                            }
                                         }
            });

    });


$( "#deleteBtn" ).click(function() {
    var id = courseDialog.content.find('.courseId').text();

     $.ajax({
            url : "/course/" + id,
            method : HTTP_METHODS.DELETE,
            data : HTTP_METHODS.DELETE.toUpperCase() === HTTP_METHODS.GET?data:JSON.stringify(id),
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            error: function(jqXHR, textStatus, errorThrown) {
                showError("Cannot communicate with server!",  errorThrown)
            },
            success: function(data) {  courseDialog.real.slideUp("fast");
                                              courseDialog.fadeOut(function(){
                                                  $(document.body).css('overflow', 'auto');
                                              });

                                        location.reload();
                                     }
        });
});


function GetKeyOfValue(value){
    for(var prop in DIFFICULTY_LEVEL){
        if(DIFFICULTY_LEVEL[prop] === value){
              return prop;
        }
    }
    return;

}

$( "#addBtn" ).click(function() {

    var courseD = courseAddDialog.content;
    //nou curs cu datele alea

     $.ajax({

            url : '/course',
            method : HTTP_METHODS.POST,
            data : JSON.stringify( { "id": courseD.find('.courseId').text(), "difficultyLevel": GetKeyOfValue(courseD.find("#dropdown").val()),
            "startHour": courseD.time.find('.start').val().split(':')[0], "endHour": courseD.time.find('.end').val().split(':')[0],
            "startDate": courseD.children('.date').find('.start').val(),
            "endDate": courseD.children('.date').find('.end').val(),
            "maxPlaces": courseD.participants.children(".freePlaces").val(),
            "teacher": courseD.teacher.find(".username").val(), "title": courseD.find(".title").val(),
            "description": courseD.find(".description").val() }),
            processData: false,
            dataType: "text",
            contentType: "application/json; charset=utf-8",
            error: function(jqXHR, textStatus, errorThrown) {

                showError("You didn't complete all fields",  errorThrown)
            },
            success: function(data) {
                                           if(data.includes("STATUS_FAILED")){
                                                var errString = data.split('[')[1].split(']')[0].split(',');
                                                for(var s in errString){
                                                   $( "#errors" ).append('<label>' + errString[s] +'</label></br>');
                                                }
                                            }
                                            else{
                                            courseAddDialog.real.slideUp("fast");
                                              courseAddDialog.fadeOut(function(){
                                                  $(document.body).css('overflow', 'auto');
                                              });

                                        location.reload();
                                        }
                                     }
        });

});

$(init);