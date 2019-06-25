import {i18n} from './i18n/stash-translations.js';
import {i18n as periodI18n} from './i18n/period-translations.js';
import {i18n as classificationI18n} from './i18n/classification-translations.js';
import {pageBuilder} from './page/page-builder.js';

window.onload = function() {
  stash.init();
};

let stash = {
  init: function() {
    const location = window.location.href;

    if (location.includes("/group/")) {
      const n = location.lastIndexOf('/');
      const groupId = location.substring(n + 1);
      stash.loadGroup(groupId)
    } else if (location.includes("/stash/item")) {
      const n = location.lastIndexOf('/');
      const itemId = location.substring(n + 1);
      stash.loadItem(itemId);
    } else if (location.includes("/stash/") && !location.endsWith("/stash")) {
      const n = location.lastIndexOf('/');
      const userId = location.substring(n + 1);
      stash.renderStash(userId)
    } else if (location.endsWith("/stash")) {
      stash.renderStash();
    }
  },
  renderStash: function(userId) {
    pageBuilder.initPage('template#stash', '/stash', i18n.en.stash.title);

    const stashSection = document.querySelector("#stash-section");
    const messageSource = i18n.en.stash;
    stashSection.querySelector('h1#group-list-heading').innerHTML = messageSource.groups.heading;
    let url = '/api/stash/group';

    if (userId !== undefined) {
      url += '?userId=' + userId;
    }

    const actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      const userInfo = JSON.parse(response.headers.get("User-Info"));
      const currentUserId = userInfo.userId;

      response.json().then(function(data) {
        const groupList = stashSection.querySelector('div#group-list');
        const groupListItemTemplate = stashSection.querySelector('template#group-list-item-template');
        const userId = data.owner.id;
        const userName = data.owner.userName;

        pageBuilder.breadcrumb([
            {url: '/stash/' + userId, text: messageSource.breadCrumb + " (" + userName + ")"}
        ]);

        if (currentUserId == userId) {
          const createGroupSection = stashSection.querySelector("div#create-group-section");
          createGroupSection.classList.remove("hidden");
          createGroupSection.querySelector("form label[for=name]").innerHTML = messageSource.groups.create.name;
          createGroupSection.querySelector("form label[for=description]").innerHTML = messageSource.groups.create.description;
          createGroupSection.querySelector("form button.submit-button").innerHTML = messageSource.groups.create.createButton;
        }

        for (const group of data.groups) {
            renderGroupListItem(group);
        }

        function renderGroupListItem(group) {
          const groupListItem = groupListItemTemplate.content.querySelector('div');
          const groupListItemNode = document.importNode(groupListItem, true);
          const groupLink = groupListItemNode.querySelector("a");
          groupLink.href = "/stash/" + group.id;
          groupLink.onclick = function() {
            stash.renderGroup(group, userInfo);
            return false;
          }
          groupLink.querySelector("b.group-name").innerHTML = group.name;
          groupLink.querySelector("p.group-description").innerHTML = group.description || '&nbsp;';
          groupList.appendChild(groupLink);
        }
      });
    }
  },
  loadGroup: function(groupId) {
    ajaxHandler.blockUI();
    const url = '/api/stash/group/' + groupId;
    const actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      const userInfo = JSON.parse(response.headers.get("User-Info"));

      response.json().then(function(data) {
        stash.renderGroup(data, userInfo);
      });
    }
  },
  renderGroup: function(group, userInfo) {
    pageBuilder.initPage('template#item-group', '/stash/group/' + group.id, i18n.en.stash.title + ' - ' + group.name);

    const groupId = group.id;
    const groupName = group.name;
    const groupDescription = group.description;

    pageBuilder.breadcrumb([
        {url: '/stash/' + userInfo.userId, text: i18n.en.stash.breadCrumb + " (" + userInfo.userName + ")"},
        {url: '/stash/group/' + groupId, text: groupName}
    ]);

    const groupSection = document.querySelector("#group-section");
    groupSection.querySelector('h1.item-list-heading').innerHTML = i18n.en.stash.group.heading;

    const groupNameLabel = i18n.en.stash.group.nameLabel;
    const groupDescriptionLabel = i18n.en.stash.group.descriptionLabel;
    const updateNameAction = '/api/stash/group/' + groupId + '/name';
    const updateDescriptionAction = '/api/stash/group/' + groupId + '/description';
    const groupForm = groupSection.querySelector(".group-form");

    const isEditable = group.owner.id == userInfo.userId;
    pageBuilder.smartForm(groupForm, groupNameLabel + ' *', groupName, 'name', updateNameAction, isEditable);
    pageBuilder.smartForm(groupForm, groupDescriptionLabel, groupDescription, 'description', updateDescriptionAction, isEditable);

    if (isEditable) {
      const createItemForm = document.querySelector('.create-item-form');
      const itemProperties = i18n.en.stash.items.properties;
      const classifications = classificationI18n.en.classifications;
      const periods = periodI18n.en.period;
      const labels = Object.keys(itemProperties);
      const classificationOptions = createItemForm.querySelectorAll('select[name=classification] > option');
      const periodOptions = createItemForm.querySelectorAll('select[name=period] > option');
      const submitButton = createItemForm.querySelector('.submit-button');
      createItemForm.action = '/api/stash/group/' + groupId + '/item';

      createItemForm.onsubmit = function() {
        stash.createItem(this);
        return false;
      }

      labels.forEach(function(label) {
        createItemForm.querySelector('label[for=' + label + ']').innerHTML = itemProperties[label];
      });

      classificationOptions.forEach(function(option) {
        option.innerHTML = classifications[option.value];
      });

      periodOptions.forEach(function(option) {
        option.innerHTML = periods[option.value].name;
      });

      submitButton.innerHTML = i18n.en.stash.items.submitButton;
      createItemForm.onsubmit = function() {
        stash.createItem(this);
        return false;
      }

      const createItemButton = document.querySelector('button.create-item-button');
      createItemButton.classList.remove('hidden');
      createItemButton.querySelector('span.text-holder').innerHTML = i18n.en.stash.items.createButton;
    }

    const url = '/api/stash/group/' + groupId + '/item?page=0&size=40';
    const actions = {200: renderItemList};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderItemList(response) {
      const itemListTemplate = document.querySelector('template.item-list-template');
      const itemListBlock = document.querySelector('.item-list-block');

      response.json().then(function(data) {
        for (const item of data) {
          renderItem(item);
        }

        function renderItem(item) {
          const listItem = itemListTemplate.content.querySelector('div');
          const listItemNode = document.importNode(listItem, true);
          const itemLink = listItemNode.querySelector('a');
          itemLink.href = '/stash/' + item.itemGroup.id + '/item/' + item.id;
          itemLink.onclick = function() {
            stash.renderItem(item, userInfo);
            return false;
          }
          itemLink.querySelector("b.item-name").innerHTML = item.name;
          itemLink.querySelector("p.item-description").innerHTML = item.description || "&nbsp;";
          itemListBlock.appendChild(itemLink);
        }
      });
    }
  },
  loadItem: function(itemId) {
    ajaxHandler.blockUI();
    const url = '/api/stash/item/' + itemId;
    const actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      const userInfo = JSON.parse(response.headers.get("User-Info"));

      response.json().then(function(data) {
        stash.renderItem(data, userInfo);
      });
    }
  },
  renderItem: function(item, userInfo) {
    pageBuilder.initPage('template#item', '/stash/item/' + item.id, i18n.en.stash.title + ' - ' + item.name);

    pageBuilder.breadcrumb([
      {url: '/stash/' + userInfo.userId, text: i18n.en.stash.breadCrumb + " (" + userInfo.userName + ")"},
      {url: '/stash/group/' + item.itemGroup.id, text: item.itemGroup.name},
      {url: '/stash/item/' + item.id, text: item.name}
    ]);

    const itemForm = document.querySelector('.item-form');

    const isEditable = item.itemGroup.owner.id == userInfo.userId;
    const updateNameAction = '/api/stash/item/' + item.id + '/name';
    const updateDescriptionAction = '/api/stash/item/' + item.id + '/description';
    const itemNameLabel = i18n.en.stash.items.properties.name;
    const itemDescriptionLabel = i18n.en.stash.items.properties.description;
    const itemClassificationLabel = i18n.en.stash.items.properties.classification;
    const itemPeriodLabel = i18n.en.stash.items.properties.period;

    const periodName = periodI18n.en.period[item.period].name;
    const classificationName = classificationI18n.en.classifications[item.classification];

    pageBuilder.smartForm(itemForm, itemNameLabel + ' *', item.name, 'name', updateNameAction, isEditable);
    pageBuilder.smartForm(itemForm, itemDescriptionLabel, item.description, 'description', updateDescriptionAction, isEditable);
    pageBuilder.smartForm(itemForm, itemClassificationLabel, classificationName);
    pageBuilder.smartForm(itemForm, itemPeriodLabel, periodName);
  },
  createItem: function(form) {
    const messageSource = i18n.en.stash.items.create.validation;
    const formData = new FormData(form);

    const object = {
      name: formData.get('name'),
      description: formData.get('description'),
      classification: formData.get('classification'),
      period: formData.get('period'),
    };

    const validator = {
      name: messageSource.nameBlank,
      classification: messageSource.classificationMissing,
      period: messageSource.periodMissing
    };

    let inputIsValid = true;
    Object.entries(validator).forEach(validate);

    function validate(entry) {
      const fieldName = entry[0];
      const validationMessage = entry[1];

      if (!object[fieldName] || !/\S/.test(object[fieldName])) {
        let field = form.querySelector('[name=' + fieldName + ']');
        field.setAttribute('data-toggle', 'tooltip');
        field.setAttribute('data-placement', 'top');
        field.setAttribute('title', validationMessage);
        $(field).tooltip('show');
        inputIsValid = false;
      }
    }

    if (!inputIsValid) {
      return;
    }

    const payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(object)
    };

    const actions = {
      200: renderItem,
      400: function(response, form) {
        ajaxHandler.handleBadRequest(response, form);
      }
    };

    function renderItem(response) {
      response.json().then(function(data) {
        alert("item created!");
      });
    }

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, actions);
  },
  createGroup: function(form) {
    const formData = new FormData(form);

    const object = {
      name: formData.get('name'),
      description: formData.get('description'),
    };

    if (!object.name) {
      const field = form.querySelector('[name=name]');
      field.setAttribute('data-toggle', 'tooltip');
      field.setAttribute('data-placement', 'top');
      field.setAttribute('title', i18n.en.stash.groups.create.validation.nameBlank);
      $(field).tooltip('show');
      return;
    }

    const payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(object)
    };

    const actions = {
      200: renderGroupItems,
      400: function(response, form) {
        ajaxHandler.handleBadRequest(response, form);
      }
    };

    function renderGroupItems(response) {
      response.json().then(function(data) {
        window.location.href = '/item/group/' + data.id;
      });
    }

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, actions);
  }
}
