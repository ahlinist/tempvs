import {pageBuilder} from './page/page-builder.js';
import {langResolver} from './i18n/language-resolver.js';
import {i18n} from './i18n/user-translations.js';
import {profile} from './profile.js';
import {formValidator} from './validation/form-validator.js';

export const user = {
  init: function() {
    const location = window.location.pathname;

    if (location.startsWith("/user/registration/")) {
      const idPosition = location.lastIndexOf('/');
      const verificationId = location.substring(idPosition + 1);
      user.create(verificationId);
    }
  },
  create: function(verificationId) {
    const lang = langResolver.resolve();
    const messageSource = i18n[lang] || i18n['en'];
    pageBuilder.initPage('template#create-user', '/user/registration/' + verificationId, messageSource.create.title);
    const registerForm = document.querySelector('content form.register-user');
    registerForm.querySelector('label[for=password]').innerHTML = messageSource.create.password + ' *';
    registerForm.querySelector('button.submit-button').innerHTML = messageSource.create.submitButton;
    registerForm.action = '/api/user/verify/' + verificationId;
    registerForm.onsubmit = function() {
      const form = this;
      const formData = new FormData(form);
      const object = {
        password: formData.get('password'),
      };
      const payload = {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
      };
      const actions = {
        200: function(response) {
          window.history.pushState("", "", '/profile');
          profile.loadProfile();
        },
        400: function(response, form) {
          formValidator.handleBadRequest(response, form);
        }
      };
      ajaxHandler.fetch(form, form.action, payload, actions);
      return false;
    }
  }
};
