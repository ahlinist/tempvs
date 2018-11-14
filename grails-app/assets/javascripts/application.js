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
        this.setAttribute('disabled', true);
    });

    $('body').click(function() {
        $('.popped-over').popover('destroy');
        $('[data-toggle="tooltip"]').tooltip('destroy');
    });

    //enabling tooltips for glyphiconed buttons
    $('[data-toggle="tooltip"]').tooltip();

    //hide inner modals and their backdrops after hiding the outer modal
    $('.modal').on('hidden.bs.modal', function (e) {
        if ($('.modal:visible').length == 0) {
            ajaxHandler.hideModals();
        }
    });

    //hide tooltip when modal is shown
    $('.modal').on('shown.bs.modal', function (e) {
        $('[data-toggle="tooltip"]').tooltip('hide');
    });

    //populate profile dropdown
    profileDropdown.displayDropdown();

    //display horizontal menu counters
    messaging.displayNewMessagesCounter();
    horizontalMenuCounter.displayCounter('span#new-followings', '/following/getNewFollowersCount');
});

function setFileUploadPlaceholder(element) {
    //cutting off "fakepath"
    var fileName = element.value.split(/(\\|\/)/g).pop();
    var placeholder = element.parentNode.querySelector(".placeholder");
    placeholder.innerHTML = "<b>" + fileName + "</b>";
}
