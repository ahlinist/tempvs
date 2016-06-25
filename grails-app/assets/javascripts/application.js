// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
    (function($) {
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
            $(this).fadeOut();
        });
    })(jQuery);
}

$(function (){
    incrementMinutes()
});

function incrementMinutes() {
    setTimeout(function() {
        $('.incrementMinutes').each(
            function() {
                var minutesAgo = parseInt($(this).attr('minutes'));

                if (minutesAgo < 30) {
                    $(this).attr("minutes", minutesAgo + 1).text(minutesAgo + 1 + ' ' + $('#mins-ago').text());
                } else {
                    $(this).text($('#half-hour-ago').text()).removeClass('incrementMinutes').removeAttr('minutes');
                }
            }
        );

        incrementMinutes();
    }, 60000);
}