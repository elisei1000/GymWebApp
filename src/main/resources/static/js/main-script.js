function submitLogin(){
    $.ajax({
        url: '/login',
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify( { "username": $('#username').val(), "password": $('#password').val() } ),
        processData: false,
        success: function( data, textStatus, jQxhr ){
            console.log(data);
            if(data.errors==0){
            alert("You have logged in successfully!");
            window.location.href='/main.html';}
            else{
                alert("Invalid username or password!");
            }
        },
        error: function( jqXhr, textStatus, errorThrown ){

        }
    });
}
