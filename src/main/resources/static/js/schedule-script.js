/**
 * Created by diana on 29.12.2017.
 */

DevExpress.viz.currentTheme("generic.light");

if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] !== 'undefined'
                ? args[number]
                : match
                ;
        });
    };
}

function loadSchedule(filteredData){
    $("#scheduler").dxScheduler({
        dataSource: filteredData,
        views: ["day","week", "month"],
        currentView: "week",
        currentDate: new Date(),
        startDayHour: 8,
        endDayHour: 22,
        height: 500,
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

}


function formatDate(date, hours) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    minutes = "00";
    seconds = "00";
    hours = "" + hours;

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    if(hours.length < 2) hours = '0' + hours;
    if(minutes.length < 2) minutes = '0' + minutes;
    if(seconds.length < 2) seconds = '0' + seconds;

    return "{0} {1}".format(
            [year, month, day].join('-'),
            [hours, minutes, seconds].join(":")
    )
}

function loadCourses(data){
    var courses = data.courses;

    data = courses.map(function(course) {
            return {
                text: course.title,
                startDate: formatDate(course.startDate, course.startHour),
                endDate: formatDate(course.endDate, course.endHour),
                startHour: course.startHour,
                endHour:course.endHour,
                difficultyLevel:course.difficultyLevel
            }
        }
    );

    var toAdd = [];
    for(var index in data){
        var course = data[index];
        if(course.startDate.substring(0, 10) !== course.endDate.substring(0, 10)){
            var startDate  = course.startDate;
            var endDate = course.endDate;
            course.endDate = startDate;

            var s = new Date(startDate);
            var e = new Date(endDate);
            var count = (e.getTime() - s.getTime()) / (24 * 3600 * 1000);
            for(var z=1;z<count;z++){
                s.setTime(s.getTime() + 24 * 3600 *1000);

                var newCourse = {
                    text:course.text,
                    startDate:formatDate(s.getTime(), course.startHour),
                    endDate:formatDate(s.getTime(), course.endHour),
                    difficultyLevel:course.difficultyLevel
                };
                toAdd.push(newCourse);
            }
        }
    }
    data = data.concat(toAdd);
    loadSchedule(data);

    dataAll=data;

    data0 = data.filter(function(course) {
        return course.difficultyLevel === 0;
    });
    data1 = data.filter(function(course) {
        return course.difficultyLevel === 1;
    });
    data2 = data.filter(function(course) {
        return course.difficultyLevel === 2;
    });
    data3 = data.filter(function(course) {
        return course.difficultyLevel === 3;
    });
    data4 = data.filter(function(course) {
        return course.difficultyLevel === 4;
    });

    $('#filtersDifficulty input').on('change', function() {
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="all"){
            loadSchedule(dataAll);
        }
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="easy"){
            loadSchedule(data0);
        }
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="easymedium") {
            loadSchedule(data1);
        }
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="medium"){
                loadSchedule(data2);
        }
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="mediumhard"){
            loadSchedule(data3);
        }
        if($('input[name=options]:checked', '#filtersDifficulty').val()==="hard"){
            loadSchedule(data4);
        }
    });

}

$(function(){
    callServer(APIS.API_GET_COURSES, HTTP_METHODS.GET, {}, loadCourses);
})


