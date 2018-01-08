function submitLogin() {

    console.info("Attempting to authenticate");
     var form = $('#login-form')[0];

    		// Create an FormData object
            var data = new FormData(form);

            data.append("username", $('#username').val());
            data.append("password", $('#password').val());

    $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/login",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
        success: function (data) {
            var response = jQuery.parseJSON(data);
            if(response.errors==0){
                alert("You have logged in successfully!");
                window.location.href='/main.html';}
                else
                {
                alert("Login failure!");
                }

        }
    });
}
$(function() {
    $('form').each(function() {
        $(this).find('input').keypress(function(e) {
            // Enter pressed?
            if(e.which == 10 || e.which == 13) {
                submitLogin();
            }
        });

        $(this).find('input[type=submit]').hide();
    });
});