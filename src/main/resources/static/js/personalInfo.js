/**
 * Created by Diana on 1/12/2018.
 */
/**
 * Created by Diana on 1/5/2018.
 */

var block;
var subscription;

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

function loadSubscription(){
    callServer(APIS.API_GET_SUBSCRIPTION, HTTP_METHODS.GET, {}, function(data){
        if(!("subscription" in data)){
            showError("Invalid data received from server!",
            'Invalid data received from server. {0}'.format(JSON.stringify(data)));
            return;
        }

        if(!("startDate" in data.subscription) || !("endDate" in data.subscription)){
            showError("Invalid data received from server!",
                'Invalid data received from server. {0}'.format(JSON.stringify(data)));
            return;
        }



        subscription = data.subscription;
        if(subscription.endDate !== 0 ){
            block.existingSubscription.date.val(formatDate(subscription.endDate));
            block.existingSubscription.show();
            block.nonExistingSubscription.hide();
        }
    });
}

function initComps(){
    block = $(".personalInfoBlock");
    block.existingSubscription = block.find(".existingSubscription");
    block.existingSubscription.date = block.existingSubscription.children("#subscription");
    block.existingSubscription.extendButton = block.existingSubscription.children(".button.update");
    block.existingSubscription.extendButton.click(function(){
        if(confirm('Are you sure? This will extend your subscription by 2 weeks')){
            var newEndDate =     subscription.endDate + 2 * 1000 * 24 * 3600 * 7;
            callServer(APIS.API_GET_SUBSCRIPTION, HTTP_METHODS.PUT,
                {
                    endDate: newEndDate
                },
                function(){
                    subscription.endDate = newEndDate;
                    block.existingSubscription.date.val(formatDate(subscription.endDate));
                })
        }

    });
    block.nonExistingSubscription = block.find(".nonExistingSubscription");
    block.nonExistingSubscription.addButton = block.nonExistingSubscription.children(".button.add");
    block.nonExistingSubscription.addButton.click(function(){
        if(confirm("Are you sure? This will add an subscription of 2 weeks for GymWEbApp")){
            var newEndDate = new Date().getTime() + 2 * 1000 * 24 * 3600 * 7;
            callServer(APIS.API_GET_SUBSCRIPTION, HTTP_METHODS.POST,{
                endDate: newEndDate
            },
            function(){
                subscription = {};
                subscription.endDate = newEndDate;
                subscription.startDate = new Date();
                block.existingSubscription.date.val(formatDate(subscription.endDate));
                block.existingSubscription.show();
                block.nonExistingSubscription.hide();
            })
        }
    });


}


function init(){
    initComps();
    callServer(APIS.API_HAS_PERMISSION, HTTP_METHODS.GET, {page: PAGE_PERSONAL_INFO },
        function(){
            callServer(APIS.API_GET_CURRENT_USER, HTTP_METHODS.GET, {},
                function(data){
                    if(!("user" in data)){
                        showError("Invalid data received from server!",
                            "User not in JSON: {0}".format(JSON.stringify(data)));
                        return false;
                    }
                    if(!validateObject(data.user, OBJECT_KEYS.CUSER)){
                        showError("Invalid data received from server!",
                            "User hasn't all necessary fields! {0}".format(
                                JSON.stringify(data.user)
                            ));
                        return false;
                    }
                    $('#name').val(data.user.name);
                    $('#username').val(data.user.username);
                    $('#email').val(data.user.email);
                    $('#birthDay').val(formatDate(data.user.birthDay));

                    loadSubscription();
            });
        }
    )
}

$(init);