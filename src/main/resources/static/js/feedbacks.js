/**
 * Created by Diana on 1/12/2018.
 */
/**
 * Created by Diana on 1/7/2018.
 */
/*
var feedbacks=[{id:1, details:"ffff bine", author:"diana", date:"19.02.2016"},
    {id:2, details:"excelent", author:"diana2", date:"19.02.2016"},
    {id:3, details:"super", author:"diana3", date:"19.02.2016"},
    {id:4, details:"minunat", author:"diana4", date:"19.02.2016"},
    {id:5, details:"sexy", author:"diana5", date:"19.02.2016"},
    {id:6, details:"incredibil", author:"diana6", date:"19.02.2016"},
]

function render_feedbacks(){

    $("#feedbacks-list").html("");
    for(var i=0; i<feedbacks.length; i++){

        $("#feedbacks-list").append("<li >"+
            "<div" +
            " style=' border-width: 0.5px;" +
            " border-bottom: 0.5px #999b9e solid; border-top: 0.5px #999b9e solid'>"
            +"<div>" + "Author: " + feedbacks[i].author + " Date: " + feedbacks[i].date + "</div>"
            + "<div>" + feedbacks[i].details + "</div>"

            +"</div>"
            +"</li>");


    }}

render_feedbacks()*/

/**
 * Created by Ramo on 24.11.2017.
 */

var coaches=[];

var feedbacks=[];

var my_feedback={};

var user = null;


var coachDialog;
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

function loadCoach(data){
    showCoachInPopup(data);
}

function showCoachInPopup(coach){
    var coachId = $(coach).attr('id');
    coachDialog.content.title.text(coachId);


    showLoader();
    $(function(){
        callServer(APIS.API_COACH_FEEDBACK.format(coachId), HTTP_METHODS.GET, {},
            //onsuccess
            function(data){
                console.log('here');
                console.log(data);
                if(!loadFeedbacks(data)) return;
                showDialog();
                closeLoader();
            });
    })
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




function call_coaches_list(){
    $.ajax({
        type: "GET",
        url: "/coach",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            coaches = data.data['coaches'];
            console.log(coaches);
            render_coaches_list();
            console.log('hei');



        },
        error: function (data) {
            alert("Something went wrong.");
        }
    });
}

function call_feedbacks(el) {


    var id = $(el).attr('id');

    $.ajax({
        type: "GET",
        url: "/coach/" + id + "/feedback",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log(data, "data");
            feedbacks = data.data['feedbacks'];
            console.log(feedbacks);
            render_feedbacks();
        },
        error: function (data) {
            alert("Something went wrong.");
        }
    });

}

function add_feedback(){

}

function delete_frrdback(){

}

function update_feedback() {

}

function render_coaches_list() {
    $("#coaches-list").html("");
    for(var i=0; i<coaches.length; i++){
        $("#coaches-list").append("<li >"+
            "<div" +
            " style=' border-width: 0.5px;" +
            " border-bottom: 0.5px #999b9e solid; border-top: 0.5px #999b9e solid'>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: xx-large; margin-top: 25px'>"
            +  coaches[i].name +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: x-large'>" + "Want an advice? Contact me: "
            + coaches[i].email +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: large; margin-bottom: 25px'>" + "Liked me? Tell everyone "
            + "<a id='" + coaches[i].username + "' href='#feedback-section' onclick='loadCoach(this)'>here.</a>"
            + "</div>"
            +"</div>"
            +"</li>");
    }
}


function render_feedbacks(){

    $("#feedback-section").show();
    $("#all-feedbacks").html("");
    for(var i=0; i<feedbacks.length; i++){
        if(user) {
            if (feedbacks[i].author !== user.username) {
                $("#all-feedbacks").append("<div style=' color: #FFFFFF; margin-top: 30px; margin-left: 30px;'>" +
                    "<div>" + "Author: " + feedbacks[i].author + " Date: " + feedbacks[i].date + "</div>"
                    + "<div> Summary:" + feedbacks[i].summary + "</div>"
                    + "<div> Details:" + feedbacks[i].details + "</div>"
                    + "<div> Stars" + feedbacks[i].starsCount + "</div>"
                    + "<div>" + feedbacks[i].details + "</div>"
                    + "</div>")
            } else {
                my_feedback = feedbacks[i];
            }
        } else {
            $("#all-feedbacks").append("<div style=' color: #FFFFFF; margin-top: 30px; margin-left: 30px;'>" +
                "<div>" + "Author: " + feedbacks[i].author + " Date: " + feedbacks[i].date + "</div>"
                + "<div> Summary:" + feedbacks[i].summary + "</div>"
                + "<div> Details:" + feedbacks[i].details + "</div>"
                + "<div> Stars" + feedbacks[i].starsCount + "</div>"
                + "<div>" + feedbacks[i].details + "</div>"
                + "</div>")
        }
    }
    if( Object.keys(my_feedback).length !== 0){
        $("#particular-feedback").append("<div style=' color: #FFFFFF; margin-top: 30px; margin-left: 30px;'>" +
            "<div>Your feedback:</div>"+
            "<div>" + " Date: " + my_feedback.date + "</div>"
            + "<div> Summary:"+ my_feedback.summary+"</div>"
            + "<div> Details:"+ my_feedback.details+"</div>"
            + "<div> Stars"+ my_feedback.starsCount +"</div>"
            + "<div >" + my_feedback.details + "<a href='#'> Edit</a>" + "</div>"
            + "</div>")
    }

}

$('.ratings_stars').hover(
    // Handles the mouseover
    function() {
        $(this).prevAll().andSelf().addClass('ratings_over');
        $(this).nextAll().removeClass('ratings_vote');
    },
    // Handles the mouseout
    function() {
        $(this).prevAll().andSelf().removeClass('ratings_over');
        set_votes($(this).parent());
    }
);

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
    coachDialog = $("#coachPopup");
    coachDialog.click(closeDialog);
    coachDialog.find(".close-button").click(closeDialog);
    coachDialog.real = coachDialog.children(".popup-dialog");
    coachDialog.real.click(function(){event.preventDefault();return false;})
    coachDialog.loading = coachDialog.real.children(".loading");
    coachDialog.content = coachDialog.real.children(".popup-content");
    coachDialog.content.coachId = coachDialog.content.find('.title');
    coachDialog.content.title = coachDialog.content.find(".title");

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
            var coachId = coachDialog.content.title.text();
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
            var coachId = coachDialog.content.title.text();
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
    call_coaches_list();

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_CLIENT_COACHES},
        function() {
            //onsuccess
            canHaveFeedback = true;
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET,
                {}, function(data){
                    if(!loadUser(data))
                        return;

                    initDialog();
                    call_coaches_list();
                })
        },
        function(){
            //onnotloggedin
            canHaveFeedback = false;
            initDialog();
            call_coaches_list();
        },
        function(){
            //permission denied
            callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page:FEEDBACKS},function(){
                canHaveFeedback = false;
                initDialog();
                call_coaches_list();
            })
        }
    );
}

$(init);


