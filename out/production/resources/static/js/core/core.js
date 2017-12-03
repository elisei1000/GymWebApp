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

function verifyRights(data, onsuccess){
    var message;
    try{
        data = JSON.parse(data);
    }
    catch(e){
        showError("Invalid data received from server", e.toString());
        return;
    }

    if(!('data' in data) || !('status' in data.data) ||  !('errors' in data)){
        message = "Invalid data received from server: Important fields are missing";
        console.log(data);
        showError(message);
        return;
    }

    var status = data.data.status;
    if(status === STATUSES.STATUS_NOT_LOGGED_IN){
        redirectPage(PAGES.LOGIN);
        return;
    }
    if(status === STATUSES.STATUS_PERMISSION_DENIED){
        redirectPage(PAGES.HOME);
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

function callServer(api, method, data, onsuccess){
    if(data === undefined) data = {};
    $.ajax({
        url : api,
        method : method,
        data : data,
        error: function(jqXHR, textStatus, errorThrown) {
            showError("Cannot communicate with server!",  errorThrown)
        },
        success: function(data) { verifyRights(data, onsuccess)}
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
