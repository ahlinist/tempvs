import {langResolver} from './i18n/language-resolver.js';
import {i18n} from './i18n/header-translations.js';
import {formValidator} from './validation/form-validator.js';
import {user} from './user.js';
import {profile} from './profile.js';

export const header = {
  init: function() {
    const lang = langResolver.resolve();
    const messageSource = i18n[lang] || i18n['en'];
    const isAuthenticated = isUserAuthenticated();
    const header = document.querySelector('header');

    //library
    const library = header.querySelector('.library');
    library.setAttribute('title', messageSource.library.tooltip);

    //login/logout
    const login = header.querySelector('.login');
    if (isAuthenticated) {
      login.classList.add('hidden');
      const logout = header.querySelector('.logout');
      logout.classList.remove('hidden');
    } else {
      login.classList.remove('hidden');
      login.setAttribute('title', messageSource.login.tooltip);
      login.querySelector('[href=\\#login-tab]').innerHTML = messageSource.login.loginTab;
      login.querySelector('[href=\\#register-tab]').innerHTML = messageSource.login.registerTab;

      //registration
      const registerForm = login.querySelector('form.registration');
      registerForm.querySelector('label[for=email]').innerHTML = messageSource.login.email + ' *';
      registerForm.querySelector('button.register').innerHTML = messageSource.login.registerButton;
      registerForm.action = '/api/user/register';
      registerForm.onsubmit = function() {
        const form = this;
        const isEmailBlank = formValidator.validateBlank(
            form.querySelector('input[name=email]'),
            messageSource.login.emailBlankMessage
        );
        if (isEmailBlank) {
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

      //login
      const loginForm = login.querySelector('form.login');
      loginForm.querySelector('label[for=email]').innerHTML = messageSource.login.email + ' *';
      loginForm.querySelector('label[for=password]').innerHTML = messageSource.login.password + ' *';
      loginForm.querySelector('button.login').innerHTML = messageSource.login.loginButton;
      loginForm.action = '/api/user/login';
      loginForm.onsubmit = function() {
        const form = this;
        const isEmailBlank = formValidator.validateBlank(
            form.querySelector('input[name=email]'),
            messageSource.login.emailBlankMessage
        );
        const isPasswordBlank = formValidator.validateBlank(
            form.querySelector('input[name=password]'),
            messageSource.login.passwordBlankMessage
        );
        if (isEmailBlank || isPasswordBlank) {
          return false;
        }
        const formData = new FormData(form);
        const object = {
          email: formData.get('email'),
          password: formData.get('password')
        };
        const payload = {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(object)
        };
        const actions = {
          200: user.login,
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
      const authCookieName = 'TEMPVS_LOGGED_IN';
      const cookieMatcher = document.cookie.match('(^|;) ?' + authCookieName + '=([^;]*)(;|$)');
      return cookieMatcher && cookieMatcher[2]
    }
  }
};
