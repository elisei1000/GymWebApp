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
                alert("You have logged in successfully!");
                window.location.href='/main.html';
        },
        error: function (data) {
            alert("Login failure");
        }
    });
}