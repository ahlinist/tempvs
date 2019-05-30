export const smartFormBuilder = {
  build: function(container, selector, value, formAction, executionActions) {
    container.querySelector(selector + ' .text-holder').innerHTML = value;
    container.querySelector(selector + ' input').value = value;
    container.querySelector(selector + ' form').action = formAction;
    container.querySelector(selector + ' .smart-form-activator').classList.remove('hidden');

    container.querySelector(selector + ' .smart-form-activator').onclick = function() {
      ajaxHandler.activateSmartForm(this, executionActions, 'PATCH');
    };
  }
};
