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
    if (overlay.parentNode == document.body) {
        document.body.removeChild(overlay);
    }
}

$(function (){
    //preventing double clicking
    $('.disableable').click(function() {
        this.setAttribute('disabled', true);
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

    //display new followers counter
    followersCounter.displayCounter();

    //populate profile dropdown
    profileDropdown.displayDropdown();
});

function hideModals() {
    $('.modal-backdrop').remove();
    $('.modal').modal('hide');
}

function setFileUploadPlaceholder(element) {
    //cutting off "fakepath"
    var fileName = element.value.split(/(\\|\/)/g).pop();
    var placeholder = element.parentNode.querySelector(".placeholder");
    placeholder.innerHTML = "<b>" + fileName + "</b>";
}
