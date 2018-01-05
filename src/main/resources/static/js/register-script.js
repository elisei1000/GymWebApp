/**
 * Created by Diana on 1/5/2018.
 */


function submitRegister(){

    // callServer(APIS.API_POST_REGISTER, HTTP_METHODS.POST, {"username": $('#username').val(), "password": $('#password').val(), "email": $('#email').val(), "name": $('#name').val(), "birthday": $('#birthday').val()});


    $.ajax({
        url: '/user-register',
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify( { "username": $('#username').val(), "password": $('#password').val(), "email": $('#email').val(), "name": $('#name').val(), "birthDay": $('#birthDay').val() } ),
        processData: false,
        success: function( data, textStatus, jQxhr ){
            console.log(data);
            if(data.errors==0){
                alert("You have registered successfully!");
                window.location.href='/main.html';}
            else{
                alert("Incorect data!");
            }
        },
        error: function( jqXhr, textStatus, errorThrown ){

        }

    });

}