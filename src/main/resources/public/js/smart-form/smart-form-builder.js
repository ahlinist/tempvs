export const smartFormBuilder = {
  build: function(container, selector, label, value, formAction) {
    container.querySelector(selector + '-label').innerHTML = label;
    container.querySelector(selector + ' .text-holder').innerHTML = value;
    container.querySelector(selector + ' input').value = value;
    container.querySelector(selector + ' form').action = formAction;
    container.querySelector(selector + ' .smart-form-activator').classList.remove('hidden');

    container.querySelector(selector + ' .smart-form-activator').onclick = function() {
      ajaxHandler.activateSmartForm(this, smartFormBuilder.hideSpinners, 'PATCH');
    };
  },
  hideSpinners: {
    200: function() {
      var elementsToHide = document.querySelectorAll('.hide-me');
      elementsToHide.forEach(function(element) {
        element.classList.add('hidden');
        element.classList.remove('hide-me');
      });
    }
  }
};
