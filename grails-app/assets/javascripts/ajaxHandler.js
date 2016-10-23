function sendAjaxRequest(form) {
    var spinner = $(form).find('.ajaxSpinner');
    var submitButton = $(form).find('.submit-button')

    $.ajax({
        url: $(form).attr('action'),
        type: 'post',
        data: new FormData(form),
        dataType: 'json',
        processData: false,
        contentType: false,
        beforeSend: function() {
            submitButton.attr("disabled", true);
            spinner.fadeIn();
            $(form).find('.alert').remove();
        },
        complete: function() {
            submitButton.removeAttr("disabled");
            spinner.fadeOut();
        },
        success: function(response) {
            if (response.redirect) {
                window.location.href = response.redirect;
            } else {
                $.each(response.messages, function(index, message) {
                    renderAjaxResponseMessage(form, response.success ? 'success' :'danger', message);
                });
            }
        },
        error: function() {
            renderAjaxResponseMessage(form, 'danger', 'Something went wrong :(');
        }
    });
};

function renderAjaxResponseMessage(form, alertType, message) {
    $(form).append('<div class="alert alert-' + alertType + ' text-center">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + message + '</div>');
}
