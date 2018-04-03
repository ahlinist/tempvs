var ajaxHandler = {
    actions: {
        redirect: function(element, response){
            hideModals();
            blockUI();
            window.location.href = response.location;
        },
        replaceElement: function(element, response, selector) {
            var backdrop = document.querySelector('.modal-backdrop');
            var container = document.querySelector(selector);

            if (backdrop) {
                backdrop.parentNode.removeChild(backdrop);
            }

            $(element).modal('hide');
            document.querySelector('body').classList.remove('modal-open');
            container.innerHTML = response.template;
        },
        formMessage: function(element, response) {
            var messageContainer = document.createElement('span');
            messageContainer.classList.add('text-center');
            messageContainer.classList.add('form-message');
            messageContainer.classList.add(response.success ? 'text-success' : 'text-danger');
            messageContainer.innerHTML += response.message;
            element.querySelector('.submit-button').parentNode.appendChild(messageContainer);
        },
        validationResponse: function(element, response) {
            var fieldEntry;
            var field;

            for (entry in response.messages) {
                fieldEntry = response.messages[entry];
                field = element.querySelector('[name="' + fieldEntry.name + '"]')

                if (!field) {
                    field = element.querySelector('.submit-button');
                }

                field.classList.add('popped-over');
                field.setAttribute('data-placement','right');
                field.setAttribute('data-container','body');
                field.setAttribute('data-content', fieldEntry.message);
                field.setAttribute('data-html', true);
            }

            $('.popped-over').popover('show');
        },
        none: function() {},
    },
    processAjaxRequest: function(element, url, data, method, selector, actions) {
        var submitButton = element.querySelector('.submit-button');

        $.ajax({
            url: url,
            method: method,
            data: data,
            dataType: 'json',
            processData: false,
            contentType: false,
            beforeSend: function() {
                beforeSend(element, submitButton);
            },
            success: function(response) {
                complete(submitButton);
                actions[response.action](element, response, selector);
            },
            error: function() {
                complete(submitButton);
                actions.formMessageAction(element, {success: false, message: "Something went wrong :("});
            }
        });

        function beforeSend(element, submitButton) {
            var popover = element.querySelector('.popped-over');
            var formMessage = element.querySelector('.form-message');
            var bgDanger = element.querySelector('.bg-danger');

            if (popover) {
                popover.classList.remove('popped-over');
                popover.removeAttribute('data-placement');
                popover.removeAttribute('data-content');
                $(popover).popover('hide');
            }

            if (formMessage) {
                formMessage.remove();
            }

            if (bgDanger) {
                bgDanger.classList.remove('bg-danger');
            }

            blockUI();

            if (submitButton) {
                submitButton.setAttribute("disabled", true);
            }
        }

        function complete(submitButton) {
            unblockUI();

            if (submitButton) {
                submitButton.removeAttribute("disabled");
            }
        }
    }
};
