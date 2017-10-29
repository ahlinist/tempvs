// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
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

    //hide inner modals and their backdrops after hiding the outer modal
    $('.modal').on('hidden.bs.modal', function (e) {
        if ($('.modal:visible').length == 0) {
            $('.modal-backdrop').remove();
            $('.modal').modal('hide');
        }
    });
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
