function sendAjaxRequest(form) {
    var spinner = $(form).find('.ajaxSpinner');
    var submitButton = $(form).find('.submit-button')

    $.ajax({
        url: form.action,
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
                    renderResponseMessage(form, response.success ? 'success' :'danger', message);
                });
            }
        },
        error: function() {
            renderResponseMessage(form, 'danger', 'Something went wrong :(');
        }
    });
};

function renderResponseMessage(form, alertType, message) {
    var alertBox = createAlertBox(alertType, message);
    form.append(alertBox);
    $(alertBox).hide().fadeIn();

    if (alertType == 'success') {
        $(alertBox).delay(2000).fadeOut();
    }

    $("a.close").click(function (e) {
        $(this).parent().fadeOut();
    });
}

function createAlertBox(alertType, message) {
    var alertBox = document.createElement('div');
    alertBox.display = 'none';
    alertBox.className = 'alert alert-' + alertType + ' text-center';
    alertBox.append(createCloseLink());
    alertBox.innerHTML += message;
    return alertBox;
}

function createCloseLink() {
    var closeLink = document.createElement('a');
    closeLink.href = '#';
    closeLink.className = 'close';
    closeLink.innerHTML = '&times;';
    return closeLink;
}
