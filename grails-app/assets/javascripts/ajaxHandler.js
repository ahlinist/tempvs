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

    $.ajax({
        url: url,
        type: 'post',
        data: data,
        dataType: 'json',
        processData: false,
        contentType: false,
        beforeSend: function() {
            clearForm(element);
            submitButton.attr("disabled", true);
            spinner.fadeIn();
        },
        complete: function() {
            submitButton.removeAttr("disabled");
            spinner.fadeOut();
        },
        success: function(response) {
            if (response.redirect) {
                window.location.href = response.redirect;
            } else if (response.formMessage) {
                createFormMessage(element, response.success, response.message);
            } else {
                $.each(response, function(index, fieldEntry) {
                    createPopover(element, fieldEntry);
                });

                $('.popped-over').popover('show');
            }
        },
        error: function() {
            createFormMessage(element, false, "Something went wrong :(");
        }
    });
};

function createPopover(element, fieldEntry) {
    var field = $(element).find('[name="' + fieldEntry.name + '"]')
    field.addClass('popped-over').attr('data-placement','right').attr('data-content', fieldEntry.message).attr('data-html', true);
    field.addClass('bg-danger');
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
