var coaches = {};

var coachesDiv;
var coachDialog;
var showDialog, showLoader, closeLoader;
var canHaveFeedback = false;
var user, myFeedback, feedbackState;

var FEEDBACK_STATE = {
    EDIT:"EDIT",
    DONE:"DONE"
}



function showMyFeedback(){
    var feedback = myFeedback;
    switch (feedbackState){
        case FEEDBACK_STATE.EDIT:
        {
            coachDialog.content.myFeedback.edit.show();
            coachDialog.content.myFeedback.done.hide();
            coachDialog.content.myFeedback.edit.summary.val(feedback !== undefined ? feedback.summary:"");
            coachDialog.content.myFeedback.edit.details.val(feedback!== undefined ? feedback.details:"");
            coachDialog.content.myFeedback.edit.stars.children().each(
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
            coachDialog.content.myFeedback.edit.hide();
            coachDialog.content.myFeedback.done.show();
            coachDialog.content.myFeedback.done.username.text(feedback.author);
            coachDialog.content.myFeedback.done.details.text(feedback.details);
            coachDialog.content.myFeedback.done.summary.text(feedback.summary);
            coachDialog.content.myFeedback.done.stars.children().each(
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
    coachDialog.content.feedbacks.children().not(".titled").remove();
    for(var index in feedbacks){
        if(index > 0)
            coachDialog.content.feedbacks.append($("<hr>"));

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
        coachDialog.content.feedbacks.append(feedbackDiv);
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

function getImage(id){
    return 'url(coach/{0}/image)'.format(id);
}


function coachToString(coach){
    var array = ["username", "email", "birthDay"];
    var r = "";
    for(var index in array){
        var key = array[index];
        r += "<b>{0}</b> : {1}<br>".format(key, key !== 'birthDay' ? coach[key] : formatDate(coach[key]));
    }
    return r;
}



function showCoachPopup(coach){
    var coachId = coach.username;
    coachDialog.content.coachId.text(coachId);
    coachDialog.content.username.text(coach.username);
    coachDialog.content.email.text(coach.email);
    coachDialog.content.title.text(coach.name);
    coachDialog.content.birthday.text(formatDate(coach.birthDay));
    coachDialog.content.description.text(coach.about);

    var url;
    url = getImage(coachId);
    coachDialog.content.image.css('backgroundImage', url);
    showLoader();
    $(function(){
        callServer(APIS.API_COACH_FEEDBACK.format(coachId), HTTP_METHODS.GET, {},
            //onsuccess
            function(data){
                if(!loadFeedbacks(data)) return;
                showDialog();
                closeLoader();
            });
    })


}

function showCoaches(){
    for(var username in coaches){
        var coach = coaches[username];

        var coachThumb ;
        coachThumb = $("<div></div>");
        coachThumb.data("coachId", username);
        coachThumb.addClass("coach_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");

        content = $("<div></div>");
        content.addClass("content");

        divImage = $('<div></div>');
        divImage.addClass('coachImage')
            .css('background-image', getImage(username));
        content.append(divImage);

        var divInfo = $("<div> </div>");
        divInfo.addClass("info")
            .append(
                $("<div></div>").addClass("title").html(coach.name).click(function(){
                    var coachDiv = $(this).parent().parent().parent();
                    var coachId = coachDiv.data("coachId");
                    showCoachPopup(coaches[coachId]);
                })
            )
            .append(
                $("<div></div>").addClass("description").html("<p>{0}</p>".format(coachToString(coach)))
            )
            .append(
                $("<div class='floatingButton'><i class='glyphicon glyphicon-option-horizontal'></i></div>")
                    .click(function(){

                        var coachDiv = $(this).parent().parent().parent();
                        var coachId = coachDiv.data("coachId");
                        showCoachPopup(coaches[coachId]);
                    })
            );
        content.append(divInfo);
        coachThumb.append(content);
        coachesDiv.append(coachThumb);
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
        coachDialog.content.feedbacks.hide();
    }
    else{
        coachDialog.content.feedbacks.show();
        showFeedbacks(feedbackList);
    }
    if(canHaveFeedback){
        if(myFeedback !== undefined)
        {
            coachDialog.content.myFeedback.edit.cancelButton.removeClass("hide");
            feedbackState = FEEDBACK_STATE.DONE;
        }
        else
        {
            coachDialog.content.myFeedback.edit.cancelButton.addClass("hide");
            feedbackState = FEEDBACK_STATE.EDIT;
        }
        showMyFeedback();
    }
    return true;
}


function loadCoaches(data) {
    var coach, index;
    if(!("coaches" in data)){
        console.log("the coaches field is missing from json");
        showError("Invalid data received from server!", "Coache field not found in JSON!");
        return;
    }

    for(var index in data.coaches){
        coach = data.coaches[index];
        if(!validateObject(coach, OBJECT_KEYS.COACH)){
            showError("Invalid coaches received!", "Invalid coach: " + JSON.stringify(coach));
            return;
        }
    }
    coaches = {};
    for(index in data.coaches){
        coach = data.coaches[index];
        coach.version = 1;
        coaches[coach.username] = coach;
    }
    showCoaches();
}


function initDialog(){
    var closeDialog = function(){
        coachDialog.real.slideUp("fast");
        coachDialog.fadeOut(function(){
            $(document.body).css('overflow', 'auto');
        });
    };

    showDialog = function(){
        coachDialog.real.slideDown("fast");
        $(document.body).css('overflow', 'hidden');
        coachDialog.fadeIn();
    };

    closeLoader = function(){
        coachDialog.loading.hide();
    };

    showLoader = function(){
        coachDialog.loading.show();
    };
    coachDialog = $("#feedbackDialog");
    coachDialog.click(closeDialog);
    coachDialog.find(".close-button").click(closeDialog);
    coachDialog.real = coachDialog.children(".popup-dialog");
    coachDialog.real.click(function(event){event.preventDefault();return false;})
    coachDialog.loading = coachDialog.real.children(".loading");
    coachDialog.content = coachDialog.real.children(".popup-content");
    coachDialog.content.coachId = coachDialog.content.find('.coachId');
    coachDialog.content.image = coachDialog.content.children(".bannerImage");
    coachDialog.content.closeButton = coachDialog.content.children(".close-button");
    coachDialog.content.title = coachDialog.content.find(".title");
    coachDialog.content.username = coachDialog.content.find(".username .value");
    coachDialog.content.email = coachDialog.content.find(".email .value");
    coachDialog.content.birthday = coachDialog.content.find(".birthday .value");
    coachDialog.content.description = coachDialog.content.find(".description");
    if(canHaveFeedback){
        coachDialog.content.myFeedback = coachDialog.content.children(".myFeedback");
        coachDialog.content.myFeedback.end = coachDialog.content.myFeedback.children(".end");
        coachDialog.content.myFeedback.done = coachDialog.content.myFeedback.children(".done");
        coachDialog.content.myFeedback.done.username = coachDialog.content.myFeedback.done.find(".username");
        coachDialog.content.myFeedback.done.details = coachDialog.content.myFeedback.done.find(".details");
        coachDialog.content.myFeedback.done.summary = coachDialog.content.myFeedback.done.find(".summary");
        coachDialog.content.myFeedback.done.stars = coachDialog.content.myFeedback.done.find(".stars");
        coachDialog.content.myFeedback.done.editButton = coachDialog.content.myFeedback.done.find(".button.edit");
        coachDialog.content.myFeedback.done.editButton.click(function(){
            feedbackState = FEEDBACK_STATE.EDIT;
            coachDialog.content.myFeedback.edit.cancelButton.removeClass("hide");
            showMyFeedback();
        });
        coachDialog.content.myFeedback.done.deleteButton = coachDialog.content.myFeedback.done.find(".button.delete");
        coachDialog.content.myFeedback.done.deleteButton.click(function(){
            var coachId = coachDialog.content.coachId.text();
            callServer(APIS.API_COACH_FEEDBACK.format(coachId), HTTP_METHODS.DELETE, {}, function(){
                feedbackState = FEEDBACK_STATE.EDIT;
                coachDialog.content.myFeedback.edit.cancelButton.addClass("hide");
                myFeedback = undefined;
                showMyFeedback();
            });
        });

        coachDialog.content.myFeedback.edit = coachDialog.content.myFeedback.children(".edit");
        coachDialog.content.myFeedback.edit.summary = coachDialog.content.myFeedback.edit.find('#summaryFeedbackInput');
        coachDialog.content.myFeedback.edit.details = coachDialog.content.myFeedback.edit.find('#detailsFeedbackInput');
        coachDialog.content.myFeedback.edit.stars = coachDialog.content.myFeedback.edit.find('.stars');
        coachDialog.content.myFeedback.edit.stars.mouseleave(function(){
            $(this).children(".star").removeClass('temporaryFilled temporaryUnFilled');
        });
        coachDialog.content.myFeedback.edit.stars.find('.star.changeable').hover(function(){
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
        coachDialog.content.myFeedback.edit.cancelButton = coachDialog.content.myFeedback.edit.find(".button.cancel");
        coachDialog.content.myFeedback.edit.cancelButton.click(function(){
            feedbackState = FEEDBACK_STATE.DONE;
            showMyFeedback();
        });
        coachDialog.content.myFeedback.edit.submitButton = coachDialog.content.myFeedback.edit.find(".button.submit");
        coachDialog.content.myFeedback.edit.submitButton.click(function(){
            var feedback = {};
            var coachId = coachDialog.content.coachId.text();
            var date  = new Date().getTime();
            feedback.details = coachDialog.content.myFeedback.edit.details.val();
            feedback.summary = coachDialog.content.myFeedback.edit.summary.val();
            feedback.starsCount = 0;
            var stars = coachDialog.content.myFeedback.edit.stars.children(".star");
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

            callServer(APIS.API_COACH_FEEDBACK.format(coachId), myFeedback !== undefined?HTTP_METHODS.PUT : HTTP_METHODS.POST,
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
        coachDialog.content.children(".myFeedback").remove();
    }
    coachDialog.content.feedbacks = coachDialog.content.children(".feedbacks");
}


function init(){
    coachesDiv = $('#coachesGrid');
    if(coachesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_CLIENT_COACHES},
        function() {
            //onsuccess
            canHaveFeedback = true;
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET,
                {}, function(data){
                    if(!loadUser(data))
                        return;

                    initDialog();
                    callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches);
                })
        },
        function(){
            //onnotloggedin
            canHaveFeedback = false;
            initDialog();
            callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches)
        },
        function(){
            //permission denied
            callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page:PAGE_COACHES},function(){
                canHaveFeedback = false;
                initDialog();
                callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches);
            })
        }
    );
}


$(init);