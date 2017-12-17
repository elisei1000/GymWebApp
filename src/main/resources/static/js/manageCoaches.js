var coaches = [];

var coachesDiv;
var coachesContent;


function loadDialog() {
    var dialog, form,

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

    function addCoachFromInput() {

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
        tempCoach["username"] = username.val();
        tempCoach["name"] = name.val();
        tempCoach["email"] = email.val();
        tempCoach["birthday"] = birthday.val();

        if ( valid ) {
            addCoachToGrid(tempCoach);

            // $( "#users tbody" ).append( "<tr>" +
            //     "<td>" + name.val() + "</td>" +
            //     "<td>" + email.val() + "</td>" +
            //     "<td>" + birthday.val() + "</td>" +
            //     "</tr>" );
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
        addCoachFromInput();
    });

    $("#btn-add").click(function () {
        // coachesContent = $('#coachesGrid').html();
        $('#coachesGrid').hide();
        $('#btn-add').hide();
        dialog.dialog("open");
    });
    $("#btn-cancel").click(function() {
        dialog.dialog( "close" );
    });
    $("#btn-save").click(addCoachFromInput);
}

function coachClick(coachInfo) {
    console.log(coachInfo);
}

//coach must have: name, email, username and birthday
function addCoachToGrid(coach) {
    var coachThumb = $('<div ></div>');
    coachThumb.addClass("coach_thumbnail col-xs-6 col-sm-4 col-md-3");

    var content = $('<div></div>');
    content.addClass("content");

    var divImage = $('<div></div>');
    divImage.addClass('image').css('background-image', 'url(images/coach-{0}.jpg)'.format(coach.username));// .attr('src', 'images/coach-{0}.jpg'.format(coach.id)));
    content.append(divImage);

    var divName = $("<div></div>");
    divName.addClass("nume").html(coach.name);        //todo: manage click
    var divEmail = $("<div></div>");
    divEmail.html("email: <span>{0}</span>".format(coach.email))
        .addClass("email");

    var btnInfo = $("<button></button>");
    btnInfo.addClass("info")
        .append(divName)
        .append(divEmail).click(coachClick())
    // )
    // .append(
    //     $("<div></div>").addClass("age").html("<p>{0}</p>".format(coach.birthday))
    ;

    content.append(btnInfo);

    coachThumb.append(content);
    coachesDiv.append(coachThumb);
}


function showCoaches(){
    for(var index in coaches){
        var coach = coaches[index];

        addCoachToGrid(coach);
    }
}


function loadCoaches(data) {
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
}


function init(){
    coachesDiv = $('#coachesGrid');
    if(coachesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }


    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_MANAGE_COACHES});
    callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches);
    loadDialog();
};


$(document).ready(function () {
    $(init);
});