/**
 * Created by elisei on 24.11.2017.
 */

function showError(message, errorThrown){
    alert(message);
    console.log(errorThrown !== undefined ? errorThrown:message);
}

function redirectPage(page){
    window.location = PAGES_MAPPINGS[page];
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
        if(onnotlogged !== undefined) onnotlogged();
        else redirectLogin();
        return;
    }
    if(status === STATUSES.STATUS_PERMISSION_DENIED){
        if(onnotpermission !== undefined) onnotpermission();
        else redirectHome()
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

function callServer(api, method, data, onsuccess, onnotloggedin, onpermissiondenied){
    if(data === undefined) data = {};
    $.ajax({
        url : api,
        method : method,
        data : data,
        dataType: "text",
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
