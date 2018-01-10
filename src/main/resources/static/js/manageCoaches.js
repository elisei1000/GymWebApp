var coaches = [];

var coachesDiv;
var coachDialog;
var showDialog, showLoader, closeLoader;
var user;


var POPUP_STATE = {
    EDIT : "EDIT",
    ADD  : "ADD"
};

var popupState = POPUP_STATE.EDIT;

function loadDialog() {
    var  form,

    // From http://www.whatwg.org/specs/web-apps/current-work/multipage/states-of-the-type-attribute.html#e-mail-state-%28type=email%29
    emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
    username = $( "#username" ),
        password = $( "#password" ),
        name = $( "#name" ),
        email = $( "#email" ),
        birthDay = $( "#birthDay" ),
        allFields = $( [] ).add(username).add( name ).add( email ).add( birthDay ),
        tips = $( ".validateTips" );

    function updateTips( t ) {
        tips
            .text( t )
            .addClass( "ui-state-highlight" );
        setTimeout(function() {
            tips.removeClass( "ui-state-highlight", 1500 );
        }, 500 );
    }

    function checkLength( o, n, min, max ) {
        if ( o.val().length > max || o.val().length < min ) {
            o.addClass( "ui-state-error" );
            updateTips( "Length of " + n + " must be between " +
                min + " and " + max + "." );
            return false;
        } else {
            return true;
        }
    }

    function checkRegexp( o, regexp, n ) {
        if ( !( regexp.test( o.val() ) ) ) {
            o.addClass( "ui-state-error" );
            updateTips( n );
            return false;
        } else {
            return true;
        }
    }

    function validateFields() {

        var valid = true;

        valid = valid && checkLength( username, "username", 3, 16 );
        valid = valid && checkLength( password, "password", 3, 16 );
        valid = valid && checkLength( name, "name", 3, 16 );
        valid = valid && checkLength( email, "email", 6, 80 );
        valid = valid && checkLength( birthDay, "birthDay", 5, 16 );

        valid = valid && checkRegexp( username, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter." );
        valid = valid && checkRegexp( password, /^[a-z]([0-9a-z_])+$/i, "Password may consist of a-z, 0-9, underscores and must begin with a letter." );
        valid = valid && checkRegexp( name, /^[A-Za-z\s]+$/i, "Name may consist of a-z, A-Z and spaces." );
        valid = valid && checkRegexp( email, emailRegex, "Invalid email format! (eg. ui@jquery.com)" );
        valid = valid && checkRegexp( birthDay, /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/, "Invalid birthday field!" );

        if(valid)
            return true;
        return false;
    }

    function updateCoach() {
        console.log($("#btn-update").attr("name"));

        var valid = validateFields();

        if(valid) {

            var tempCoach = {};

            tempCoach["name"] = name.val();
            tempCoach["username"] = username.val();
            tempCoach["email"] = email.val();
            tempCoach["password"] = password.val();
            tempCoach["birthDay"] = birthDay.val();

            dialog.dialog( "close" );
            // console.log("url: ", APIS.API_GET_COACHES + "\/" + tempCoach.username, tempCoach);

            callServer(APIS.API_GET_COACHES + "\/" + tempCoach.username, HTTP_METHODS.PUT, tempCoach , updateFields(tempCoach));
        }
    }

    function updateFields(updatedCoach) {
        var id = $("#btn-update").attr("name");
        var coach = document.getElementById(id);
        coach.getElementsByClassName("name")[0].innerText       = updatedCoach.name;
        coach.getElementsByClassName("email")[0].innerText      = updatedCoach.email;
    }

    function addCoach() {
        allFields.removeClass( "ui-state-error" );
        var valid = validateFields();

        var tempCoach = {};
        if(freeIds.length == 0)
            tempCoach.id = coaches.length;
        else {
            tempCoach.id = freeIds[0];
            freeIds.splice(0,1);
        }
        tempCoach["username"] = username.val();
        tempCoach["password"] = password.val();
        tempCoach["name"] = name.val();
        tempCoach["email"] = email.val();
        tempCoach["birthDay"] = birthDay.val();

        if ( valid ) {
            // console.log(tempCoach);

            callServer(APIS.API_GET_COACHES, HTTP_METHODS.POST, tempCoach, addCoachToGrid(tempCoach));
            coaches.push(tempCoach);

            dialog.dialog( "close" );
        }
        return valid;
    }

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 700,
        width: 550,
        modal: true,
        // buttons: {
        //     "Save coach": addUser,
        //     "Cancel": function() {
        //         dialog.dialog( "close" );
        //     }
        // },
        close: function() {
            form[ 0 ].reset();
            allFields.removeClass( "ui-state-error" );
            // $('#coachesGrid').html(coachesContent);
            $('#coachesGrid').show();
            $('#btn-add').show();
        }
    });

    form = dialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
        addCoach();
    });

    $("#btn-add").click(function () {
        $('#coachesGrid').hide();
        $('#btn-add').hide();
        $('#btn-update').hide();
        $("#btn-save").show();
        dialog.dialog("open");
    });

    $("#btn-cancel").click(function() {
        dialog.dialog( "close" );
    });
    $("#btn-save").click(addCoach);
    $("#btn-update").click(updateCoach);
}

