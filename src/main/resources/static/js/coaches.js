/**
 * Created by Ramo on 24.11.2017.
 */

var coaches=[{name:"Anamaria Popa", email:"a@email.com", username:"us1"},
    {name:"Mihai Andrei", email:"a2@email.com", username:"us2"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us4"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us5"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us6"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us7"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us8"},];

var feedbacks=[{id:1, details:"ffff bine", author:"ramo", date:"19.02.2016"},
    {id:2, details:"excelent", author:"ramo2", date:"19.02.2016"},
    {id:3, details:"super", author:"ramo3", date:"19.02.2016"},
    {id:4, details:"minunat", author:"ramo4", date:"19.02.2016"},
    {id:5, details:"sexy", author:"ramo5", date:"19.02.2016"},
    {id:6, details:"incredibil", author:"ramo6", date:"19.02.2016"},
]

var my_feedback={id:1, details:"ffff bine", author:"ramo", date:"19.02.2016"}

function render_coaches_list() {
    $("#coaches-list").html("");
    for(var i=0; i<coaches.length; i++){
        $("#coaches-list").append("<li >"+
            "<div" +
            " style=' border-width: 0.5px;" +
            " border-bottom: 0.5px #999b9e solid; border-top: 0.5px #999b9e solid'>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: xx-large; margin-top: 25px'>"
            +  coaches[i].name +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: x-large'>" + "Want an advice? Contact me: "
            + coaches[i].email +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: large; margin-bottom: 25px'>" + "Liked me? Tell everyone "
            + "<a id='" + coaches[i].username + "' href='#feedback-section' onclick='render_feedbacks(this)'>here.</a>"
            + "</div>"
            +"</div>"
              +"</li>");
    }
}


function render_feedbacks(el){
    console.log($(el).attr('id'));
    $("#all-feedbacks").html("");
    for(var i=0; i<feedbacks.length; i++){
        if(feedbacks[i].author!='ramo') {
            $("#all-feedbacks").append("<div style='text-align: center; color: #FFFFFF; margin-top: 30px;'>" +
                "<div>" + "Author: " + feedbacks[i].author + " Date: " + feedbacks[i].date + "</div>"
                + "<div>" + feedbacks[i].details + "</div>"
                + "</div>")
        }
    }
    if(my_feedback!=''){
        $("#all-feedbacks").append("<div style='text-align: center; color: #FFFFFF; margin-top: 30px;'>" +
                "<div>Your feedback:</div>"+
            "<div>" + "Author: " + my_feedback.author + " Date: " + my_feedback.date + "</div>"
            + "<div style='width: 200px; text-align: center; margin:0 auto; color: #FFFFFF;'>" + my_feedback.details + "<a href='#'> Edit</a>" + "</div>"
            + "</div>")
    }

}

function init(){
    render_coaches_list();
}

$(init);


