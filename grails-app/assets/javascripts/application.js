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

    //hide inner modals and their backdrops after hiding the outer modal
    $('.modal').on('hidden.bs.modal', function (e) {
        if ($('.modal:visible').length == 0) {
            hideModals();
        }
    });
});

function hideModals() {
    $('.modal-backdrop').remove();
    $('.modal').modal('hide');
}
