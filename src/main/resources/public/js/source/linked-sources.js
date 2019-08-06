import {i18n} from '../i18n/linked-sources-translations.js';
import {i18n as typeI18n} from '../i18n/type-translations.js';
import {sourceSearch} from './source-search.js';
import {library} from '../library.js';

export const linkedSources = {
  build: function(linkedSourcesContainer, item, isEditable) {
    let lang = 'en';
    linkedSourcesContainer.querySelector('h2.linked-sources-heading').innerHTML = i18n[lang].linkedSourcesHeading;

    if (!item.sources || !item.sources.length) {
      const messageContainer = linkedSourcesContainer.querySelector('p.message-container');
      messageContainer.innerHTML = i18n[lang].noSources;
      messageContainer.classList.remove('hidden');
    } else {
      const object = {ids: item.sources}
      const q = window.btoa(encodeURIComponent(JSON.stringify(object)));
      const url = '/api/library/source?q=' + q;
      const actions = {200: renderLinkedSources}

      ajaxHandler.fetch(this, url, {method: 'GET'}, actions);

      function renderLinkedSources(response) {
        const listItem = document.querySelector('template.linked-source-item');
        const li = listItem.content.querySelector('li');
        const ul = linkedSourcesContainer.querySelector('ul.linked-sources-list');
        ul.innerHTML = '';

        response.json().then(function(data) {
          const userInfo = JSON.parse(response.headers.get("User-Info"));

          for (let source of data) {
            const entry = document.importNode(li, true);
            const a = entry.querySelector('a');
            a.href = '/api/library/source/' + source.id;
            a.innerHTML = source.name;
            a.onclick = function() {
              library.renderSourcePage(source, userInfo);
            }
            ul.appendChild(entry);
          }
        });
      }
    }

    if (!isEditable) {
      return;
    }

    const findSourcesContainer = linkedSourcesContainer.querySelector('div.find-sources-container');
    findSourcesContainer.querySelector('span.find-sources-popup-text').innerHTML = i18n[lang].findSourcesButton;
    findSourcesContainer.querySelector('div.find-source-type > h4').innerHTML = i18n[lang].sourceTypeHeading;
    findSourcesContainer.querySelector('input[name=query]').placeholder = i18n[lang].findSourcesPlaceholder;
    const searchForm = findSourcesContainer.querySelector('form.search-form');
    searchForm.querySelector('label.type-written').innerHTML = typeI18n[lang].types['WRITTEN'];
    searchForm.querySelector('label.type-graphic').innerHTML = typeI18n[lang].types['GRAPHIC'];
    searchForm.querySelector('label.type-archaeological').innerHTML = typeI18n[lang].types['ARCHAEOLOGICAL'];
    searchForm.querySelector('label.type-other').innerHTML = typeI18n[lang].types['OTHER'];
    searchForm.action = '/api/library/source/find';

    const resultTable = findSourcesContainer.querySelector('table.result-table');
    resultTable.querySelector('th.source-name').innerHTML = i18n[lang].sourceNameLabel;
    resultTable.querySelector('th.source-description').innerHTML = i18n[lang].sourceDescriptionLabel;
    resultTable.querySelector('th.source-type').innerHTML = i18n[lang].sourceTypeLabel;
    const tbody = resultTable.querySelector('tbody');
    const noResultsContainer = findSourcesContainer.querySelector('p.no-results-message');
    const spinner = findSourcesContainer.querySelector('img.spinner');

    searchForm.onsubmit = function() {
      spinner.classList.remove('hidden');
      resultTable.classList.add('hidden');
      noResultsContainer.classList.add('hidden');
      tbody.innerHTML = '';

      const actions = {
        200: renderSearchResult
      };

      const url = sourceSearch.buildUrl(this);
      ajaxHandler.fetch(this, url, {method: 'GET'}, actions);
      return false;
    }

    searchForm.querySelector('input[name=period]').value = item.period;
    searchForm.querySelector('input[name=classification]').value = item.classification;

    function renderSearchResult(response) {
      spinner.classList.add('hidden');

      response.json().then(function(data) {
        if (!data || data.length === 0) {
          noResultsContainer.innerHTML = i18n[lang].noResultsMessage;
          noResultsContainer.classList.remove('hidden');
          return;
        }

        const userInfo = JSON.parse(response.headers.get("User-Info"));

        const resultTemplate = document.querySelector('template.source-template');
        const tr = resultTemplate.content.querySelector('tr');

        for (const row of data) {
          const entry = document.importNode(tr, true);
          const sourceName = entry.querySelector('td.source-name');
          const sourceDescription = entry.querySelector('td.source-description');
          const sourceType = entry.querySelector('td.source-type');
          sourceName.innerHTML = row.name;
          sourceDescription.innerHTML = row.description;
          sourceType.innerHTML = typeI18n[lang].types[row.type];
          sourceName.onclick = function() {library.renderSourcePage(row, userInfo);};
          sourceDescription.onclick = function() {library.renderSourcePage(row, userInfo);};
          sourceType.onclick = function() {library.renderSourcePage(row, userInfo);};

          entry.querySelector('td.link-button').onclick = function() {
            const url = '/api/stash/item/' + item.id + '/source/' + row.id;
            const actions = {
              200: function(response) {
                response.json().then(function(data) {
                  ajaxHandler.hideModals();
                  linkedSources.build(linkedSourcesContainer, item, isEditable);
                });
              }
            };

            ajaxHandler.fetch(this, url, {method: 'POST'}, actions);
          };

          tbody.appendChild(entry);
        }

        resultTable.classList.remove('hidden');
      });
    }
  }
};
