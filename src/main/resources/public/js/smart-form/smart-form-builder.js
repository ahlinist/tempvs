import {formValidator} from '../validation/form-validator.js';

export const smartFormBuilder = {
  build: function(container, selector, label, value, formAction, isEditable) {
    const form = container.querySelector(selector + ' form');
    container.querySelector(selector + '-label').innerHTML = label;
    container.querySelector(selector + ' .text-holder').innerHTML = value;

    if (isEditable) {
      container.querySelector(selector + ' input').value = value;
      form.classList.add('smart-form');
      form.action = formAction;
      container.querySelector(selector + ' .smart-form-activator').classList.remove('hidden');

      container.querySelector(selector + ' .text-holder').onclick = function() {
        activateSmartForm(this, form);
      };
    }

    function hideSpinner() {
      const elementsToHide = document.querySelectorAll('.hide-me');
      elementsToHide.forEach(function(element) {
        element.classList.add('hidden');
        element.classList.remove('hide-me');
      });
    }

    function activateSmartForm(editButton, form) {
      const textWrapper = form.querySelector('.text-wrapper');
      const textHolder = textWrapper.querySelector('.text-holder');
      const inputWrapper = form.querySelector('.input-wrapper');
      const input = inputWrapper.querySelector('input');
      const originalValue = textHolder.innerHTML;

      textWrapper.classList.add('hidden');
      inputWrapper.classList.remove('hidden');

      const clickOutEventListener = function(event) {
        if((event.target !== input) && (event.target !== editButton)) {
          submitSmartForm();
        }
      }

      const keyPressEventListener = function(event) {
        if (event.key === 'Enter') {
          event.preventDefault();
          submitSmartForm();
        }

        if (event.key === 'Escape') {
          cancelEdit();
        }
      }

      function cancelEdit() {
        inputWrapper.classList.add('hidden');
        textWrapper.classList.remove('hidden');
        textHolder.innerHTML = originalValue;
        input.value = originalValue;
        window.removeEventListener("click", clickOutEventListener);
        window.removeEventListener("keydown", keyPressEventListener);
      }

      function submitSmartForm() {
        if(!!(inputWrapper.offsetWidth || inputWrapper.offsetHeight || inputWrapper.getClientRects().length)) {
          const spinner = form.querySelector('.spinner');
          const value = input.value;
          textHolder.innerHTML = value;
          inputWrapper.classList.add('hidden');
          textWrapper.classList.remove('hidden');
          window.removeEventListener("click", clickOutEventListener);
          window.removeEventListener("keydown", keyPressEventListener);

          let object = {};

          new FormData(form).forEach(function(value, key){
            object[key] = value;
          });

          const payload = {
            method: 'PATCH',
            headers:{
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(object)
          }

          spinner.classList.remove('hidden');
          spinner.classList.add('hide-me');

          const actions = {
            200: function() {
              hideSpinner();
            },
            400: function(response, form) {
              hideSpinner();
              cancelEdit();
              formValidator.handleBadRequest(response, form);
            }
          }

          ajaxHandler.fetch(form, form.action, payload, actions);
        }
      }

      window.addEventListener("click", clickOutEventListener);
      window.addEventListener("keydown", keyPressEventListener);
    }
  }
};
