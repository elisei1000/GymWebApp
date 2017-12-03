/**
 * Created by Ramo on 24.11.2017.
 */

var coaches=[{name:"Anamaria Popa", email:"a@email.com", username:"us1"},
    {name:"Mihai Andrei", email:"a2@email.com", username:"us2"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},
    {name:"Constantin Popovici", email:"a3@email.com", username:"us3"},];

function render_coaches_list() {
    $("#coaches-list").html("");
    for(var i=0; i<coaches.length; i++){
        $("#coaches-list").append("<li id=coaches[i].username>"+
            "<div" +
            " style=' border-width: 0.5px;" +
            " border-bottom: 0.5px #999b9e solid; border-top: 0.5px #999b9e solid'>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: xx-large; margin-top: 25px'>"
            +  coaches[i].name +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: x-large'>" + "Want an advice? Contact me: "
            + coaches[i].email +"</div>"
            + "<div style='color: #FFFFFF; text-align: center; font-size: large; margin-bottom: 25px'>" + "Liked me? Tell everyone "
            + "<a href='#feedback-section'>here.</a>"
            + "</div>"
            +"</div>"
              +"</li>");
    }
}

function init(){
    render_coaches_list();
}

$(init);


