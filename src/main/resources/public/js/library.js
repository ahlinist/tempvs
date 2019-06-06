import {i18n} from './i18n/library-translations.js';
import {i18n as periodI18n} from './i18n/period-translations.js';
import {i18n as classificationI18n} from './i18n/classification-translations.js';
import {smartFormBuilder} from './smart-form/smart-form-builder.js';
import {formValidator} from './validation/form-validator.js';

window.onload = function() {
  library.init();
};

var library = {
  init: function() {
    const location = window.location.href;

    if (location.endsWith("/library")) {
      library.renderLibraryPage();
    } else if (location.endsWith("/library/admin")) {
      library.renderAdminPage();
    } else if (location.includes('period')) {
      const n = location.lastIndexOf('/');
      const period = location.substring(n + 1);
      library.renderPeriodPage(period)
    } else if (location.includes('source')) {
      const n = location.lastIndexOf('/');
      const sourceId = location.substring(n + 1);
      library.loadSource(sourceId);
    }
  },
  isContributionAllowed: function(roles) {
    if (roles) {
      return roles.includes("ROLE_CONTRIBUTOR") || roles.includes("ROLE_SCRIBE")
        || roles.includes("ROLE_ARCHIVARIUS") || roles.includes("ROLE_ADMIN");
    } else {
      return false;
    }
  },
  isEditAllowed: function(roles) {
    if (roles) {
      return roles.includes("ROLE_SCRIBE") || roles.includes("ROLE_ARCHIVARIUS") || roles.includes("ROLE_ADMIN");
    } else {
      return false;
    }
  },
  isAdmin: function(roles) {
    if (roles) {
      return roles.includes("ROLE_ARCHIVARIUS") || roles.includes("ROLE_ADMIN");
    } else {
      return false;
    }
  },
  periods: [
    "ANCIENT",
    "ANTIQUITY",
    "EARLY_MIDDLE_AGES",
    "HIGH_MIDDLE_AGES",
    "LATE_MIDDLE_AGES",
    "RENAISSANCE",
    "MODERN",
    "WWI",
    "WWII",
    "CONTEMPORARY",
    "OTHER"
  ],
  renderLibraryPage: function() {
    const content = document.querySelector("content");
    content.innerHTML = "";
    const libraryTemplate = document.querySelector("template#library");
    const libraryPage = libraryTemplate.content.querySelector('div');
    const libraryPageNode = document.importNode(libraryPage, true);
    content.appendChild(libraryPageNode);
    window.history.pushState("", "", '/library');

    document.querySelector('title').innerHTML = i18n.en.welcomePage.title;
    var periodsSection = document.querySelector('ul#periods-section');
    var periodTemplate = document.querySelector('template.period-template');
    var periodItem = periodTemplate.content.querySelector('li.period-list-item');
    var heading = document.querySelector('h1.period-list-heading');
    var breadcrumbLibrary = document.querySelector('a#breadcrumb-library');

    heading.innerHTML = i18n.en.welcomePage.heading;
    breadcrumbLibrary.innerHTML = i18n.en.breadcrumb.library;

    library.periods.forEach(renderPeriodList);

    function renderPeriodList(periodKey) {
      var periodNode = document.importNode(periodItem, true);
      var detailsLink = periodNode.querySelector('a.period-details-link');
      var thumbnailImg = periodNode.querySelector('img.period-thumbnail-image');
      var periodName = periodNode.querySelector('p.period-name');
      var periodShortDescription = periodNode.querySelector('p.period-short-description');

      detailsLink.href = '/library/period/' + periodKey.toLowerCase();
      detailsLink.onclick = function() {
        library.renderPeriodPage(periodKey.toLowerCase());
      };
      thumbnailImg.src = '/static/images/library/thumbnails/' + periodKey.toLowerCase() + '.jpg';
      periodName.innerHTML = periodI18n.en.period[periodKey].name;
      periodShortDescription.innerHTML = periodI18n.en.period[periodKey].shortDescription;

      periodsSection.appendChild(periodNode);
    }

    var url = '/api/library/library';
      var actions = {200: renderPage};
      ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

      function renderPage(response) {
        response.json().then(function(data) {
          var welcomeSection = document.querySelector('div#welcome-section');
          var welcomeTemplate = document.querySelector('template.welcome-block');
          var welcomeBlock = welcomeTemplate.content.querySelector('div');
          var welcomeBlockNode = document.importNode(welcomeBlock, true);
          var greetingBlock = welcomeBlockNode.querySelector('span.greeting-block');
          var button = welcomeBlockNode.querySelector('a.role-button');
          var buttonText = data.buttonText;
          var role = data.role;

          welcomeSection.innerHTML = '';

          if (data.adminPanelAvailable) {
            button.href = "/library/admin";

            button.onclick = function() {
              library.renderAdminPage();
            };
          } else {
            var method = data.roleRequestAvailable ? 'POST' : 'DELETE';

            button.onclick = function() {
              var url = '/api/library/library/role/' + role;
              ajaxHandler.fetch(null, url, {method: method}, smartFormBuilder.hideSpinners);
            };
          }

          greetingBlock.innerHTML = data.greeting;
          button.innerHTML = buttonText;
          welcomeSection.appendChild(welcomeBlockNode);
        });
      }
  },
  renderAdminPage: function() {
    ajaxHandler.blockUI();
    const content = document.querySelector("content");
    content.innerHTML = "";
    const libraryAdminTemplate = document.querySelector("template#library-admin");
    const libraryAdminPage = libraryAdminTemplate.content.querySelector('div');
    const libraryAdminPageNode = document.importNode(libraryAdminPage, true);
    content.appendChild(libraryAdminPageNode);
    window.history.pushState("", "", '/library/admin');

    document.querySelector('title').innerHTML = i18n.en.adminPage.title;
    var url = '/api/library/library/admin?page=0&size=40';
    var actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      var heading = document.querySelector('h1#admin-panel-heading');
      var userHeader = document.querySelector('table th#user-header');
      var authorityHeader = document.querySelector('table th#authority-header');
      var actionsHeader = document.querySelector('table th#actions-header');
      var breadcrumbLibrary = document.querySelector('a#breadcrumb-library');
      var breadcrumbAdmin = document.querySelector('a#breadcrumb-admin');

      heading.innerHTML = i18n.en.adminPage.heading;
      breadcrumbLibrary.innerHTML = i18n.en.breadcrumb.library;
      breadcrumbAdmin.innerHTML = i18n.en.breadcrumb.admin;
      userHeader.innerHTML = i18n.en.adminPage.user;
      authorityHeader.innerHTML = i18n.en.adminPage.authority;
      actionsHeader.innerHTML = i18n.en.adminPage.actions;

      response.json().then(function(data) {
        var requestsSection = document.querySelector('tbody#requests-section');
        var requestTemplate = document.querySelector('template.request-template');
        var requestBlock = requestTemplate.content.querySelector('tr');

        requestsSection.innerHTML = '';

        data.roleRequests.forEach(renderRoleRequest);

        function renderRoleRequest(roleRequest) {
          var requestBlockNode = document.importNode(requestBlock, true);
          var profileButton = requestBlockNode.querySelector('a.user');
          var authorityCell = requestBlockNode.querySelector('span.authority');
          var acceptButton = requestBlockNode.querySelector('span.accept-request');
          var rejectButton = requestBlockNode.querySelector('span.reject-request');

          profileButton.innerHTML = roleRequest.userName;
          profileButton.href = '/profile/show/' + roleRequest.userProfileId;
          authorityCell.innerHTML = roleRequest.roleLabel;

          acceptButton.onclick = function() {
            var url = '/api/library/library/' + roleRequest.role + '/' + roleRequest.userId;
            ajaxHandler.fetch(null, url, {method: 'POST'}, actions);
          };

          rejectButton.onclick = function() {
            var url = '/api/library/library/' + roleRequest.role + '/' + roleRequest.userId;
            ajaxHandler.fetch(null, url, {method: 'DELETE'}, actions);
          };

          requestsSection.appendChild(requestBlockNode);
        }
      });
    }
  },
  renderPeriodPage: function(periodKey) {
    const content = document.querySelector("content");
    content.innerHTML = "";
    const libraryPeriodTemplate = document.querySelector("template#library-period");
    const libraryPeriodPage = libraryPeriodTemplate.content.querySelector('div');
    const libraryPeriodPageNode = document.importNode(libraryPeriodPage, true);
    content.appendChild(libraryPeriodPageNode);
    window.history.pushState("", "", '/library/period/' + periodKey);

    const period = periodKey.toUpperCase();
    const periodMessageSource = periodI18n.en.period[period];
    document.querySelector('title').innerHTML = periodMessageSource.name;
    var srcProperties = i18n.en.source.properties;
    var classifications = classificationI18n.en.classifications;
    var types = i18n.en.source.types;
    var breadcrumbPeriod = document.querySelector('a#breadcrumb-period');
    var createSourceSection = document.querySelector('div#create-source-section');
    var labels = Object.keys(srcProperties);
    var searchSection = document.querySelector('div#search-section');
    var searchTemplate = searchSection.querySelector('template#search-template');
    var searchCriterion = searchTemplate.content.querySelector('div');
    var sourceTable = document.querySelector('table#source-table');

    if (createSourceSection) {
      var popupButton = createSourceSection.querySelector('#popup-button > span.fa-plus');
      var createSourceForm = createSourceSection.querySelector('form');
      var classificationOptions = createSourceForm.querySelectorAll('select[name=classification] > option');
      var typeOptions = createSourceForm.querySelectorAll('select[name=type] > option');
      var submitButton = createSourceSection.querySelector('.submit-button');
      popupButton.innerHTML = i18n.en.periodPage.createSource.popupButton;

      createSourceForm.onsubmit = function() {
        library.createSource(this);
        return false;
      }

      labels.forEach(function(label) {
        createSourceForm.querySelector('label[for=' + label + ']').innerHTML = srcProperties[label];
      });

      createSourceForm.querySelector('input[name=period]').value = period;
      createSourceForm.querySelector('input[name=fake-period]').value = periodMessageSource.name;
      submitButton.innerHTML = i18n.en.periodPage.createSource.submitButton;

      classificationOptions.forEach(function(option) {
        option.innerHTML = classifications[option.value];
      });

      typeOptions.forEach(function(option) {
        option.innerHTML = types[option.value];
      });
    }

    document.querySelector('h1#period-heading').innerHTML = periodMessageSource.name;
    document.querySelector('div#period-long-description').innerHTML = periodMessageSource.longDescription;
    document.querySelector('h1#sources').innerHTML = i18n.en.periodPage.sourceListHeading;
    document.querySelector('a#breadcrumb-library').innerHTML = i18n.en.breadcrumb.library;
    breadcrumbPeriod.innerHTML = periodMessageSource.name;
    breadcrumbPeriod.href = '/library/period/' + periodKey;
    document.querySelector('img#period-image').src = '/static/images/library/' + periodKey + '.jpg';
    searchSection.querySelector('div#classification-search h4').innerHTML = srcProperties.classification + ":";
    searchSection.querySelector('div#type-search h4').innerHTML = srcProperties.type + ":";
    searchSection.querySelector('input[name=query]').placeholder = i18n.en.periodPage.searchSourcePlaceholder;

    sourceTable.querySelector('th#table-name').innerHTML = srcProperties.name;
    sourceTable.querySelector('th#table-description').innerHTML = srcProperties.description;
    sourceTable.querySelector('th#table-classification').innerHTML = srcProperties.classification;
    sourceTable.querySelector('th#table-type').innerHTML = srcProperties.type;

    buildcheckboxSearch(classifications, 'classification');
    buildcheckboxSearch(types, 'type');

    var searchForm = searchSection.querySelector('form');
    searchForm.querySelector('input[name=period]').value = period;
    library.search(searchForm);

    function buildcheckboxSearch(checkboxGroup, checkboxType) {
      Object.keys(checkboxGroup).forEach(function(checkboxGroupItem) {
        if (checkboxGroupItem) {
          var searchItem = document.importNode(searchCriterion, true);
          var label = searchItem.querySelector('label');
          var checkbox = searchItem.querySelector('input[type=checkbox]');

          label.innerHTML = checkboxGroup[checkboxGroupItem];
          checkbox.name = checkboxType;
          checkbox.value = checkboxGroupItem;

          searchSection.querySelector('div#' + checkboxType + '-search').appendChild(searchItem);
        }
      });
    }
  },
  loadSource: function(sourceId) {
    ajaxHandler.blockUI();
    const url = '/api/library/source/' + sourceId;
    ajaxHandler.fetch(null, url, {method: 'GET'}, {200: renderPage});

    function renderPage(response) {
      const userInfo = JSON.parse(response.headers.get("User-Info"));

      response.json().then(function(data) {
        library.renderSourcePage(data, userInfo);
      });
    }
  },
  renderSourcePage: function(source, userInfo) {
    const sourceId = source.id;
    const sourceName = source.name;
    const content = document.querySelector("content");
    content.innerHTML = "";
    const librarySourceTemplate = document.querySelector("template#library-source");
    const librarySourcePage = librarySourceTemplate.content.querySelector('div');
    const librarySourcePageNode = document.importNode(librarySourcePage, true);
    content.appendChild(librarySourcePageNode);
    window.history.pushState("", "", '/library/source/' + sourceId);

    document.querySelector('title').innerHTML = i18n.en.sourcePage.title;
    const msgSource = i18n.en;
    const sourceForm = document.querySelector('div#source-form');
    const imageUploadForm = document.querySelector('form#image-upload-form');

    const sourceNameLabel = msgSource.source.properties.name;
    const sourceDescriptionLabel = msgSource.source.properties.description;

    var periodName = periodI18n['en'].period[source.period].name;
    document.querySelector('a#breadcrumb-library').innerHTML = i18n.en.breadcrumb.library;
    document.querySelector('a#breadcrumb-period').innerHTML = periodName;
    document.querySelector('a#breadcrumb-period').href = '/library/period/' +  source.period.toLowerCase();
    document.querySelector('a#breadcrumb-source-name').innerHTML = sourceName;
    document.querySelector('a#breadcrumb-source-name').href = '/library/source/' + sourceId;

    sourceForm.querySelector('div.source-name b').innerHTML = sourceNameLabel;
    sourceForm.querySelector('div.source-description b').innerHTML = sourceDescriptionLabel;
    sourceForm.querySelector('div#source-classification b').innerHTML = msgSource.source.properties.classification;
    sourceForm.querySelector('div#source-type b').innerHTML = msgSource.source.properties.type;
    sourceForm.querySelector('div#source-period b').innerHTML = msgSource.source.properties.period;
    imageUploadForm.action = '/api/library/source/' + sourceId + '/images';
    imageUploadForm.querySelector('label[for=image]').innerHTML = i18n.en.sourcePage.uploadImage.imageLabel;
    imageUploadForm.querySelector('label[for=imageInfo]').innerHTML = i18n.en.sourcePage.uploadImage.imageInfoLabel;
    imageUploadForm.querySelector('span#select-file-button i').innerHTML = i18n.en.sourcePage.uploadImage.selectFileButton;
    imageUploadForm.querySelector('button.submit-button').innerHTML = i18n.en.sourcePage.uploadImage.submitButton;

    var imageContainer = document.querySelector('div#image-container');
    var carouselInner = imageContainer.querySelector('div.carousel-inner');
    var modalActivateButton = imageContainer.querySelector('div#modal-activate-button');
    var carouselIndicatorList = imageContainer.querySelector('ol.carousel-indicators');
    var imageIndicatorTemplate = imageContainer.querySelector('template#image-indicator');
    var imageIndicatorItem = imageIndicatorTemplate.content.querySelector('li');
    var carouselInnerTemplate = imageContainer.querySelector('template#carousel-inner');
    var carouselInnerItem = carouselInnerTemplate.content.querySelector('div');

    carouselInner.innerHTML = '';
    carouselIndicatorList.innerHTML = '';

    var firstImageHolder = modalActivateButton.querySelector('div#first-image-holder');

    if (source.images.length) {
      imageContainer.querySelector('div#image-carousel').classList.remove('hidden');
      imageContainer.querySelector('img#default-image').classList.add('hidden');
      modalActivateButton.querySelector('.badge-notify').innerHTML = source.images.length;

      source.images.forEach(function(image, index) {
        var indicatorNode = document.importNode(imageIndicatorItem, true);
        var carouselInnerNode = document.importNode(carouselInnerItem, true);

        indicatorNode.setAttribute('data-slide-to', index);
        carouselInnerNode.querySelector('p.image-info').innerHTML = image.imageInfo;
        var htmlImage = new Image();
        htmlImage.setAttribute("style", "height: 90vh; max-width: 90vw; width: auto; margin-left: auto; margin-right: auto;");
        htmlImage.src = "/api/image/image/" + image.objectId;

        if (index === 0) {
          indicatorNode.classList.add('active');
          carouselInnerNode.classList.add('active');

          var htmlFirstImage = new Image();
          htmlFirstImage.setAttribute("style", "width: 30vw;");
          htmlFirstImage.src = "/api/image/image/" + image.objectId;
          firstImageHolder.innerHTML = '';
          firstImageHolder.appendChild(htmlFirstImage);
        }

        carouselInnerNode.insertBefore(htmlImage, carouselInnerNode.firstChild);
        carouselIndicatorList.appendChild(indicatorNode);
        carouselInner.appendChild(carouselInnerNode);
      });

      var slideMapping = {};

      source.images.forEach(function(entry, index) {
        slideMapping[index] = entry.objectId;
      });

      modalCarousel.init(slideMapping);
    }

    const roles = userInfo ? userInfo.roles : null;
    const isEditable = library.isEditAllowed(roles)

    const updateNameAction = '/api/library/source/' + sourceId + '/name';
    const updateDescriptionAction = '/api/library/source/' + sourceId + '/description';

    smartFormBuilder.build(sourceForm, '.source-name', sourceNameLabel, sourceName, updateNameAction, isEditable);
    smartFormBuilder.build(sourceForm, '.source-description', sourceDescriptionLabel, source.description, updateDescriptionAction, isEditable);

    if (isEditable) {
      var carouselHeader = imageContainer.querySelector('div#carousel-modal-header');
      carouselHeader.querySelector('span#delete-image-wrapper').classList.remove('hidden');
      carouselHeader.querySelector('span#image-deletion-confirmation').innerHTML = i18n.en.sourcePage.deleteImage.confirmation;
      carouselHeader.querySelector('span.yes').innerHTML = i18n.en.sourcePage.deleteImage.yes;
      carouselHeader.querySelector('span.no').innerHTML = i18n.en.sourcePage.deleteImage.no;
      carouselHeader.querySelector('form').action = '/api/library/source/' + sourceId + '/images';
      carouselHeader.querySelector('form').onsubmit = function() {
        modalCarousel.deleteImage(this, {200: library.loadSource});
        return false;
      };
    }

    if (library.isAdmin(roles)) {
      var deleteSrcMessages = i18n.en.sourcePage.deleteSource;
      var deleteSrcSection = document.querySelector('span#delete-source-section');
      deleteSrcSection.classList.remove('hidden');
      deleteSrcSection.querySelector('span#source-deletion-confirmation').innerHTML = deleteSrcMessages.confirmation;
      deleteSrcSection.querySelector('form span.yes').innerHTML = deleteSrcMessages.yes;
      deleteSrcSection.querySelector('form span.no').innerHTML = deleteSrcMessages.no;
      deleteSrcSection.querySelector('form').action = '/api/library/source/' + sourceId;
      deleteSrcSection.querySelector('form').onsubmit = function() {
        library.deleteSource(this, source.period.toLowerCase());
        return false;
      };
    }

    sourceForm.querySelector('#source-classification .text-holder').innerHTML = classificationI18n.en.classifications[source.classification];
    sourceForm.querySelector('#source-type .text-holder').innerHTML = msgSource.source.types[source.type];
    sourceForm.querySelector('#source-period .text-holder').innerHTML = periodName;
  },
  createSource: function(form) {
    var formData = new FormData(form);

    var object = {
      name: formData.get('name'),
      description: formData.get('description'),
      classification: formData.get('classification'),
      type: formData.get('type'),
      period: formData.get('period')
    };

    var validator = {
      name: i18n.en.periodPage.createSource.validation.nameBlank,
      classification: i18n.en.periodPage.createSource.validation.classificationBlank,
      type: i18n.en.periodPage.createSource.validation.typeBlank
    };

    var inputIsValid = true;
    Object.entries(validator).forEach(validate);

    function validate(entry) {
      var fieldName = entry[0];
      var validationMessage = entry[1];

      if (!object[fieldName] || !/\S/.test(object[fieldName])) {
        var field = form.querySelector('[name=' + fieldName + ']');
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

    var payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(object)
    };

    var actions = {
      200: renderSourceDetails,
      400: function(response, form) {
        formValidator.handleBadRequest(response, form);
      }
    };

    function renderSourceDetails(response) {
      response.json().then(function(data) {
        window.location.href = '/library/source/' + data.id;
      });
    }

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, actions);
  },
  search: function(form) {
    var spinner = document.querySelector('img.load-sources-spinner');
    var sourceTable = document.querySelector('table#source-table');
    var formData = new FormData(form);
    var tableBody = sourceTable.querySelector('tbody');

    var object = {
      query: formData.get('query'),
      period: formData.get('period').toUpperCase(),
      classifications: formData.getAll('classification'),
      types: formData.getAll('type')
    };

    var q = window.btoa(JSON.stringify(object));
    var url = '/api/library/source?page=0&size=40&q=' + q;

    var actions = {
      200: displaySearchResult
    };

    spinner.classList.remove('hidden');
    tableBody.innerHTML = '';

    ajaxHandler.fetch(form, url, {method: 'GET'}, actions);

    function displaySearchResult(response) {
      const userInfo = JSON.parse(response.headers.get("User-Info"));

      response.json().then(function(data) {
        var sourceTemplate = document.querySelector('template#source-template');
        var sourceItem = sourceTemplate.content.querySelector('tr');

        spinner.classList.add('hidden');

        data.forEach(function(source) {
          var sourceNode = document.importNode(sourceItem, true);

          sourceNode.querySelector('td.source-name').innerHTML = source.name;
          sourceNode.onclick = function() {
            library.renderSourcePage(source, userInfo);
          };
          sourceNode.querySelector('td.source-description').innerHTML = source.description;
          sourceNode.querySelector('td.source-classification').innerHTML = classificationI18n.en.classifications[source.classification];
          sourceNode.querySelector('td.source-type').innerHTML = i18n.en.source.types[source.type];
          tableBody.appendChild(sourceNode);
        });
      });
    }
  },
  deleteSource: function(form, period) {
    var actions = {
      200: redirectToPeriodPage
    };

    function redirectToPeriodPage() {
      window.location.href = '/library/period/' + period;
    }

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, {method: 'DELETE'}, actions);
  },
  uploadImage: function(form) {
    var formData = new FormData(form);
    var image = formData.get('image');
    var imageInfo = formData.get('imageInfo');

    var actions = {200: library.loadSource};

    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(image);
      reader.onload = () => {
        let encoded = reader.result.replace(/^data:(.*;base64,)?/, '');
        if ((encoded.length % 4) > 0) {
          encoded += '='.repeat(4 - (encoded.length % 4));
        }

        var object = {
          imageInfo: imageInfo,
          content: encoded,
          fileName: image.name
        };

        resolve(object);
      };
      reader.onerror = error => reject(error);
    })
    .then(
      data => {
        var payload = {
          method: 'POST',
          headers:{
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        };

        ajaxHandler.blockUI();
        ajaxHandler.fetch(form, form.action, payload, actions);
      }
    );
  }
};
