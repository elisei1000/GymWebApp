/**
 * Created by diana on 29.12.2017.
 */


DevExpress.viz.currentTheme("generic.light");

$(function(){

    $("#scheduler").dxScheduler({
        dataSource: data,
        views: ["day","week", "month"],
        currentView: "week",
        currentDate: new Date(),
        startDayHour: 9,
        height: 600,
        onCellClick: function(e) {
            e.cancel = true;
        },
        onCelDblClick: function(e) {
            e.cancel = true;
        },
        onAppointmentClick: function(e) {
            e.cancel = true;
        },
        onAppointmentDblClick: function(e) {
            e.cancel = true;
        }
    }).dxScheduler("instance");

});

/*var data = $.ajax({
    url: "/course",
    async: false,
    dataType: 'json'
}).responseJSON;
console.log(data);
*/
var data =[
    {
        "id": 1,
        "difficultyLevel": 0,
        "startHour": "09:00:00+01:00",
        "endHour": "10:00:00+01:00",
        "startDate": "2018-01-04T09:00:00+01:00",
        "endDate": "2018-01-04T10:00:00+01:00",
        "maxPlaces": 10,
        "text": "Curs1"
    },
    {
        "id": 2,
        "difficultyLevel": 0,
        "startHour": "09:00:00+01:00",
        "endHour": "10:00:00+01:00",
        "startDate": "2018-01-05T09:00:00+01:00",
        "endDate": "2018-01-05T10:00:00+01:00",
        "maxPlaces": 10,
        "text": "Curs2"
    }
];
