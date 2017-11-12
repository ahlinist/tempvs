function submitAjaxForm(form, success) {
    var method = 'POST'
    var url = form.action;
    var data = new FormData(form);
    var successAction = (typeof success === "function") ? success : defaultSuccess;
    processAjaxRequest(form, url, data, method, successAction);
}

function sendAjaxRequest(element, url, method, success) {
    var successAction = (typeof success === "function") ? success : defaultSuccess;
    processAjaxRequest(element, url, '', method, successAction);
}

function processAjaxRequest(element, url, data, method, success) {
    var spinner = $(element).find('.ajaxSpinner');
    var submitButton = $(element).find('.submit-button');

    $.ajax({
        url: url,
        method: method,
        data: data,
        dataType: 'json',
        processData: false,
        contentType: false,
        beforeSend: function() {
            beforeSend(element, submitButton, spinner);
        },
        success: function(response) {
            complete(submitButton, spinner);
            success(element, response);
        },
        error: function() {
            complete(submitButton, spinner);
            createFormMessage(element, false, "Something went wrong :(");
        }
    });
};

function beforeSend(element, submitButton, spinner) {
    clearForm(element);
    submitButton.attr("disabled", true);
    spinner.fadeIn();
}

function complete(submitButton, spinner) {
    submitButton.removeAttr("disabled");
    spinner.fadeOut();
}

function defaultSuccess(element, response) {
    if (response.redirect) {
        window.location.href = response.redirect;
    } else if (response.formMessage) {
        createFormMessage(element, response.success, response.message);
    } else if (response.append) {
        var appendTo = document.querySelector(response.selector);
        appendTo.innerHTML = response.template + appendTo.innerHTML;
    } else if (response.delete) {
        $(element).modal('hide');
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
        var toDelete = document.querySelector(response.selector);
        toDelete.parentNode.removeChild(toDelete);
    } else {
        $.each(response, function(index, fieldEntry) {
            createPopover(element, fieldEntry);
        });

        $('.popped-over').popover('show');
    }
}

function createPopover(element, fieldEntry) {
    var field = $(element).find('[name="' + fieldEntry.name + '"]')
    field.addClass('popped-over').addClass('bg-danger').attr('data-placement','right').attr('data-content', fieldEntry.message).attr('data-html', true);
}

function clearForm(element) {
    $(element).find('.popped-over').removeClass('popped-over').removeAttr('data-placement').removeAttr('data-content').popover('hide');
    $(element).find('.form-message').remove();
    $(element).find('.bg-danger').removeClass('bg-danger');
}

function createFormMessage(element, success, message) {
    var messageContainer = document.createElement('span');
    $(messageContainer).addClass('text-center').addClass(success ? 'text-success' : 'text-danger').addClass('form-message');
    messageContainer.innerHTML += message;
    $(element).find('[name="submit-button"]').parent().append(messageContainer);
}
