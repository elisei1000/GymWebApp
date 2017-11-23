$(document).ready(function() {
    console.log("OK");
    findAll();
});
function findAll(){
    $.ajax({
        type: "POST",
        url:"http://localhost:8080/login",
        dataType:"json",
        success: function(data, textStatus, jqXHR) {
            // handle your successful response here
            alert(data);
        },
        error: function(xhr, ajaxOptions, thrownError) {
            // handle your fail response here
            alert("Error");
        }
    });
}