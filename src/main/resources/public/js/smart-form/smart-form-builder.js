export const smartFormBuilder = {
  build: function(form, selector, value, formAction, executionActions) {
    form.querySelector(selector + ' .text-holder').innerHTML = value;
    form.querySelector(selector + ' input').value = value;
    form.querySelector(selector + ' form').action = formAction;
    form.querySelector(selector + ' .smart-form-activator').classList.remove('hidden');

    form.querySelector(selector + ' .smart-form-activator').onclick = function() {
      ajaxHandler.activateSmartForm(this, executionActions, 'PATCH');
    };
  }
};
