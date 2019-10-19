import {pageBuilder} from './page/page-builder.js';

export const header = {
  init: function() {
    pageBuilder.initHeader('template#' + getHeaderType());

    function getHeaderType() {
      const authCookieName = 'TEMPVS_AUTH';
      const cookieMatcher = document.cookie.match('(^|;) ?' + authCookieName + '=([^;]*)(;|$)');
      return cookieMatcher ? 'header' : 'header-anonymous'
    }
  }
};
