var coaches = [];

var coachesDiv;
var coachDialog;
var showDialog;


function coachClick() {
    console.log("Clicked");
}

function showCoaches(){
    for(var index in coaches){
        var coach = coaches[index];

        var coachThumb = $('<div ></div>');
        coachThumb.addClass("coach_thumbnail col-xs-6 col-sm-4 col-md-3 wow animated fadeInUp ");

        var content = $('<div></div>');
        content.addClass("content");

        var divImage = $('<div></div>');
        divImage.addClass('image').append($('<img />')
            .attr('src', 'images/coach-{0}.jpg'.format(coach.id)));
        content.append(divImage);

        var divEmail = $("<div></div>");
        divEmail.html("email: <span>{0}</span>".format(coach.email))
            .addClass("email");
        // content.append(divEmail);

        var divInfo = $("<div></div>");
        divInfo.addClass("info")
            .append(
                $("<div></div>").addClass("name").html(coach.name).click(coachClick)
            .append(divEmail)
            // )
            // .append(
            //     $("<div></div>").addClass("age").html("<p>{0}</p>".format(coach.birthday))
            );


        content.append(divInfo);

        coachThumb.append(content);
        coachesDiv.append(coachThumb);
    }
}


function loadCoaches(data) {
    console.log(data);

    if(!("coaches" in data)){
        console.log("coaches field is missing");
        showError("Invalid data received from server!", "Coache field not found in JSON!");
        return;
    }

    for(var index in data.coaches){
        var coach = data.coaches[index];
        if(!validateObject(coach, OBJECT_KEYS.MANAGE_COACHES)){
            showError("Invalid coaches received!", "Invalid coach: " + JSON.stringify(coach));
            return;
        }
    }

    coaches = data.coaches;
    console.log(coaches);
    showCoaches();
}


function init(){
    coachesDiv = $('#coachesGrid');
    if(coachesDiv.length === 0){
        showError("Invalid HTML!");
        return ;
    }


    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_MANAGE_COACHES});
    callServer(APIS.API_GET_COACHES, HTTP_METHODS.GET, {}, loadCoaches)
};


$(init);