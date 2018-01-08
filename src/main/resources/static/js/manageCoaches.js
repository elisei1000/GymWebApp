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
    name = $( "#name" ),
    email = $( "#email" ),
    birthday = $( "#birthday" ),
    allFields = $( [] ).add(username).add( name ).add( email ).add( birthday ),
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

    function updateCoach(data) {
        // console.log(data.target.parentElement);

        // console.log($("#name").val());
        // console.log($("#email").val());
        console.log($("#btn-update").attr("name"));

        var id = $("#btn-update").attr("name");


        var valid = true;
        // var name = $("#name");
        // var email = $("#email");

        valid = valid && checkLength( name, "name", 3, 16 );
        valid = valid && checkLength( email, "email", 6, 80 );
        valid = valid && checkRegexp( name, /^[A-Za-z\s]+$/i, "Name may consist of a-z, A-Z and spaces." );
        valid = valid && checkRegexp( email, emailRegex, "Invalid email format! (eg. ui@jquery.com)" );

        if(valid) {
            var coach = document.getElementById(id);
            coach.getElementsByClassName("nume")[0].innerText = name.val();
            coach.getElementsByClassName("email")[0].innerText = email.val();
            dialog.dialog( "close" );
        }
        // var newCoach = document.getElementById('coachesGrid').removeChild(coach);

    }

    function addCoach() {
        var valid = true;
        allFields.removeClass( "ui-state-error" );

        valid = valid && checkLength( username, "username", 3, 16 );
        valid = valid && checkLength( name, "name", 3, 16 );
        valid = valid && checkLength( email, "email", 6, 80 );
        valid = valid && checkLength( birthday, "birthday", 5, 16 );

        valid = valid && checkRegexp( username, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter." );
        valid = valid && checkRegexp( name, /^[A-Za-z\s]+$/i, "Name may consist of a-z, A-Z and spaces." );
        valid = valid && checkRegexp( email, emailRegex, "Invalid email format! (eg. ui@jquery.com)" );
        valid = valid && checkRegexp( birthday, /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/, "Invalid birthday field!" );

        var tempCoach = {};
        if(freeIds.length == 0)
            tempCoach.id = coaches.length;
        else {
            tempCoach.id = freeIds[0];
            freeIds.splice(0,1);
        }
        tempCoach["username"] = username.val();
        tempCoach["name"] = name.val();
        tempCoach["email"] = email.val();
        tempCoach["birthday"] = birthday.val();

        if ( valid ) {
            addCoachToGrid(tempCoach);
            // coaches.add(tempCoach);

            dialog.dialog( "close" );
        }
        return valid;
    }

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 600,
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
        // coachesContent = $('#coachesGrid').html();
        $('#coachesGrid').hide();
        $('#btn-add').hide();
        $('#btn-update').hide();
        $("#btn-save").show();
        $('label[for=username], input#username').show();
        $('label[for=birthday], input#birthday').show();
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
    removeCoachFromGrid(id);
    freeIds.push(id);
}
function removeCoachFromGrid(id) {
    // console.log("before: ", coaches);
    // coaches.splice(id, 1);
    // console.log("after: ", coaches);

    var coach = document.getElementById('' +id);
    document.getElementById('coachesGrid').removeChild(coach);

    // showCoaches();
}

function updateClick(coachInfo) {
    var id = coachInfo.target.parentElement.parentElement.id;
    var coach = document.getElementById('' +id);

    var name = coach.getElementsByClassName("nume")[0].innerText;
    var email = coach.getElementsByClassName("email")[0].innerText;

    email = email.replace("email: ","");
    console.log("updating: " , id, name, email, coach);

    $("#name").val(name);
    $("#email").val(email);


    $('#btn-update').show();
    $('#btn-update').attr("name", id);
    $('#coachesGrid').hide();
    $('#btn-add').hide();
    $("#btn-save").hide();

    $('label[for=username], input#username').hide();
    $('label[for=birthday], input#birthday').hide();

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
    divName.addClass("nume").html(coach.name);        //todo: manage click
    var divEmail = $("<div></div>");
    divEmail.html("email: {0}".format(coach.email))
        .addClass("email");

    var btnDelete = $("<button></button>");
    btnDelete.addClass("btn-delete").on("click", deleteClick);

    var divInfo = $("<div></div>").addClass("info");

    var btnUpdate = $("<button></button>").addClass("btn-update")
        .append(divName)
        .append(divEmail)
        .on("click", updateClick);

    divInfo.attr("id", coach.id);
    divInfo.append(btnUpdate).append(btnDelete);

    content.append(divInfo);

    coachThumb.append(content);
    coachesDiv.append(coachThumb);
}


function showCoaches(){
    coachesDiv.html("");
    for(var index in coaches){
        var coach = coaches[index];
        coach.id = index;
        addCoachToGrid(coach);
    }
}


function loadCoachesFromServer(data) {
    console.log(data);              //todo: delete this log

    if(!("coaches" in data)){
        console.log("coaches field is missing");
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