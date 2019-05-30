import {i18n} from './i18n/stash-translations.js';
import {smartFormBuilder} from './smart-form/smart-form-builder.js';

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
    } else if (location.includes("/stash/") && !location.endsWith("/stash")) {
      const n = location.lastIndexOf('/');
      const userId = location.substring(n + 1);
      stash.renderStash(userId)
    } else if (location.endsWith("/stash")) {
      stash.renderStash();
    }
  },
  renderStash: function(userId) {
    const content = document.querySelector("content");
    content.innerHTML = "";
    const stashTemplate = document.querySelector("template#stash");
    const stashPage = stashTemplate.content.querySelector('div');
    const stashPageNode = document.importNode(stashPage, true);
    content.appendChild(stashPageNode);

    const stashSection = document.querySelector("#stash-section");
    const messageSource = i18n.en.stash;
    document.querySelector('title').innerHTML = messageSource.title;
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
        const groupList = stashSection.querySelector('ul#group-list');
        const groupListItemTemplate = stashSection.querySelector('template#group-list-item-template');
        const userId = data.owner.id;
        const userName = data.owner.userName;
        const breadCrumb = stashSection.querySelector("a#breadcrumb-stash");
        breadCrumb.href = "/stash/" + userId;
        breadCrumb.innerHTML = messageSource.breadCrumb + " (" + userName + ")";

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
          const groupListItem = groupListItemTemplate.content.querySelector('li');
          const groupListItemNode = document.importNode(groupListItem, true);
          const groupLink = groupListItemNode.querySelector("a");
          groupLink.href = "/stash/" + group.id;
          groupLink.onclick = function() {
            stash.renderGroup(group, userInfo);
            return false;
          }
          groupLink.querySelector("b.group-name").innerHTML = group.name;
          groupLink.querySelector("p.group-description").innerHTML = group.description;
          groupList.appendChild(groupLink);
        }
      });
    }
  },
  loadGroup: function(groupId) {
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
    const groupId = group.id;
    const groupName = group.name;
    const groupDescription = group.description;
    const userId = userInfo.userId;
    const userName = userInfo.userName;
    const content = document.querySelector("content");
    content.innerHTML = "";
    const stashTemplate = document.querySelector("template#item-group");
    const stashPage = stashTemplate.content.querySelector('div');
    const stashPageNode = document.importNode(stashPage, true);
    content.appendChild(stashPageNode);

    window.history.pushState("", "", '/stash/group/' + groupId);
    const groupSection = document.querySelector("#group-section");
    const messageSource = i18n.en.stash;
    document.querySelector('title').innerHTML = messageSource.title;
    const breadCrumbStash = groupSection.querySelector("a#breadcrumb-stash");
    breadCrumbStash.href = "/stash/" + userId;
    breadCrumbStash.innerHTML = messageSource.breadCrumb + " (" + userName + ")";
    const breadCrumbGroup = groupSection.querySelector("a#breadcrumb-item-group");
    breadCrumbGroup.href = "/stash/group/" + groupId;
    breadCrumbGroup.innerHTML = groupName;
    groupSection.querySelector('h1#item-list-heading').innerHTML = messageSource.group.heading;

    const groupNameLabel = i18n.en.stash.group.nameLabel;
    const groupDescriptionLabel = i18n.en.stash.group.descriptionLabel;
    const updateNameAction = '/api/stash/group/' + groupId + '/name';
    const updateDescriptionAction = '/api/stash/group/' + groupId + '/description';
    const groupForm = groupSection.querySelector(".group-form");
    smartFormBuilder.build(groupForm, '.group-name', groupNameLabel, groupName, updateNameAction);
    smartFormBuilder.build(groupForm, '.group-description', groupDescriptionLabel, groupDescription, updateDescriptionAction);

    const url = '/api/stash/items';
    const actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage() {
      alert("items found!");
    }
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
