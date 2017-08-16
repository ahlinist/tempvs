// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require /webjars/jquery/3.2.1/jquery.min
//= require /webjars/bootstrap/3.3.7-1/js/bootstrap.min
//= require_tree .
//= require_self

$(function (){
    //preventing double clicking
    $('.disableable').click(function() {
        $(this).attr("disabled", true);
    });

    //enabling tooltips for glyphiconed buttons
    $('[data-toggle="tooltip"]').tooltip();

    //incrementing lastActive
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
