var coaches = [];
var freeIds = [];

var coachesDiv;
var coachesContent;

var dialog;
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

//coach must have: name, email, username and birthday
function addCoachToGrid(coach) {
    var coachThumb = $('<div ></div>');
    coachThumb.addClass("coach_thumbnail col-xs-6 col-sm-4 col-md-3");
    coachThumb.attr("id", coach.id);

    var content = $('<div></div>');
    content.addClass("content");

    var divImage = $('<div></div>');
    divImage.addClass('image').css('background-image', 'url(images/coach-{0}.jpg)'.format(coach.username));// .attr('src', 'images/coach-{0}.jpg'.format(coach.id)));
    content.append(divImage);

    var divName = $("<div></div>");
    divName.addClass("name").html(coach.name);

    var divUsername = $("<div></div>");
    divUsername.addClass("username hidden").html(coach.username);
    var divBirthDay = $("<div></div>");
    divBirthDay.addClass("birthDay hidden").html(coach.birthDay);
    var divPassword = $("<div></div>");
    divPassword.addClass("password hidden").html(coach.password);

    var divEmail = $("<div></div>");
    divEmail.html(coach.email)
        .addClass("email");

    var btnDelete = $("<button></button>");
    btnDelete.addClass("btn-delete").on("click", deleteClick);

    var divInfo = $("<div></div>").addClass("info");

    var btnUpdate = $("<button></button>").addClass("btn-update")
        .append(divUsername)
        .append(divBirthDay)
        .append(divPassword)
        .append(divName)
        .append(divEmail)
        .on("click", updateClick);

    divInfo.attr("id", coach.id);
    divInfo.append(btnUpdate).append(btnDelete);

    content.append(divInfo);

    coachThumb.append(content);
    coachesDiv.append(coachThumb);
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

function showCoaches(){
    coachesDiv.html("");
    for(var index in coaches){
        var coach = coaches[index];
        coach.id = index;
        coach.birthDay = formatDate(new Date(coach.birthDay));
        // console.log("birthDay: ", formatDate(new Date(coach.birthDay)));

        addCoachToGrid(coach);
    }
}


function loadCoachesFromServer(data) {
    console.log("received coaches from server: ", data);              //todo: delete this log

    if(!("coaches" in data)){
        console.log("the coaches field is missing from json");
        showError("Invalid data received from server!", "Coache field not found in JSON!");
        return;
    }

    for(var index in data.coaches){
        var coach = data.coaches[index];
        if(!validateObject(coach, OBJECT_KEYS.COACH)){
            showError("Invalid coaches received!", "Invalid coach: " + JSON.stringify(coach));
            return;
        }
    }

    coaches = data.coaches;
    showCoaches();
    // $(".btn-delete").on("click", deleteClick);
    // $(".btn-update").on("click", updateClick);
}


function init(){
    coachesDiv = $('#coachesGrid');
    if(coachesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }

    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_MANAGE_COACHES});
    callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoachesFromServer);
    loadDialog();
};


$(document).ready(function () {
    $(init);
});