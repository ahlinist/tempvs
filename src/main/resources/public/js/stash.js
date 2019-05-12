window.onload = function() {
  stash.init();
};

let stash = {
  init: function() {
    const location = window.location.href;

    if (location.includes("/stash")) {
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
    const messageSource = stash.i18n.en.stash;
    document.querySelector('title').innerHTML = messageSource.title;
    stashSection.querySelector('h1#group-list-heading').innerHTML = messageSource.groups.heading;
    const url = '/api/stash/group';
    const actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      const currentUserId = JSON.parse(response.headers.get("User-Info")).userId;

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
          groupLink.querySelector("b.group-name").innerHTML = group.name;
          groupLink.querySelector("p.group-description").innerHTML = group.description;
          groupList.appendChild(groupLink);
        }
      });
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
      field.setAttribute('title', stash.i18n.en.stash.groups.create.validation.nameBlank);
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
  },
  i18n: {
    en: {
      stash: {
        breadCrumb: "Stash",
        title: "Stash",
        groups: {
          heading: "Item groups",
          create: {
            name: "Name",
            description: "Description",
            createButton: "Create group",
            validation: {
              nameBlank: "Name can not be blank"
            }
          }
        }
      }
    }
  }
}
