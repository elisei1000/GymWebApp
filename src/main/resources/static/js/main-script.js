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
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}
