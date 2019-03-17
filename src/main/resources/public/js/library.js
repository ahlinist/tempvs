window.onload = function() {
  library.init();
};

var library = {
  init: function() {
    var location = window.location.href;

    if (location.endsWith("/library")) {
      library.renderWelcomePage();
    } else if (location.endsWith("/admin")) {
      library.renderAdminPage();
    } else if (location.includes('period')) {
      var n = location.lastIndexOf('/');
      var period = location.substring(n + 1);
      library.renderPeriodPage(period)
    } else if (location.includes('source')) {
      var n = location.lastIndexOf('/');
      var sourceId = location.substring(n + 1);
      library.renderSourcePage(sourceId);
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
  actions: {
    200: function() {
      var elementsToHide = document.querySelectorAll('.hide-me');
      elementsToHide.forEach(function(element) {
        element.classList.add('hidden');
        element.classList.remove('hide-me');
      });
    }
  },
  renderWelcomePage: function() {
    var periodsSection = document.querySelector('ul#periods-section');
    var periodTemplate = document.querySelector('template.period-template');
    var periodItem = periodTemplate.content.querySelector('li.period-list-item');
    var title = document.querySelector('title');
    var heading = document.querySelector('h1.period-list-heading');
    var breadcrumbLibrary = document.querySelector('a#breadcrumb-library');

    title.innerHTML = library.i18n.en.welcomePage.title;
    heading.innerHTML = library.i18n.en.welcomePage.heading;
    breadcrumbLibrary.innerHTML = library.i18n.en.breadcrumb.library;

    library.periods.forEach(renderPeriodList);

    function renderPeriodList(periodKey) {
      var periodNode = document.importNode(periodItem, true);
      var detailsLink = periodNode.querySelector('a.period-details-link');
      var thumbnailImg = periodNode.querySelector('img.period-thumbnail-image');
      var periodName = periodNode.querySelector('p.period-name');
      var periodShortDescription = periodNode.querySelector('p.period-short-description');

      detailsLink.href = '/library/period/' + periodKey.toLowerCase();
      thumbnailImg.src = '/assets/library/thumbnails/' + periodKey.toLowerCase() + '.jpg';
      periodName.innerHTML = library.i18n.en.period[periodKey].name;
      periodShortDescription.innerHTML = library.i18n.en.period[periodKey].shortDescription;

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
          var button = welcomeBlockNode.querySelector('span.role-button');
          var buttonText = data.buttonText;
          var role = data.role;

          welcomeSection.innerHTML = '';

          if (data.adminPanelAvailable) {
            button.onclick = function() {
              window.location.href = 'library/admin';
            };
          } else {
            var method = data.roleRequestAvailable ? 'POST' : 'DELETE';

            button.onclick = function() {
              var url = '/api/library/library/role/' + role;
              ajaxHandler.fetch(null, url, {method: method}, library.actions);
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

    var url = '/api/library/library/admin?page=0&size=40';
    var actions = {200: renderPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderPage(response) {
      var title = document.querySelector('title');
      var heading = document.querySelector('h1#admin-panel-heading');
      var userHeader = document.querySelector('table th#user-header');
      var authorityHeader = document.querySelector('table th#authority-header');
      var actionsHeader = document.querySelector('table th#actions-header');
      var breadcrumbLibrary = document.querySelector('a#breadcrumb-library');
      var breadcrumbAdmin = document.querySelector('a#breadcrumb-admin');

      title.innerHTML = library.i18n.en.adminPage.title;
      heading.innerHTML = library.i18n.en.adminPage.heading;
      breadcrumbLibrary.innerHTML = library.i18n.en.breadcrumb.library;
      breadcrumbAdmin.innerHTML = library.i18n.en.breadcrumb.admin;
      userHeader.innerHTML = library.i18n.en.adminPage.user;
      authorityHeader.innerHTML = library.i18n.en.adminPage.authority;
      actionsHeader.innerHTML = library.i18n.en.adminPage.actions;

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
    var period = periodKey.toUpperCase()
    var srcProperties = library.i18n.en.source.properties;
    var classifications = library.i18n.en.source.classifications;
    var types = library.i18n.en.source.types;
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
      popupButton.innerHTML = library.i18n.en.periodPage.createSource.popupButton;

      labels.forEach(function(label) {
        createSourceForm.querySelector('label[for=' + label + ']').innerHTML = srcProperties[label];
      });

      createSourceForm.querySelector('input[name=period]').value = period;
      createSourceForm.querySelector('input[name=fake-period]').value = library.i18n.en.period[period].name;
      submitButton.innerHTML = library.i18n.en.periodPage.createSource.submitButton;

      classificationOptions.forEach(function(option) {
        option.innerHTML = classifications[option.value];
      });

      typeOptions.forEach(function(option) {
        option.innerHTML = types[option.value];
      });
    }

    document.querySelector('title').innerHTML = library.i18n.en.period[period].name;
    document.querySelector('h1#period-heading').innerHTML = library.i18n.en.period[period].name;
    document.querySelector('div#period-long-description').innerHTML = library.i18n.en.period[period].longDescription;
    document.querySelector('h1#sources').innerHTML = library.i18n.en.periodPage.sourceListHeading;
    document.querySelector('a#breadcrumb-library').innerHTML = library.i18n.en.breadcrumb.library;
    breadcrumbPeriod.innerHTML = library.i18n.en.period[period].name;
    breadcrumbPeriod.href = '/library/period/' + periodKey;
    document.querySelector('img#period-image').src = '/assets/library/' + periodKey + '.jpg';
    searchSection.querySelector('div#classification-search h4').innerHTML = srcProperties.classification + ":";
    searchSection.querySelector('div#type-search h4').innerHTML = srcProperties.type + ":";
    searchSection.querySelector('input[name=query]').placeholder = library.i18n.en.periodPage.searchSourcePlaceholder;

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
  renderSourcePage: function(sourceId) {
    ajaxHandler.blockUI();

    var msgSource = library.i18n.en;
    var sourceForm = document.querySelector('div#source-form');
    var imageUploadForm = document.querySelector('form#image-upload-form');

    document.querySelector('title').innerHTML = library.i18n.en.sourcePage.title;
    document.querySelector('a#breadcrumb-library').innerHTML = library.i18n.en.breadcrumb.library;
    sourceForm.querySelector('div#source-name b').innerHTML = msgSource.source.properties.name;
    sourceForm.querySelector('div#source-description b').innerHTML = msgSource.source.properties.description;
    sourceForm.querySelector('div#source-classification b').innerHTML = msgSource.source.properties.classification;
    sourceForm.querySelector('div#source-type b').innerHTML = msgSource.source.properties.type;
    sourceForm.querySelector('div#source-period b').innerHTML = msgSource.source.properties.period;
    sourceForm.querySelector('div#source-name form').action = '/api/library/source/' + sourceId + '/name';
    sourceForm.querySelector('div#source-description form').action = '/api/library/source/' + sourceId + '/description';
    imageUploadForm.action = '/api/library/source/' + sourceId + '/images';
    imageUploadForm.querySelector('label[for=image]').innerHTML = library.i18n.en.sourcePage.uploadImage.imageLabel;
    imageUploadForm.querySelector('label[for=imageInfo]').innerHTML = library.i18n.en.sourcePage.uploadImage.imageInfoLabel;
    imageUploadForm.querySelector('span#select-file-button i').innerHTML = library.i18n.en.sourcePage.uploadImage.selectFileButton;
    imageUploadForm.querySelector('button.submit-button').innerHTML = library.i18n.en.sourcePage.uploadImage.submitButton;

    var url = '/api/library/source/' + sourceId;
    var actions = {200: renderSourcePage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderSourcePage(response) {
      var userInfo = response.headers.get("User-Info");

      if (userInfo) {
        var roles = JSON.parse(userInfo).roles;
      }

      response.json().then(function(data) {
        var periodName = msgSource.period[data.period].name;
        var imageContainer = document.querySelector('div#image-container');
        var carouselInner = imageContainer.querySelector('div.carousel-inner');
        var modalActivateButton = imageContainer.querySelector('div#modal-activate-button');
        var carouselIndicatorList = imageContainer.querySelector('ol.carousel-indicators');
        var imageIndicatorTemplate = imageContainer.querySelector('template#image-indicator');
        var imageIndicatorItem = imageIndicatorTemplate.content.querySelector('li');
        var carouselInnerTemplate = imageContainer.querySelector('template#carousel-inner');
        var carouselInnerItem = carouselInnerTemplate.content.querySelector('div');

        carouselIndicatorList.innerHTML = '';
        document.querySelector('a#breadcrumb-period').innerHTML = periodName;
        document.querySelector('a#breadcrumb-period').href = '/library/period/' +  data.period.toLowerCase();
        document.querySelector('a#breadcrumb-source-name').innerHTML = data.name;
        document.querySelector('a#breadcrumb-source-name').href = '/library/source/' + data.id;
        sourceForm.querySelector('#source-name .text-holder').innerHTML = data.name;
        sourceForm.querySelector('#source-description .text-holder').innerHTML = data.description;

        modalActivateButton.querySelector('.badge-notify').innerHTML = data.images.length;

        data.images.forEach(function(image, index) {
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
            modalActivateButton.appendChild(htmlFirstImage);
          }

          carouselInnerNode.insertBefore(htmlImage, carouselInnerNode.firstChild);
          carouselIndicatorList.appendChild(indicatorNode);
          carouselInner.appendChild(carouselInnerNode);
        });

        if (library.isEditAllowed(roles)) {
          sourceForm.querySelector('#source-name input').value = data.name;
          sourceForm.querySelector('#source-name form').action = '/api/library/source/' + data.id + '/name';
          sourceForm.querySelector('#source-description input').value = data.description;
          sourceForm.querySelector('#source-description form').action = '/api/library/source/' + data.id + '/description';
          sourceForm.querySelector('#source-name .smart-form-activator').classList.remove('hidden');
          sourceForm.querySelector('#source-description .smart-form-activator').classList.remove('hidden');

          sourceForm.querySelector('#source-name .smart-form-activator').onclick = function() {
            ajaxHandler.activateSmartForm(this, library.actions, 'PATCH');
          };

          sourceForm.querySelector('#source-description .smart-form-activator').onclick = function() {
            ajaxHandler.activateSmartForm(this, library.actions, 'PATCH');
          };
        }

        if (library.isAdmin(roles)) {
          var deleteSrcMessages = library.i18n.en.sourcePage.deleteSource;
          var deleteSrcSection = document.querySelector('span#delete-source-section');
          deleteSrcSection.classList.remove('hidden');
          deleteSrcSection.querySelector('span#source-deletion-confirmation').innerHTML = deleteSrcMessages.confirmation;
          deleteSrcSection.querySelector('form span.yes').innerHTML = deleteSrcMessages.yes;
          deleteSrcSection.querySelector('form span.no').innerHTML = deleteSrcMessages.no;
          deleteSrcSection.querySelector('form').action = '/api/library/source/' + data.id;
          deleteSrcSection.querySelector('form').onsubmit = function() {
            library.deleteSource(this, data.period.toLowerCase());
            return false;
          };
        }

        sourceForm.querySelector('#source-classification .text-holder').innerHTML = msgSource.source.classifications[data.classification];
        sourceForm.querySelector('#source-type .text-holder').innerHTML = msgSource.source.types[data.type];
        sourceForm.querySelector('#source-period .text-holder').innerHTML = periodName;
      });
    }
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
      name: library.i18n.en.periodPage.createSource.validation.nameBlank,
      classification: library.i18n.en.periodPage.createSource.validation.classificationBlank,
      type: library.i18n.en.periodPage.createSource.validation.typeBlank
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
        ajaxHandler.handleBadRequest(response, form);
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
      response.json().then(function(data) {
        var sourceTemplate = document.querySelector('template#source-template');
        var sourceItem = sourceTemplate.content.querySelector('tr');

        spinner.classList.add('hidden');

        data.forEach(function(source) {
          var sourceNode = document.importNode(sourceItem, true);
          var msgSource = library.i18n.en.source;

          sourceNode.querySelector('td.source-name').innerHTML = source.name;
          sourceNode.onclick = function() {window.location.href = '/library/source/' + source.id};
          sourceNode.querySelector('td.source-description').innerHTML = source.description;
          sourceNode.querySelector('td.source-classification').innerHTML = msgSource.classifications[source.classification];
          sourceNode.querySelector('td.source-type').innerHTML = msgSource.types[source.type];
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

    var actions = {
      200: function(response) {
        response.json().then(function(data) {
          //TODO: append to carousel
        });
      }
    };

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

        ajaxHandler.fetch(form, form.action, payload, actions);
      }
    );
  },
  i18n: {
    en: {
      source: {
        properties: {
          name: 'Name',
          description: 'Description',
          classification: 'Classification',
          type: 'Type',
          period: 'Period'
        },
        classifications: {
          "": "-",
          CLOTHING: "Clothing",
          FOOTWEAR: "Footwear",
          HOUSEHOLD: "Household",
          WEAPON: "Weapon",
          ARMOR: "Armor",
          OTHER: "Other"
        },
        types: {
          "": "-",
          WRITTEN: "Written",
          GRAPHIC: "Graphic",
          ARCHAEOLOGICAL: "Archaeological",
          OTHER: "Other"
        }
      },
      adminPage: {
        title: "Admin panel",
        heading:  "Requested Authorities",
        user: "User",
        authority: "Authority",
        actions: "Actions"
      },
      welcomePage: {
        title: "Library",
        heading: "Historical periods",
      },
      periodPage: {
        sourceListHeading: "Sources",
        searchSourcePlaceholder: "Find source...",
        createSource: {
          popupButton: " New source",
          submitButton: "Create source",
          validation: {
            nameBlank: 'Please choose a name',
            classificationBlank: 'Please choose a classification',
            typeBlank: 'Please choose a type'
          }
        }
      },
      sourcePage: {
        title: 'Source',
        uploadImage: {
          imageLabel: 'Image',
          imageInfoLabel: 'Image Info',
          selectFileButton: 'Select file',
          submitButton: 'Submit'
        },
        deleteSource: {
          confirmation: 'Are you sure you want to delete this source?',
          yes: 'Yes',
          no: 'No'
        }
      },
      breadcrumb: {
        library: "Library",
        admin: "Admin"
      },
      period: {
        ANCIENT: {
          name: "Ancient times",
          shortDescription: "Ancient history is the aggregate of past events from the beginning of recorded human history and extending as far as the Early Middle Ages or the Post-classical Era that begins in 5th century",
          longDescription: "Historians have two major avenues which they take to better understand the ancient world: archaeology and the study of source texts. Primary sources are those sources closest to the origin of the information or idea under study. Primary sources have been distinguished from secondary sources, which often cite, comment on, or build upon primary sources. <br/> Archaeology is the excavation and study of artefacts in an effort to interpret and reconstruct past human behavior. Archaeologists excavate the ruins of ancient cities looking for clues as to how the people of the time period lived. Some important discoveries by archaeologists studying ancient history include: The Egyptian pyramids, The study of the ancient cities of Harappa, The Terracotta Army. <br/> A fundamental difficulty of studying ancient history is that recorded histories cannot document the entirety of human events, and only a fraction of those documents have survived into the present day. Furthermore, the reliability of the information obtained from these surviving records must be considered. Few people were capable of writing histories, as literacy was not widespread in almost any culture until long after the end of ancient history."
        },
        ANTIQUITY: {
          name: "Antiquity",
          shortDescription: "Antiquity is a broad term for a long period of cultural history centered around the Mediterranean Sea, which begins roughly with the earliest-recorded Greek poetry of Homer (9th century BC),  and continues through the rise of Christianity and the fall of the Western Roman Empire (5th century AD)",
          longDescription: "Classical antiquity is the long period of cultural history centered on the Mediterranean Sea, comprising the interlocking civilizations of ancient Greece and ancient Rome, collectively known as the Greco-Roman world. It is the period in which Greek and Roman society flourished and wielded great influence throughout Europe, North Africa and Western Asia. <br/> Conventionally, it is taken to begin with the earliest-recorded Epic Greek poetry of Homer (8th–7th century BC), and continues through the emergence of Christianity and the decline of the Roman Empire (5th century AD). It ends with the dissolution of classical culture at the close of Late Antiquity (300–600), blending into the Early Middle Ages (600–1000). Such a wide sampling of history and territory covers many disparate cultures and periods. Classical antiquity may refer also to an idealised vision among later people of what was, in Edgar Allan Poe's words, \"the glory that was Greece, and the grandeur that was Rome.\" <br/> Most of what is known of the ancient world comes from the accounts of antiquity's own historians. Although it is important to take into account the bias of each ancient author, their accounts are the basis for our understanding of the ancient past. Some of the more notable ancient writers include Herodotus, Thucydides, Arrian, Plutarch, Polybius, Sima Qian, Sallust, Livy, Josephus, Suetonius, and Tacitus."
        },
        EARLY_MIDDLE_AGES: {
          name: "Early Middle Ages",
          shortDescription: "The Early Middle Ages or Early Medieval Period, typically regarded as lasting from the 6th century to the 10th century CE, marked the start of the Middle Ages of European history. The Early Middle Ages followed the decline of the Western Roman Empire and preceded the High Middle Ages (c. 10th to 13th centuries)",
          longDescription: "Early Middle Ages saw a continuation of trends begun during late classical antiquity, including population decline, especially in urban centres, a decline of trade, and increased immigration. The period has been labelled the \"Dark Ages\", a characterization highlighting the relative scarcity of literary and cultural output from this time, especially in Northwestern Europe. The term \"Dark Ages\" is inappropriate when applied to the Iberian peninsula, since during the Caliphate of Córdoba and Taifas periods Spanish culture, learning, arts, and science flourished as nowhere else in Europe, and Córdoba was the largest city in the world. However, the Eastern Roman Empire, or Byzantine Empire, continued to survive, though in the 7th century the Islamic caliphates conquered swathes of formerly Roman territory. <br/> Many of these trends were reversed later in the period. In 800 the title of emperor was revived in Western Europe by Charlemagne, whose Carolingian Empire greatly affected later European social structure and history. Europe experienced a return to systematic agriculture in the form of the feudal system, which introduced such innovations as three-field planting and the heavy plough. Barbarian migration stabilized in much of Europe, although Northern Europe was greatly affected by the Viking expansion."
        },
        HIGH_MIDDLE_AGES: {
          name: "High Middle Ages",
          shortDescription: "The High Middle Ages or High Medieval Period was the period of European history lasting from AD 1000 to 1250. The High Middle Ages are preceded by the Early Middle Ages and followed by the Late Middle Ages, which by convention end around 1500",
          longDescription: "Key historical trends of the High Middle Ages include the rapidly increasing population of Europe, which brought about great social and political change from the preceding era, and the \"Renaissance of the 12th century\", including the first developments of rural exodus and urbanization. By 1250 the robust population increase greatly benefited the European economy, reaching levels that would not be seen again in some areas until the 19th century. This trend faltered in the Late Middle Ages due to a series of calamities, notably the Black Death but also including numerous wars and economic stagnation. <br> The Carolingian Renaissance led to scientific and philosophical revivals in Europe. The first universities started operating – in Bologna, Paris, Oxford and Modena. The Vikings settled in the British Isles, France and elsewhere, whilst Norse Christian kingdoms started developing in their Scandinavian homelands. The Magyars ceased their expansion in the 10th century, and by the year 1000 a Christian Kingdom of Hungary had become a recognized state in Central Europe, forming alliances with regional powers. With the brief exception of the Mongol invasions in the 13th century, major nomadic incursions ceased. The powerful Byzantine Empire of the Macedonian and Komnenos dynasties gradually gave way to the resurrected Serbia and Bulgaria and to a successor Crusader state (1204 to 1261), while countering the continuous threat of the Seljuk Turks in Asia Minor."
        },
        LATE_MIDDLE_AGES: {
          name: "Late Middle Ages",
          shortDescription: "The Late Middle Ages or Late Medieval Period was the period of European history lasting from 1250-1500 AD. The Late Middle Ages followed the High Middle Ages and preceded the onset of the early modern era (and, in much of Europe, the Renaissance)",
          longDescription: "Around 1300, centuries of prosperity and growth in Europe came to a halt. A series of famines and plagues, including the Great Famine of 1315–1317 and the Black Death, reduced the population to around half of what it was before the calamities. Along with depopulation came social unrest and endemic warfare. France and England experienced serious peasant uprisings, such as the Jacquerie and the Peasants' Revolt, as well as over a century of intermittent conflict in the Hundred Years' War. To add to the many problems of the period, the unity of the Catholic Church was temporarily shattered by the Western Schism. Collectively these events are sometimes called the Crisis of the Late Middle Ages. <br/> Despite these crises, the 14th century was also a time of great progress in the arts and sciences. Following a renewed interest in ancient Greek and Roman texts that took root in the High Middle Ages, the Italian Renaissance began. The absorption of Latin texts had started before the Renaissance of the 12th century through contact with Arabs during the Crusades, but the availability of important Greek texts accelerated with the capture of Constantinople by the Ottoman Turks, when many Byzantine scholars had to seek refuge in the West, particularly Italy. <br> Combined with this influx of classical ideas was the invention of printing, which facilitated dissemination of the printed word and democratized learning. These two things would later lead to the Protestant Reformation. Toward the end of the period, the Age of Discovery began. The expansion of the Ottoman Empire cut off trading possibilities with the east. Europeans were forced to seek new trading routes, leading to the Spanish expedition under Columbus to the Americas in 1492, and Vasco da Gama’s voyage to Africa and India in 1498. Their discoveries strengthened the economy and power of European nations."
        },
        RENAISSANCE: {
          name: "Renaissance",
          shortDescription: "The Renaissance is a period in European history, marking the transition from the Middle Ages to the Modern Era and covering the span between the 14th and 17th centuries. Renaissance is known for extensive overseas exploration and spreading of humanistic ideas across Europe",
          longDescription: "The intellectual basis of the Renaissance was its own invented version as that of Protagoras, who said that \"Man is the measure of all things.\" This new thinking became manifest in art, architecture, politics, science and literature. Early examples were the development of perspective in oil painting and the recycled knowledge of how to make concrete. Although the invention of metal movable type sped the dissemination of ideas from the later 15th century, the changes of the Renaissance were not uniformly experienced across Europe. <br/> As a cultural movement, the Renaissance encompassed innovative flowering of Latin and vernacular literatures, beginning with the 14th-century resurgence of learning based on classical sources, which contemporaries credited to Petrarch; the development of linear perspective and other techniques of rendering a more natural reality in painting; and gradual but widespread educational reform. In politics, the Renaissance contributed to the development of the customs and conventions of diplomacy, and in science to an increased reliance on observation and inductive reasoning. Although the Renaissance saw revolutions in many intellectual pursuits, as well as social and political upheaval, it is perhaps best known for its artistic developments and the contributions of such polymaths as Leonardo da Vinci and Michelangelo, who inspired the term \"Renaissance man\". <br/> The Renaissance began in Florence, in the 14th century. Various theories have been proposed to account for its origins and characteristics, focusing on a variety of factors including the social and civic peculiarities of Florence at the time: its political structure; the patronage of its dominant family, the Medici; and the migration of Greek scholars and texts to Italy following the Fall of Constantinople to the Ottoman Turks. Other major centres were northern Italian city-states such as Venice, Genoa, Milan, Bologna, and finally Rome during the Renaissance Papacy."
        },
        MODERN: {
          name: "Modern period",
          shortDescription: "Modern era began approximately in the early 16th century; notable historical milestones included the European Renaissance, the Age of Discovery, and the Protestant Reformation. Modern period ends together with World War II and beginning of contemporary history.",
          longDescription: "The period witnessed the exploration and colonization of the Americas and the rise of sustained contacts between previously isolated parts of the globe. The historical powers became involved in global trade, as the exchange of goods, plants, animals, and food crops extended to the Old World and the New World. The Columbian Exchange greatly affected the human environment. <br/> New economies and institutions emerged, becoming more sophisticated and globally articulated over the course of the early modern period. This process began in the medieval North Italian city-states, particularly Genoa, Venice, and Milan. The European colonization of the Americas, Asia, and Africa occurred during the 15th to 19th centuries, and spread Christianity around the world. <br/> The early modern trends in various regions of the world represented a shift away from medieval modes of organization, politically and economically. Feudalism declined in Europe, while the period also included the Protestant Reformation, the disastrous Thirty Years' War, the Commercial Revolution, the European colonization of the Americas, and the Golden Age of Piracy. <br/> The European Revolutions of 1848, known in some countries as the Spring of Nations or the Year of Revolution, were a series of political upheavals throughout the European continent. Described as a revolutionary wave, the period of unrest began in France and then, further propelled by the French Revolution of 1848, soon spread to the rest of Europe. <br/> Industrial age reform movements began the gradual change of society rather than with episodes of rapid fundamental changes. The reformists' ideas were often grounded in liberalism, although they also possessed aspects of utopian, socialist or religious concepts. The Radical movement campaigned for electoral reform, a reform of the Poor Laws, free trade, educational reform, postal reform, prison reform, and public sanitation. <br/> Following the Enlightenment's ideas, the reformers looked to the Scientific Revolution and industrial progress to solve the social problems which arose with the Industrial Revolution. Newton's natural philosophy combined a mathematics of axiomatic proof with the mechanics of physical observation, yielding a coherent system of verifiable predictions and replacing a previous reliance on revelation and inspired truth. Applied to public life, this approach yielded several successful campaigns for changes in social policy."
        },
        WWI: {
          name: "World War I",
          shortDescription: "World War I was a global war originating in Europe that lasted from 28 July 1914 to 11 November 1918. More than 70 million military personnel, including 60 million Europeans, were mobilised in one of the largest wars in history",
          longDescription: "The war drew in all the world's economic great powers, assembled in two opposing alliances: the Allies (based on the Triple Entente of the Russian Empire, the French Third Republic, and the United Kingdom of Great Britain and Ireland) versus the Central Powers of Germany and Austria-Hungary. Although Italy was a member of the Triple Alliance alongside Germany and Austria-Hungary, it did not join the Central Powers, as Austria-Hungary had taken the offensive against the terms of the alliance. These alliances were reorganised and expanded as more nations entered the war: Italy, Japan and the United States joined the Allies, while the Ottoman Empire and Bulgaria joined the Central Powers. <br/> The trigger for the war was the assassination of Archduke Franz Ferdinand of Austria, heir to the throne of Austria-Hungary, by Yugoslav nationalist Gavrilo Princip in Sarajevo on 28 June 1914. This set off a diplomatic crisis when Austria-Hungary delivered an ultimatum to the Kingdom of Serbia and, as a result, entangled-international-alliances, formed over the previous decades, were invoked. Within weeks the major powers were at war, and the conflict soon spread around the world. <br> Russia was the first to order a partial mobilization of its armies on 24–25 July, and when on 28 July Austria-Hungary declared war on Serbia, Russia declared general mobilization on 30 July. Germany presented an ultimatum to Russia to demobilise, and when this was refused, declared war on Russia on 1 August. Being outnumbered on the Eastern Front, Russia urged its Triple Entente ally France to open up a second front in the west. <br/> Japan entered the war on the side of the Allies on 23 August 1914, seizing the opportunity of Germany's distraction with the European War to expand its sphere of influence in China and the Pacific."
        },
        WWII: {
          name: "World War II",
          shortDescription: "World War II, also known as the Second World War, was a global war that lasted from 1939 to 1945, although related conflicts began earlier. World War II was the deadliest conflict in human history, marked by 50 to 85 million fatalities",
          longDescription: "The world war is generally said to have begun on 1 September 1939, the day of the invasion of Poland by Nazi Germany and the subsequent declarations of war on Germany by France and the United Kingdom. From late 1939 to early 1941, in a series of campaigns and treaties, Germany conquered or controlled much of continental Europe, and formed the Axis alliance with Italy and Japan. Under the Molotov–Ribbentrop Pact of August 1939, Germany and the Soviet Union partitioned and annexed territories of their European neighbours, Poland, Finland, Romania and the Baltic states. The war continued primarily between the European Axis powers and the coalition of the United Kingdom and the British Commonwealth, with campaigns including the North Africa and East Africa campaigns, the aerial Battle of Britain, the Blitz bombing campaign, and the Balkan Campaign, as well as the long-running Battle of the Atlantic. On 22 June 1941, the European Axis powers launched an invasion of the Soviet Union, opening the largest land theatre of war in history, which trapped the major part of the Axis military forces into a war of attrition. In December 1941, Japan attacked the United States and European colonies in the Pacific Ocean, and quickly conquered much of the Western Pacific. <br/> The Axis advance halted in 1942 when Japan lost the critical Battle of Midway, and Germany and Italy were defeated in North Africa and then, decisively, at Stalingrad in the Soviet Union. In 1943, with a series of German defeats on the Eastern Front, the Allied invasion of Sicily and the Allied invasion of Italy which brought about Italian surrender, and Allied victories in the Pacific, the Axis lost the initiative and undertook strategic retreat on all fronts. In 1944, the Western Allies invaded German-occupied France, while the Soviet Union regained all of its territorial losses and invaded Germany and its allies. <br/> The war in Europe concluded with an invasion of Germany by the Western Allies and the Soviet Union, culminating in the capture of Berlin by Soviet troops, the suicide of Adolf Hitler and the subsequent German unconditional surrender on 8 May 1945. Following the Potsdam Declaration by the Allies on 26 July 1945 and the refusal of Japan to surrender under its terms, the United States dropped atomic bombs on the Japanese cities of Hiroshima and Nagasaki on 6 and 9 August respectively. With an invasion of the Japanese archipelago imminent, the possibility of additional atomic bombings and the Soviet invasion of Manchuria, Japan formally surrendered on 2 September 1945. Thus ended the war in Asia, cementing the total victory of the Allies."
        },
        CONTEMPORARY: {
          name: "Contemporary times",
          shortDescription: "Contemporary history is a subset of modern history which describes the historical period from approximately 1945 to the present. Science began transforming after 1945: spaceflight, nuclear technology, laser and semiconductor technology were developed alongside molecular biology and genetics, particle physics, and the Standard Model of quantum field theory",
          longDescription: "Contemporary history is politically dominated by the Cold War (1945–91) between the United States and Soviet Union whose effects were felt across the world. The confrontation, which was mainly fought through proxy wars and through intervention in the internal politics of smaller nations, ultimately ended with the dissolution of the Soviet Union and Warsaw Pact in 1991, following the Revolutions of 1989. The latter stages and aftermath of the Cold War enabled the democratisation of much of Europe, Africa, and Latin America. In the Middle East, the period after 1945 was dominated by conflict involving the new state of Israel and the rise of petroleum politics, as well as the growth of Islamism after the 1980s. The first supranational organisations of government, such as the United Nations and European Union, emerged during the period after 1945, while the European colonial empires in Africa and Asia collapsed, gone by 1975. Countercultures rose and the sexual revolution transformed social relations in western countries between the 1960s and 1980s, epitomised by the Protests of 1968. Living standards rose sharply across the developed world because of the post-war economic boom, whereby such major economies as Japan and West Germany emerged. The culture of the United States, especially consumerism, spread widely. By the 1960s, many western countries had begun deindustrializing; in their place, globalization led to the emergence of new industrial centres, such as Japan, Taiwan and later China, which exported consumer goods to developed countries. <br/> The most significant contemporary times conflicts took place in Vietnam, Afghanistan, Iraq and Syria."
        },
        OTHER: {
          name: "Other",
          shortDescription: "Hardly related to any of the given periods",
          longDescription: "\"Other\" is related to items, sources, profiles etc. that can't be represented by the existing ones"
        }
      }
    }
  }
};
