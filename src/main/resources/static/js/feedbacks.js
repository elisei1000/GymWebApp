/**
 * Created by Diana on 1/12/2018.
 */
/**
 * Created by Diana on 1/7/2018.
 */

var coachDialog;
var showDialog, showLoader, closeLoader;
var user;
var coach;

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
    coachDialog.content.feedbacks = coachDialog.content.children(".feedbacks");

    $(".blockButton").click(function(){
        showCoachPopup();
    })
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
        feedbackList.push(feedback);
    }
    if(feedbackList.length === 0){
        coachDialog.content.feedbacks.hide();
    }
    else{
        coachDialog.content.feedbacks.show();
        showFeedbacks(feedbackList);
    }
    return true;
}


function getImage(id){
    return 'url(coach/{0}/image)'.format(id);
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


function showCoachPopup(){
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

function loadCoach(data){
    if(!("coach" in data)){
        showError("Invalid data received from server!",
            "Coach not in JSON: {0}".format(JSON.stringify(data)));
        return false;
    }

    if (!validateObject(data.coach, OBJECT_KEYS.COACH)){
        showError("Invalid data received from server!",
            "Coach hasn't all necessary fields : {0}".format(JSON.stringify(data.coach)));
        return false;
    }
    coach = data.coach;

    $("#username").val(coach.username);
    $("#email").val(coach.email);
    $("#birthDay").val(formatDate(coach.birthDay));
    $("#name").val(coach.name);
}

function init(){
    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_FEEDBACKS},
        function(){
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET, {}, function(data){
                if(!loadUser(data))
                    return;
                initDialog();
                callServer(APIS.API_COACH.format(user.username), HTTP_METHODS.GET,{}, loadCoach);
            })
        }
    );
}

$(init);


