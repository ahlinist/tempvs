import {langResolver} from './i18n/language-resolver.js';
import {i18n} from './i18n/header-translations.js';
import {formValidator} from './validation/form-validator.js';

export const header = {
  init: function() {
    const lang = langResolver.resolve();
    const messageSource = i18n[lang] || i18n['en'];
    const isAuthenticated = isUserAuthenticated();
    const header = document.querySelector('header');

    //library
    const library = header.querySelector('.library');
    library.setAttribute('title', messageSource.library.tooltip);

    //login
    const login = header.querySelector('.login');
    if (!isAuthenticated) {
      login.classList.remove('hidden');
      login.setAttribute('title', messageSource.login.tooltip);
      login.querySelector('[href=\\#login-tab]').innerHTML = messageSource.login.loginTab;
      login.querySelector('[href=\\#register-tab]').innerHTML = messageSource.login.registerTab;
      const registerForm = login.querySelector('form.registration');
      registerForm.querySelector('label[for=email]').innerHTML = messageSource.login.email + ' *';
      registerForm.querySelector('button.submit-button').innerHTML = messageSource.login.registerButton;
      registerForm.action = '/api/user/register';
      registerForm.onsubmit = function() {
        const form = this;
        const emailInvalidMessage = messageSource.login.emailInvalidMessage;
        const isEmailValid = formValidator.validateEmail(
            form.querySelector('input[name=email]'),
            emailInvalidMessage
        );
        if (!isEmailValid) {
          return false;
        }
        const formData = new FormData(form);
        const object = {
          email: formData.get('email'),
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
            ajaxHandler.hideModals();
            const lang = langResolver.resolve(response);
            const messageSource = i18n[lang] || i18n['en'];
            const content = document.querySelector('content');
            content.innerHTML = messageSource.login.verificationSentMessage;
          },
          400: function(response, form) {
            formValidator.handleBadRequest(response, form);
          }
        };
        ajaxHandler.blockUI();
        ajaxHandler.fetch(form, form.action, payload, actions);
        return false;
      }
    }

    function isUserAuthenticated() {
      const authCookieName = 'TEMPVS_AUTH';
      const cookieMatcher = document.cookie.match('(^|;) ?' + authCookieName + '=([^;]*)(;|$)');
      return cookieMatcher && cookieMatcher[2]
    }
  }
};
