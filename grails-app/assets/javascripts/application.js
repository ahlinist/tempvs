// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require_tree .
//= require_self

var overlay = document.createElement('div');
overlay.classList.add('overlay');

function blockUI() {
    document.body.appendChild(overlay);
}

function unblockUI() {
    document.body.removeChild(overlay);
}

$(function (){
    //preventing double clicking
    $('.disableable').click(function() {
        $(this).attr("disabled", true);
    });

    $('body').click(function() {
        $('.popped-over').popover('hide');
        $('[data-toggle="tooltip"]').tooltip('hide');
    });

    //enabling tooltips for glyphiconed buttons
    $('[data-toggle="tooltip"]').tooltip();

    //hide inner modals and their backdrops after hiding the outer modal
    $('.modal').on('hidden.bs.modal', function (e) {
        if ($('.modal:visible').length == 0) {
            hideModals();
        }
    });

    //hide tooltip when modal is shown
    $('.modal').on('shown.bs.modal', function (e) {
        $('[data-toggle="tooltip"]').tooltip('hide');
    });
});

function hideModals() {
    $('.modal-backdrop').remove();
    $('.modal').modal('hide');
}
