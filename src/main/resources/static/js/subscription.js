/**
 * Created by Diana on 1/12/2018.
 */
/**
 * Created by Diana on 1/5/2018.
 */
function showSubscriptions() {


    $.ajax({
        url: '/cuser/subscription',
        success: function (data) {


            if(data.errors == "") {

                debugger;
                $('#idp').html("The subscription is available between:");
                $('#myID1').css("display", "block");
                $('#myID2').css("display", "block");
                var date1 = new Date(data.data.subscription.startDate).toLocaleString();
                var date2 = new Date(data.data.subscription.endDate).toLocaleString();

                $('#myID1').text(date1.split(',')[0]);
                $('#myID2').text(date2.split(',')[0]);

            }
            else
            {

                $('#idp').html("No subscriptions available!");
                $('#myBtn').css("display", "block");

            }

        },
        error: function (err) {
            $('#idp').html("Nu aveti abonamente disponibile!");
            $('#button').css("display", "inline");

        }
    });

}


showSubscriptions();

function addSubscription() {
    debugger;
    $.ajax({
        url: '/cuser/subscription',
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify( { "endDate": Date.parse($('#endDate').val().toString()) } ),
        processData: false,
        success: function( data, textStatus, jQxhr ){
            console.log(data);
            if(data.errors==0){
                alert("You have registered successfully!");
                window.location.href='/subscription.html';}
            else{
                alert("Incorect data!");
            }
        },
        error: function( jqXhr, textStatus, errorThrown ){

        }

    });

}