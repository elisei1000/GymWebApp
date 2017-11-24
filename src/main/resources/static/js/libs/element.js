"use strict";
$.noConflict();
var $ = jQuery;

$(document).ready(function($) {

    // smooth scroll js 
    $('.scrollbtn a[href*="#"]:not([href="#"])').on("click", function() {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: target.offset().top
                }, 1000);
                return false;
            }
        }
    });

    // menu active js 
    $('.navbar-nav li a').on("click", function(e) {
        $('.navbar-nav li').removeClass('active');
        var $parent = $(this).parent();
        if (!$parent.hasClass('active')) {
            $parent.addClass('active');
        }
    });

    //  NAVBAR CLOSE ICON 
    $(".navbar-toggle").on("click", function() {
        $(this).toggleClass("active");
        $("#header").toggleClass("headClr");
        $("body").toggleClass("popup-open");
    });

    $('.main-menu ul li a').on("click", function(){
        $("body").removeClass("popup-open");
    });

    //  NAVBAR OPEN/CLOSE 
    function resMenu() {
        if ($(window).width() < 1200) {

            $('.main-menu ul li a').on("click", function() {
                $(".navbar-collapse").removeClass("in");
                $(".navbar-toggle").addClass("collapsed").removeClass("active");
                $("#header").removeClass("headClr");
            });
        }
    }

    resMenu();

    // Back To top 
    var offset = 300,
        offset_opacity = 1200,
        scroll_top_duration = 700,
        $back_to_top = $('.back-to-top');

    $(window).scroll(function() {
        ($(this).scrollTop() > offset) ? $back_to_top.addClass('cd-is-visible'): $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if ($(this).scrollTop() > offset_opacity) {
            $back_to_top.addClass('cd-fade-out');
        }

        // on scroll header reduce js  
        var scroll = $(window).scrollTop();
        if (scroll >= 100) {
            $("#header").addClass("fixed");
        } else {
            $("#header").removeClass("fixed");
        }

        if ($(window).width() < 767) {
            $("#header").removeClass("fixed");
        }

    });

    $back_to_top.on('click', function(event) {
        event.preventDefault();
        $('body,html').animate({
            scrollTop: 0,
        }, scroll_top_duration);
    });

    // wow animation Js
    new WOW().init();

    // Owl Caoursel for deal slider
    $("#owl-deal").owlCarousel({
        items: 3,
        loop: true,
        mouseDrag: true,
        nav: true,
        dots: false,
        responsive: {
            1200: {
                items: 3
            },
            1000: {
                items: 3
            },
            600: {
                items: 2
            },
            200: {
                items: 1
            }
        }

    });

    // fancybox popup for gallery slider images
    $(".grouped_gallery").fancybox({
        helpers: {
            overlay: {
                locked: false
            }
        }
    });

    // Owl Carousle for testimonial slider
    $('#owl-testimonial').owlCarousel({
        autoHeight: false,
        loop: true,
        items: 1,
        margin: 0,
        nav: false,
        dots: false
    });

    // Owl Caoursel for news slider
    $("#owl-news").owlCarousel({
        items: 4,
        loop: true,
        mouseDrag: true,
        nav: true,
        dots: false,
        responsive: {
            1200: {
                items: 4
            },
            767: {
                items: 3
            },
            690: {
                items: 2
            },
            200: {
                items: 1
            }
        }

    });

    // Owl Carousle for Gallery slider
    $('#owl-news-list').owlCarousel({
        autoHeight: false,
        loop: true,
        items: 1,
        margin: 0,
        nav: false,
        dots: false
    });

    // Animate slowly to comment section on clicking reply
    $(".comment-reply").on('click', function() {
        if (this.hash !== "") {
            var hash = this.hash;
            var headHeight = $("#header").height();
            $('html, body').animate({
                scrollTop: $(hash).offset().top - headHeight
            }, 800, function() {
                window.location.hash = hash;
            });
        }
    });

    // video sections for news page
    $('.video-block #work-video, #work-video-frame').css("visibility", "hidden");
    $(".video-block").on('click', function() {
        var video = $("#work-video").get(0);
        video.play();
        $('.video-block #work-video').css("visibility", "visible");
        $(this).css("visibility", "hidden");
        return false;
    });

    $(".video-iframe").on('click', function() {
        $('#work-video-frame').css("visibility", "visible");
    });

    // Isotope Js for Gallery and Timetable Sections
    var $container = [$('#classbox'), $('#gallery-box')],
        $optionSets = [$('.button-group'), $('.gallery-button-group')];

    $('.class-grids').isotope({
        itemSelector: '.celement',
        filter: '.sun'
    });
    $('.gallery-grid').isotope({
        itemSelector: '.grid-item',
        masonry: {
            columnWidth: 100,
            gutter: 15
        }
    });

    //Initialize filter links for each option set in isotope 
    jQuery.each($optionSets, function(index, object) {

        var $optionLinks = object.find('button');

        $optionLinks.on("click", function(){
            var $this = $(this),
                $optionSet = $this.parents('.isogrp'),
                options = {},
                key = $optionSet.attr('data-option-key'),
                value = $this.attr('data-filter');
            // don't proceed if already is-checked
            if ($this.hasClass('is-checked')) {
                return false;
            }

            $optionSet.find('.is-checked').removeClass('is-checked');
            $this.addClass('is-checked');

            // make option object dynamically, i.e. { filter: '.my-filter-class' }

            // parse 'false' as false boolean
            value = value === 'false' ? false : value;
            options[key] = value;
            if (key === 'layoutMode' && typeof changeLayoutMode === 'function') {
                // changes in layout modes need extra logic
                changeLayoutMode($this, options);
            } else {
                // otherwise, apply new options

                $container[index].isotope(options);
            }

            return false;
        });
    });


  /*********************************************** Material Effect On Tab Begins *************************************************/
    // Intial Border Position
    var activePos = $('.tabs-header .is-checked').position();
        $('.border').stop().css({
            left: 0,
            width: $('.tabs-header .is-checked').width()
        });
    // Change Position
    function changePos() {

        // Update Position
        activePos = $('.tabs-header .is-checked').position(); 

        // Change Position & Width
        $('.border').stop().css({
            left: activePos.left,
            width: $('.tabs-header .is-checked').width()
        });
    }

    //changePos();

    // Tabs
    $('.tabs-header .button').on('click', function(e) {
    e.preventDefault();
    changePos();
    });

  /********************************************************* Material Effect On Tab Ends **************************************************/

});

window.onload = function() {
    var cur_url = window.location.href;
    var animate_val = (cur_url).split("#")[1];
    if (animate_val) {
        if ($(window).width() < 768) {
            $('html,body').animate({
                    scrollTop: $('[id="' + animate_val + '"]').offset().top - 75
                },
                1000);
            return false;
        } else {
            $('html,body').animate({
                    scrollTop: $('[id="' + animate_val + '"]').offset().top - 50
                },
                1000);
            return false;
        }
    }
};