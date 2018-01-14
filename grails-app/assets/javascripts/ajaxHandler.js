function getActions() {
    function redirectAction(element, response){
        window.location.href = response.location;
    }

    function replaceElementAction(element, response, selector) {
        var backdrop = document.querySelector('.modal-backdrop');

        if (backdrop) {
            backdrop.parentNode.removeChild(backdrop);
        }

        $(element).modal('hide');
        document.querySelector('body').classList.remove('modal-open');
        var container = document.querySelector(selector);
        container.innerHTML = response.template;
    }

    function formMessageAction(element, response) {
        var messageContainer = document.createElement('span');
        messageContainer.classList.add('text-center');
        messageContainer.classList.add('form-message');
        messageContainer.classList.add(response.success ? 'text-success' : 'text-danger');
        messageContainer.innerHTML += response.message;
        element.querySelector('.submit-button').parentNode.appendChild(messageContainer);
    }

    function validationResponseAction(element, response) {
        for (entry in response.messages) {
            createPopover(element, response.messages[entry]);
        }

        $('.popped-over').popover('show');
    }

    var actions = {
               redirect: redirectAction,
               replaceElement: replaceElementAction,
               formMessage: formMessageAction,
               validationResponse: validationResponseAction,
               none: function() {},
    };

    return actions;
}

function submitAjaxForm(form, selector, actions) {
    var method = 'POST'
    var url = form.action;
    var data = new FormData(form);
    processAjaxRequest(form, url, data, method, selector, actions);
}

function sendAjaxRequest(element, url, method, selector, actions) {
    processAjaxRequest(element, url, '', method, selector, actions);
}

function processAjaxRequest(element, url, data, method, selector, actions) {
    var spinner = element.querySelector('.ajaxSpinner');
    var submitButton = element.querySelector('.submit-button');

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
            success(element, response, actions, selector);
        },
        error: function() {
            complete(submitButton, spinner);
            actions.formMessage(element, {success: false, message: "Something went wrong :("});
        }
    });
};

function beforeSend(element, submitButton, spinner) {
    clearForm(element);
    
    if (submitButton) {
        submitButton.setAttribute("disabled", true);
    } 

    if (spinner) {
        spinner.style.display = 'inline';
    }
}

function complete(submitButton, spinner) {
    if (spinner) {
        spinner.style.display = 'none';
    }

    if (submitButton) {
        submitButton.removeAttribute("disabled");
    }
}

function success(element, response, actions, selector) {
    actions[response.action](element, response, selector);
}

function createPopover(element, fieldEntry) {
    var field = element.querySelector('[name="' + fieldEntry.name + '"]')

    if (!field) {
        field = element.querySelector('.submit-button');
    }

    field.classList.add('popped-over');
    field.classList.add('bg-danger');
    field.setAttribute('data-placement','right');
    field.setAttribute('data-content', fieldEntry.message);
    field.setAttribute('data-html', true);
}

function clearForm(element) {
    $(element).find('.popped-over').removeClass('popped-over').removeAttr('data-placement').removeAttr('data-content').popover('hide');
    $(element).find('.form-message').remove();
    $(element).find('.bg-danger').removeClass('bg-danger');
}
