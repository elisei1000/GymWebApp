/**
 * Created by diana on 29.12.2017.
 */

var program=[{id:1,difficultyLevel:0,startHour: new Date("2017/12/13 7:00 AM"),endHour:new Date("2017/12/13 7:00 PM"),startDate:"2017/12/12",endDate:"2017/12/20",maxPlaces:10 },]
$(function() {
    $("#scheduler").kendoScheduler({
    date: new Date(), // The current date of the scheduler
        dataSource: [
            {
                id: program[0].id, // Unique identifier.
                start: program[0].startHour, // Start of the event
                end: program[0].endHour, // End of the event
                title: "Course "+program[0].id // Title of the event
            },
        ]
    });});
