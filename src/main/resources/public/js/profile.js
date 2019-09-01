import {pageBuilder} from './page/page-builder.js';
import {i18n} from './i18n/profile-translations.js';
import {formValidator} from './validation/form-validator.js';

export const profile = {
  init: function() {
    const location = window.location.pathname;

    if (location === "/profile" || location === "/profile/") {
      profile.loadProfile();
    } else if (location.includes("/profile/")) {
      const n = location.lastIndexOf('/');
      const profileId = location.substring(n + 1);
      profile.loadProfile(profileId);
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
      const firstNameBlank = formValidator.validateBlank(
          form.querySelector('[name=firstName]'),
          messageSource.firstNameBlank
      );

      const lastNameBlank = formValidator.validateBlank(
          form.querySelector('[name=lastName]'),
          messageSource.lastNameBlank
      );

      if (firstNameBlank || lastNameBlank) {
        return false;
      }

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
        nickName: formData.get('nickName') | null,
        type: 'USER'
      };

      const payload = {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(object)
      };
      ajaxHandler.fetch(this, this.action, payload, actions);
      return false;
    };
  },
  parseProfileResponse: function(response) {
    const userInfo = JSON.parse(response.headers.get("User-Info"));

    response.json().then(function(data) {
      profile.renderProfile(data, userInfo);
    });
  },
  renderProfile: function(profile, userInfo) {
    const lang = 'en';
    const properties = i18n[lang].properties;
    const profileId = profile.id;
    const firstName = profile.firstName;
    const lastName = profile.lastName;
    const nickName = profile.nickName;
    const period = profile.period;
    ajaxHandler.hideModals();
    pageBuilder.initPage('template#profile', '/profile/' + profileId, firstName + ' ' + lastName);

    const firstNameLabel = properties.firstName;
    const lastNameLabel = properties.lastName;
    const nickNameLabel = properties.nickName;
    const periodLabel = properties.period;
    const updateFirstNameAction = '/api/profile/' + profileId + '/firstName';
    const updateLastNameAction = '/api/profile/' + profileId + '/lastName';
    const updateNickNameAction = '/api/profile/' + profileId + '/nickName';
    const profileForm = document.querySelector(".profile-form");

    const isEditable = profile.id == userInfo.profileId;
    pageBuilder.smartForm(profileForm, firstNameLabel, firstName, 'firstName', updateFirstNameAction, isEditable, true);
    pageBuilder.smartForm(profileForm, lastNameLabel, lastName, 'lastName', updateLastNameAction, isEditable);
    pageBuilder.smartForm(profileForm, nickNameLabel, nickName, 'nickName', updateNickNameAction, isEditable);

    if (profile.type === 'CLUB') {
      pageBuilder.smartForm(profileForm, periodLabel, period, 'period', null, isEditable);
    }
  }
};
