var ajaxHandler = {
  actions: {
    redirect: function(element, response){
        ajaxHandler.hideModals();
        ajaxHandler.blockUI();
        window.location.href = response.location;
    },
    replaceElement: function(element, response, selector) {
        ajaxHandler.hideModals();
        var container = document.querySelector(selector);
        container.innerHTML = response.template;
    },
    appendElement: function(element, response, selector) {
        ajaxHandler.hideModals();
        var container = document.querySelector(selector);
        container.innerHTML += response.template;
    },
    formMessage: function(element, response) {
        var submitButton = element.querySelector('.submit-button')
        var messageContainer = document.createElement('span');
        messageContainer.classList.add('text-center');
        messageContainer.classList.add('form-message');
        messageContainer.classList.add(response.success ? 'text-success' : 'text-danger');
        messageContainer.innerHTML += response.message;

        if (submitButton) {
            submitButton.parentNode.appendChild(messageContainer);
        }
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
            field.setAttribute('data-placement','bottom');
            field.setAttribute('data-container','body');
            field.setAttribute('data-content', fieldEntry.message);
            field.setAttribute('data-html', true);
        }

        $('.popped-over').popover('show');
    },
    none: function() {},
  },
  processAjaxRequest: function(element, url, data, method, selector, actions, isValid, isSpinnerHidden) {
    if (element) {
      var submitButton = element.querySelector('.submit-button');
      clearForm(element);

      if (isValid || validateForm(element)) {
        $.ajax({
          url: url,
          method: method,
          data: data,
          dataType: 'json',
          processData: false,
          contentType: false,
          beforeSend: function() {
            if (!isSpinnerHidden) {
                ajaxHandler.blockUI();
            }

            if (submitButton) {
                submitButton.setAttribute("disabled", true);
            }
          },
          success: function(response) {
            complete(submitButton);
            actions[response.action](element, response, selector);
          },
          error: function(jqXHR, status) {
            var message;

            if (jqXHR.responseJSON) {
                message = jqXHR.responseJSON.error
            } else {
                message = "Something went wrong :(";
            }

            complete(submitButton);
            actions.formMessage(element, {success: false, message: message});
          }
        });
      }

      function validateForm(form) {
        var isValid = true;
        var mandatoryFields = form.getElementsByClassName('mandatory');

        for (var i = 0; i < mandatoryFields.length; i++) {
            var field = mandatoryFields[i];

            if (!field.value) {
                field.classList.add('bg-danger');
                isValid = false;
            }
        }

        return isValid;
      }

      function clearForm(element) {
        var popovers = element.getElementsByClassName('popped-over');
        var formMessages = element.getElementsByClassName('form-message');
        var bgDangers = element.getElementsByClassName('bg-danger');

        for (i = popovers.length - 1; i >= 0 ; i--) {
            var popover = popovers[i];
            popover.classList.remove('popped-over');
            popover.removeAttribute('data-placement');
            popover.removeAttribute('data-content');
            $(popover).popover('hide');
        }

        for (j = formMessages.length - 1; j >= 0 ; j--) {
            formMessages[j].remove();
        }

        for (k = bgDangers.length - 1; k >= 0; k--) {
            bgDangers[k].classList.remove('bg-danger');
        }
      }

      function complete(submitButton) {
        ajaxHandler.unblockUI();

        if (submitButton) {
            submitButton.removeAttribute("disabled");
        }
      }
    }
  },
  fetch: function(form, url, payload, actions) {
    fetch(url, payload)
      .then(
        function(response) {
          ajaxHandler.unblockUI();
          actions[response.status](response, form);
        }
      )
      .catch(function(error) {
        ajaxHandler.unblockUI();

        console.log('Looks like there was a problem: ' + error);
      });
  },
  hideModals: function() {
    var backdrop = document.querySelector('.modal-backdrop');

    if (backdrop) {
        backdrop.parentNode.removeChild(backdrop);
    }

    $('.modal-backdrop').remove();
    $('.modal').modal('hide');
    document.querySelector('body').classList.remove('modal-open');
  },
  blockUI: function() {
    var overlay = document.createElement('div');
    overlay.id = 'overlay';
    document.body.appendChild(overlay);
  },
  unblockUI: function() {
    var overlay = document.querySelector('#overlay');

    if (overlay && overlay.parentNode == document.body) {
        document.body.removeChild(overlay);
    }
  }
};
