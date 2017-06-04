function submitAjaxForm(form) {
    var url = form.action;
    var data = new FormData(form);
    processAjaxRequest(form, url, data);
}

function sendAjaxRequest(element, url) {
    var data = new FormData();
    processAjaxRequest(element, url, data);
}

function processAjaxRequest(element, url, data) {
    var spinner = $(element).find('.ajaxSpinner');
    var submitButton = $(element).find('.submit-button');
    data.append("isAjaxRequest", true);

    $.ajax({
        url: url,
        type: 'post',
        data: data,
        dataType: 'json',
        processData: false,
        contentType: false,
        beforeSend: function() {
            submitButton.attr("disabled", true);
            spinner.fadeIn();
            $(element).find('.alert').remove();
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
                    renderResponseMessage(element, response.success ? 'success' :'danger', message);
                });
            }
        },
        error: function() {
            renderResponseMessage(element, 'danger', 'Something went wrong :(');
        }
    });
};

function renderResponseMessage(element, alertType, message) {
    var alertBox = createAlertBox(alertType, message);
    element.append(alertBox);
    $(alertBox).hide().fadeIn();

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
