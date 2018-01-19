/**
 * Created by Diana on 1/5/2018.
 */
function getUser(){
    var user = {
        "username": $('#username').val(),
        "password": $('#password').val(),
        "email": $('#email').val(),
        "name": $('#name').val(),
        "birthDay": $('#birthDay').val()
    };
    var errors = [];
    if(user.username === "" || user.username === null){
        errors.push("Invalid user inserted");
    }
    if(user.password === "" || user.password === null)
        errors.push("Invalid password inserted");
    if(user.email === "" || user.email === null){
        errors.push("Invalid email inserted");
    }

    if(user.name === "" || user.name === null){
        errors.push("Invalid name inserted");
    }

    if(user.birthday === null){
        errors.push("Invalid birthday");
    }
    if(errors.length !== 0){
        console.log(user);
        showError(errors.join("\n"), errors.join("\n"));
        return undefined;
    }
    return user;

}

function submitRegister(){
    var user  = getUser();
    if(user === undefined)
        return;

    callServer(APIS.API_POST_REGISTER, HTTP_METHODS.POST, user,
        function(){
            showMessage("Your register request was successfully made!");
    });
}