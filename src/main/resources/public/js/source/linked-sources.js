import {i18n} from '../i18n/linked-sources-translations.js';
import {sourceSearch} from './source-search.js';

export const linkedSources = {
  build: function(linkedSourcesContainer, item, isEditable) {
    let lang = 'en';

    if (!item.sources || !item.sources.length) {
      const messageContainer = linkedSourcesContainer.querySelector('p.message-container');
      messageContainer.innerHTML = i18n[lang].noSources;
    }

    const findSourcesContainer = linkedSourcesContainer.querySelector('div.find-sources-container');
    findSourcesContainer.querySelector('span.find-sources-popup-text').innerHTML = i18n[lang].findSourcesButton;
    findSourcesContainer.querySelector('div.find-source-type > h4').innerHTML = i18n[lang].sourceTypeHeading;
    findSourcesContainer.querySelector('input[name=query]').placeholder = i18n[lang].findSourcesPlaceholder;
    const searchForm = findSourcesContainer.querySelector('form.search-form');
    searchForm.querySelector('label.type-written').innerHTML = i18n[lang].typeWritten;
    searchForm.querySelector('label.type-graphic').innerHTML = i18n[lang].typeGraphic;
    searchForm.querySelector('label.type-archaeological').innerHTML = i18n[lang].typeArchaeological;
    searchForm.querySelector('label.type-other').innerHTML = i18n[lang].typeOther;
    searchForm.action = '/api/library/source';
    searchForm.onsubmit = function() {
      const actions = {
        200: function() {alert(123);}
      };

      const url = sourceSearch.buildUrl(this);
      ajaxHandler.fetch(this, url, {method: 'GET'}, actions);
      return false;
    }

    searchForm.querySelector('input[name=period]').value = item.period;
    searchForm.querySelector('input[name=classification]').value = item.classification;
  }
};
