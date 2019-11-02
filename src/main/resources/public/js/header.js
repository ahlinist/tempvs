import {langResolver} from './i18n/language-resolver.js';
import {i18n} from './i18n/header-translations.js';
import {formValidator} from './validation/form-validator.js';
import {user} from './user.js';
import {profile} from './profile.js';
import {profileSearcher} from './profile/profile-searcher.js';

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
    const logout = header.querySelector('.logout');
    const logoutButton = logout.querySelector('span.fa-sign-out');
    logoutButton.onclick = user.logout;
    if (isAuthenticated) {
      login.classList.add('hidden');
      logout.classList.remove('hidden');
    } else {
      logout.classList.add('hidden');
      login.classList.remove('hidden');
      login.setAttribute('title', messageSource.login.tooltip);
      login.querySelector('[href=\\#login-tab]').innerHTML = messageSource.login.loginTab;
      login.querySelector('[href=\\#register-tab]').innerHTML = messageSource.login.registerTab;

      //registration
      const registerForm = login.querySelector('form.registration');
      registerForm.querySelector('label[for=email]').innerHTML = messageSource.login.email + ' *';
      registerForm.querySelector('button.register').innerHTML = messageSource.login.registerButton;
      registerForm.action = '/api/user/register';
      registerForm.onsubmit = user.register;

      //login
      const loginForm = login.querySelector('form.login');
      loginForm.querySelector('label[for=email]').innerHTML = messageSource.login.email + ' *';
      loginForm.querySelector('label[for=password]').innerHTML = messageSource.login.password + ' *';
      loginForm.querySelector('button.login').innerHTML = messageSource.login.loginButton;
      loginForm.action = '/api/user/login';
      loginForm.onsubmit = user.login;
    }

    //profile search
    if (isAuthenticated) {
      const profileSearch = header.querySelector('span.profile-search');
      profileSearch.classList.remove('hidden');
      profileSearch.querySelector('input.profile-search-box').setAttribute('placeholder', messageSource.profileSearch.placeholder);
      const profileSearchButton = profileSearch.querySelector('button.profile-search-button');
      profileSearchButton.onclick = function() {
        profileSearcher.search(this, 0);
      }
      const loadMoreButton = profileSearch.querySelector('button.load-more-button');
      loadMoreButton.innerHTML = messageSource.profileSearch.loadMore.italics();
    }

    function isUserAuthenticated() {
      const authCookieName = 'TEMPVS_LOGGED_IN';
      const cookieMatcher = document.cookie.match('(^|;) ?' + authCookieName + '=([^;]*)(;|$)');
      return cookieMatcher && cookieMatcher[2] === 'true';
    }
  }
};
