/*==============================
 STYLE SWITCHER SCRIPT INSTALLATION
 ===============================*/

(function($) {
    "use strict";

    $(".style1" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style1.css" );
        return false;
    });

    $(".style2" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style2.css" );
        return false;
    });

    $(".style3" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style3.css" );
        return false;
    });

    $(".style4" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style4.css" );
        return false;
    });

    $(".style5" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style5.css" );
        return false;
    });

    $(".style6" ).click(function(){
        $("#colors" ).attr("href", "css/switcher/style6.css" );
        return false;
    });

    // Style Switcher
    $('#style-switcher').animate({
        right: '-190px'
    });

    $('#style-switcher h4 a').click(function(e){
        e.preventDefault();
        var div = $('#style-switcher');
        console.log(div.css('right'));
        if (div.css('right') === '-190px') {
            $('#style-switcher').animate({
                right: '0px'
            });
        } else {
            $('#style-switcher').animate({
                right: '-190px'
            });
        }
    });

    $('.colors li a').click(function(e){
        e.preventDefault();
        $(this).parent().parent().find('a').removeClass('active');
        $(this).addClass('active');
    });

})(jQuery);