function deleteClick(event) {
    console.log("deleting: ", event);

    var id = event.target.parentElement.id;
    var coach = document.getElementById('' +id);

    var username = coach.getElementsByClassName("username")[0].innerText;
    console.log(APIS.API_GET_COACHES + "\/" + username);
    callServer(APIS.API_GET_COACHES + "\/" + username, HTTP_METHODS.DELETE, removeCoachFromGrid(id));

}

function removeCoachFromGrid(id) {
    var coach = document.getElementById('' +id);
    document.getElementById('coachesGrid').removeChild(coach);
    freeIds.push(id);
}

function updateClick(coachInfo) {
    var id = coachInfo.target.parentElement.parentElement.id;
    var coach = document.getElementById('' +id);

    var username = coach.getElementsByClassName("username")[0].innerText;
    var name = coach.getElementsByClassName("name")[0].innerText;
    var password = coach.getElementsByClassName("password")[0].innerText;
    var email = coach.getElementsByClassName("email")[0].innerText;
    var birthDay = coach.getElementsByClassName("birthDay")[0].innerText;

    // email = email.replace("email: ","");
    console.log("updating: " , id, name, email);

    $("#username").val(username);
    $("#name").val(name);
    $("#password").val(password);
    $("#email").val(email);
    $("#birthDay").val(birthDay);


    $('#btn-update').show();
    $('#btn-update').attr("name", id);
    $('#coachesGrid').hide();
    $('#btn-add').hide();
    $("#btn-save").hide();

    // $('label[for=username], input#username').hide();
    dialog.dialog("open");
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

function showCoachPopup(coach){
    if(popupState === POPUP_STATE.ADD){
        coach = new Object();
        coach.username = "";
        coach.email = "";
        coach.birthDay = new Date();
        coach.name = "";
    }
    var coachId = coach.username;
    coachDialog.content.coachId.text(coachId);
    coachDialog.content.username.val(coach.username);
    coachDialog.content.email.val(coach.email);
    coachDialog.content.title.val(coach.name);
    coachDialog.content.password.val(coach.password);
    coachDialog.content.birthday.val(formatDate(coach.birthDay));
    var url = $(this).parent().parent().children('.coachImage').css('background-image');
    coachDialog.content.image.css('backgroundImage', url);

    if(popupState === POPUP_STATE.ADD){
        coachDialog.content.addButton.show();
        coachDialog.content.updateButton.hide();
        coachDialog.content.deleteButton.hide();
        coachDialog.content.feedbacks.hide();
        closeLoader();
        showDialog();
    }
    else{
        coachDialog.content.addButton.hide();
        coachDialog.content.updateButton.show();
        coachDialog.content.deleteButton.show();
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

function coachToString(coach){
    var array = ["username", "email", "birthDay"];
    var r = "";
    for(var index in array){
        var key = array[index];
        r += "<b>{0}</b> : {1}<br>".format(key, key !== 'birthDay' ? coach[key] : formatDate(coach[key]));
    }
    return r;
}

function showCoaches(){
    var coachThumb, content, divImage;
    coachesDiv.empty();
    for(var username in coaches){
        var coach = coaches[username];

        coachThumb = $("<div></div>");
        coachThumb.data("coachId", username);
        coachThumb.addClass("coach_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");

        content = $("<div></div>");
        content.addClass("content");

        divImage = $('<div></div>');
        divImage.addClass('coachImage')
            .css('background-image', 'url(images/box-{0}.jpg)'.format(Math.floor(Math.random() * 3) + 1));
        content.append(divImage);

        var divInfo = $("<div> </div>");
        divInfo.addClass("info")
            .append(
                $("<div></div>").addClass("title").html(coach.name).click(function(){
                    var coachDiv = $(this).parent().parent().parent();
                    var coachId = coachDiv.data("coachId");
                    popupState = POPUP_STATE.EDIT;
                    showCoachPopup(coaches[coachId]);
                })
            )
            .append(
                $("<div></div>").addClass("description").html("<p>{0}</p>".format(coachToString(coach)))
            )
            .append(
                $("<div>Edit coach</div>").addClass("title edit-button").click(function(){
                    popupState = POPUP_STATE.ADD;
                    showCoachPopup();
                })
            );
        content.append(divInfo);

        coachThumb.append(content);
        coachesDiv.append(coachThumb);
    }

    coachThumb = $('<div ></div>');
    coachThumb.data("coachId", "none");
    coachThumb.addClass("coach_thumbnail col-xs-12 col-sm-4 col-md-3 col-lg-2 wow animated fadeInUp ");
    content = $('<div></div>');
    content.addClass("content");
    divImage = $('<button></button>');
    divImage.addClass('coachImage')
        .css('background-image', 'url(images/box-add.png)');
    divImage.css('background-size','100%').css('border','none').click(function(){
        popupState = POPUP_STATE.ADD;
        showCoachPopup();
    });
    content.append(divImage);
    coachThumb.append(content);
    coachesDiv.append(coachThumb);
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
        coaches[coach.username] = coach;
    }
    showCoaches();
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
function getCoach(){
    var coach = {
        name        : coachDialog.content.title.val(),
        username    : coachDialog.content.username.val(),
        password    : coachDialog.content.password.val(),
        email       : coachDialog.content.email.val(),
        birthDay    : coachDialog.content.birthday.val()
    };
    if(!validateObject(coach, OBJECT_KEYS.COACH)){
        showError("Script error. ", "Not all values in coach");
        return undefined;
    }
    var errors = [];
    if(!coach.name || coach.name === '')
        errors.push("Name must be non-empty!");

    if(!coach.username || coach.username === '')
        errors.push("Username must be non-empty!");


    if(!coach.password || coach.password === '')
        errors.push("Password must be non-empty!");


    if(!coach.email || coach.email === '')
        errors.push("Email must be non-empty!");

    if(errors.length > 0){
        var error = errors.join("\n");
        showError(error, error);
        return undefined
    }

    return coach;
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
    coachDialog.real.click(function(){event.preventDefault();return false;})
    coachDialog.loading = coachDialog.real.children(".loading");
    coachDialog.content = coachDialog.real.children(".popup-content");
    coachDialog.content.coachId = coachDialog.content.find('.coachId');
    coachDialog.content.image = coachDialog.content.children(".bannerImage");
    coachDialog.content.closeButton = coachDialog.content.children(".close-button");
    coachDialog.content.title = coachDialog.content.find(".title");
    coachDialog.content.username = coachDialog.content.find(".username input");
    coachDialog.content.email = coachDialog.content.find(".email input");
    coachDialog.content.birthday = coachDialog.content.find(".birthday input");
    coachDialog.content.password = coachDialog.content.find(".password input")

    coachDialog.content.addButton = coachDialog.content.find(".button.add");
    coachDialog.content.addButton.click(function(){
        var coachId = coachDialog.content.coachId.text();
        var coach = getCoach();
        if(coach === undefined) return;
        callServer(APIS.API_GET_COACHES, HTTP_METHODS.POST, coach, function(data){

            coaches[coach.username] = coach;
            showCoaches();
            closeDialog();
        })
    });
    coachDialog.content.updateButton = coachDialog.content.find(".button.update");
    coachDialog.content.updateButton.click(function(){
        var coachId = coachDialog.content.coachId.text();
        var coach = getCoach();
        if(coach === undefined) return;
        callServer(APIS.API_COACH.format(coachId), HTTP_METHODS.PUT, coach, function(data){
            coaches[coachId] = coach;
            showCoaches();
            closeDialog();
        });
    });
    coachDialog.content.deleteButton = coachDialog.content.find(".button.delete");
    coachDialog.content.deleteButton.click(function(){
        var coachId = coachDialog.content.coachId.text();
        callServer(APIS.API_COACH.format(coachId), HTTP_METHODS.DELETE, {},  function(data){
            delete coaches[coachId];
            showCoaches();
            closeDialog();
        })
    });
    coachDialog.content.feedbacks = coachDialog.content.children(".feedbacks");
}


function init(){
    coachesDiv = $('#coachesGrid');
    if(coachesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }



    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_MANAGE_COACHES},
        function() {
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET,
                {}, function(data){
                    if(!loadUser(data))
                        return;

                    initDialog();
                    callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches);
                })
        }
    );
};

$(init);
