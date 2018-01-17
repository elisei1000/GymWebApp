/**
 * Created by Diana on 1/12/2018.
 */
/**
 * Created by Diana on 1/5/2018.
 */

function personalInfo() {
    /*
     callServer(APIS.API_GET_CUSER, HTTP_METHODS.POST, {
     "username": $('#username').val(),
     "email": $('#email').val(),
     "name": $('#name').val(),
     "birthday": $('#birthday').val()
     });
     */


    $.ajax({
        url: '/cuser',
        success: function (data) {
            debugger;
            $('#name').val(data.data.user.name);
            $('#username').val(data.data.user.username);
            $('#email').val(data.data.user.email);
            var date = new Date(data.data.user.birthDay).toLocaleString();

            $('#birthDay').val(date.split(',')[0]);

        }
    });

}

//}
personalInfo();