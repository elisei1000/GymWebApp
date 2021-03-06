/**
 * Created by elisei on 24.11.2017.
 */

var messageBox;

function createNotif(message){
    var notif = $("<div class='notif'></div>").text(message)
    notif.hide();
    notif.append(
        $("<div></div>").addClass("close").html("&times;").click(function(){
            if(notif.timeout)
                clearInterval(notif.timeout)
            notif.slideUp(function(){
                notif.remove();
            })
        })
    )
    return notif;
}

function showMessage(message){
    var split = message.split("\n");
    for(var i in split){
        var text = split[i];

        var notif = createNotif(text);
        notif.addClass("message");
        messageBox.append(notif);
        notif.fadeIn();
        notif.slideDown();
        notif.timeout = getTimeout(notif, 3000);
    }
}

function laterMessage(message){
    if(sessionStorage){
        sessionStorage.setItem("message", message)
    }
    else{
        showMessage(message);
    }
}

function getTimeout(notif, timeout){
    return setTimeout(function(){
        notif.slideUp(function(){
            notif.remove();
        })
    }, timeout);
}

function showError(message, errorThrown){
    var split = message.split("\n");
    for(var i in split){
        var text = split[i];

        var notif = createNotif(text);
        notif.addClass("error");
        messageBox.append(notif);
        notif.fadeIn();
        notif.slideDown();
        notif.timeout = getTimeout(notif, 5000);
    }
    console.log(errorThrown !== undefined ? errorThrown:message);
}

function redirectPage(page){
    window.location = PAGES_MAPPINGS_URL[page];
}

function redirectHome(){
    redirectPage(PAGE_HOME);
}

function redirectLogin(){
    redirectPage(PAGE_LOGIN);
}

function verifyRights(data, onsuccess, onnotlogged, onnotpermission){
    var message;
    if(typeof data === 'string'){
        try{
            data = JSON.parse(data);
        }
        catch(e){
            showError("Invalid data received from server", e.toString());
            console.log(data);
            return;
        }
    }
    //assume that data is an array (was parsed by jquery)

    if(!('data' in data) || !('status' in data.data) ||  !('errors' in data)){
        message = "Invalid data received from server: Important fields are missing";
        console.log(data);
        showError(message);
        return;
    }

    var status = data.data.status;
    if(status === STATUSES.STATUS_NOT_LOGGED_IN){
        if(onnotlogged !== undefined) onnotlogged(data.data);
        else{
            laterMessage("Your session has finished!");
            redirectLogin();
        }
        return;
    }
    if(status === STATUSES.STATUS_PERMISSION_DENIED){
        if(onnotpermission !== undefined) onnotpermission(data.data);
        else{
            laterMessage("You have not permissions to access this page!");
            redirectHome();
        }
        return;
    }
    if(status === STATUSES.STATUS_FAILED){
        message = data.errors.join('\n');
        showError(message);
        return;
    }
    if(onsuccess)
        onsuccess(data.data);
}

function callServer(api, method, data, onsuccess, onnotloggedin, onpermissiondenied,
                    contentType="application/json; charset=utf-8"){
    if(data === undefined) data = {};
    $.ajax({
        url : api,
        method : method,
        data : method.toUpperCase() === HTTP_METHODS.GET?data:JSON.stringify(data),
        dataType: "text",
        contentType: contentType,
        error: function(jqXHR, textStatus, errorThrown) {
            showError("Cannot communicate with server!",  errorThrown)
        },
        success: function(data) { verifyRights(data, onsuccess, onnotloggedin, onpermissiondenied)}
    });
}

function validateObject(object, keys){
    // keys is a string array with the keys of the object which must be in it
    for(var index in keys) {
        var key = keys[index];
        if (!(key in object))
            return false;
    }
    return true;
}

function generatePages(data){
    if (!("pages" in data)) {
        showError("Invalid data received from server!",
            "Invalid permissions response received from server {0}".format(JSON.stringify(data)))
        return;
    }
    var pages = data.pages;
    var toRemove = [PAGE_HOME, PAGE_LOGIN, PAGE_ABOUT, PAGE_REGISTER, PAGE_CONTACT, PAGE_SCHEDULE];
    pages = pages.filter(function (el) {
        return toRemove.indexOf(el) < 0;
    });

    pages.push(PAGE_SCHEDULE, PAGE_ABOUT, PAGE_CONTACT);
    var ul = $(".nav.navbar-nav");
    ul.empty();
    //ul.hide();
    for (var page in pages) {
        page = pages[page];

        ul.append($("<li></li>").append($("<a></a>")
            .text(PAGES_MAPPINGS_NAME[page])
            .attr("href", PAGES_MAPPINGS_URL[page])));
    }
    //ul.fadeIn();

}

function initMenuAndLinks() {
    callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET, {}, function(data){
        if(!("user" in data)){
            showError("Invalid data received from server!",
            "Invalid cuser response received from server {0}".format(JSON.stringify(data)));
            return;
        }
        var user = data.user;
        if(!validateObject(user, OBJECT_KEYS.CUSER)){
            showError("Invalid object received from server!",
            "Invalid user received from server {0}".format(JSON.stringify(data)));
            return;
        }
        var ul, lis, a, i;
        ul = $(".top-head .pull-right");
        lis = ul.children("li");
        a = $(lis.get(1)).children("a");
        i = a.children('i');
        a.removeAttr("href").html(user.username);
        a.prepend(i);

        a = $(lis.get(2)).children("a");
        i = a.children("i");
        a.attr("href", "/logout").html("Logout");
        a.prepend(i);

    }
    , function() {//onnotloggedin
        });

    callServer(APIS.API_PERMISSIONS, HTTP_METHODS.GET, {}, generatePages, generatePages, generatePages)

    if(sessionStorage && sessionStorage.getItem("message")){
        $(function(){
            showMessage(sessionStorage.getItem("message"));
            sessionStorage.removeItem("message");
        })

    }
    if(sessionStorage && sessionStorage.getItem("error")){
        $(function(){
            showError(sessionStorage.getItem("error"))
            sessionStorage.removeItem("error");
        })
    }
    messageBox = $("<div id='messageBox'></div>");
    $(document.body).append(messageBox);
}
$(initMenuAndLinks);

if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] !== 'undefined'
                ? args[number]
                : match
                ;
        });
    };
}
