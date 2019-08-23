import {pageBuilder} from './page/page-builder.js';
import {i18n} from './i18n/profile-translations.js';
import {formValidator} from './validation/form-validator.js';

export const profile = {
  init: function() {
    const location = window.location.pathname;

    if (location === "/profile" || location === "/profile/") {
      profile.loadProfile();
    }
  },
  loadProfile: function(profileId) {
    ajaxHandler.blockUI();
    let url;
    if (profileId) {
      url = '/api/profile/profile/' + profileId;
    } else {
      url = '/api/profile/profile';
    }
    const actions = {
      200: profile.parseProfileResponse,
      404: profile.renderCreateUserProfile
    };
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);
  },
  renderCreateUserProfile: function(response) {
    const userInfo = JSON.parse(response.headers.get("User-Info"));
    const lang = 'en';
    const messageSource = i18n[lang].createProfile;
    const properties = i18n[lang].properties;
    ajaxHandler.hideModals();
    pageBuilder.initPage('template#create-user-profile', '/profile', messageSource.title);
    document.querySelector('h1.create-user-profile-heading').innerHTML = messageSource.heading;
    const form = document.querySelector('form.create-user-profile-form');
    form.querySelector('label[for=firstName]').innerHTML = properties.firstName + ' *';
    form.querySelector('label[for=lastName]').innerHTML = properties.lastName + ' *';
    form.querySelector('label[for=nickName]').innerHTML = properties.nickName;
    const submitButton = form.querySelector('button.submit-button').innerHTML = messageSource.submitButton;

    form.onsubmit = function() {
      ajaxHandler.blockUI();
      const formData = new FormData(this);

      const actions = {
        200: profile.parseProfileResponse,
        400: function(response, form) {
          formValidator.handleBadRequest(response, form);
        }
      };

      const object = {
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        nickName: formData.get('nickName'),
        type: 'USER'
      };

      const payload = {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
      };
      //TODO: apply both server and client validation
      ajaxHandler.fetch(this, this.action, payload, actions);
      return false;
    };
  }
};
