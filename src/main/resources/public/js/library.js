var library = {
  periods: [
    "ancient",
    "antiquity",
    "early_middle_ages",
    "high_middle_ages",
    "late_middle_ages",
    "renaissance",
    "modern",
    "wwi",
    "wwii",
    "contemporary",
    "other"
  ],
  welcomePage: function() {
    var url = '/api/library/library';
    var actions = {200: renderWelcomePage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderWelcomePage(response) {
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
  adminPage: function() {
    var url = '/api/library/library/admin';
    var actions = {200: renderAdminPage};
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);

    function renderAdminPage(response) {
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

      detailsLink.href = '/library/period/' + periodKey;
      thumbnailImg.src = '/assets/library/thumbnails/' + periodKey + '.jpg';
      periodName.innerHTML = library.i18n.en.period[periodKey].name;
      periodShortDescription.innerHTML = library.i18n.en.period[periodKey].shortDescription;

      periodsSection.appendChild(periodNode);
    }

    library.welcomePage();
  },
  renderAdminPage: function() {
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

    library.adminPage();
  },
  renderPeriodPage: function(periodKey) {
    var title = document.querySelector('title');
    var periodHeading = document.querySelector('h1#period-heading');
    var sourceListHeading = document.querySelector('h1#source-list');
    var breadcrumbLibrary = document.querySelector('a#breadcrumb-library');
    var breadcrumbPeriod = document.querySelector('a#breadcrumb-period');
    var periodImage = document.querySelector('img#period-image');

    title.innerHTML = library.i18n.en.period[periodKey].name;
    periodHeading.innerHTML = library.i18n.en.period[periodKey].name;
    sourceListHeading.innerHTML = library.i18n.en.periodPage.sourceListHeading;
    breadcrumbLibrary.innerHTML = library.i18n.en.breadcrumb.library;
    breadcrumbPeriod.innerHTML = library.i18n.en.period[periodKey].name;
    breadcrumbPeriod.href = '/library/period/' + periodKey;
    periodImage.src = '/assets/library/' + periodKey + '.jpg';
  },
  i18n: {
    en: {
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
        sourceListHeading: "Sources"
      },
      breadcrumb: {
        library: "Library",
        admin: "Admin"
      },
      period: {
        ancient: {
          name: "Ancient times",
          shortDescription: "Ancient history is the aggregate of past events from the beginning of recorded human history and extending as far as the Early Middle Ages or the Post-classical Era that begins in 5th century"
        },
        antiquity: {
          name: "Antiquity",
          shortDescription: "Antiquity is a broad term for a long period of cultural history centered around the Mediterranean Sea, which begins roughly with the earliest-recorded Greek poetry of Homer (9th century BC),  and continues through the rise of Christianity and the fall of the Western Roman Empire (5th century AD)"
        },
        early_middle_ages: {
          name: "Early Middle Ages",
          shortDescription: "The Early Middle Ages or Early Medieval Period, typically regarded as lasting from the 6th century to the 10th century CE, marked the start of the Middle Ages of European history. The Early Middle Ages followed the decline of the Western Roman Empire and preceded the High Middle Ages (c. 10th to 13th centuries)"
        },
        high_middle_ages: {
          name: "High Middle Ages",
          shortDescription: "The High Middle Ages or High Medieval Period was the period of European history lasting from AD 1000 to 1250. The High Middle Ages are preceded by the Early Middle Ages and followed by the Late Middle Ages, which by convention end around 1500"
        },
        late_middle_ages: {
          name: "Late Middle Ages",
          shortDescription: "The Late Middle Ages or Late Medieval Period was the period of European history lasting from 1250-1500 AD. The Late Middle Ages followed the High Middle Ages and preceded the onset of the early modern era (and, in much of Europe, the Renaissance)"
        },
        renaissance: {
          name: "Renaissance",
          shortDescription: "The Renaissance is a period in European history, marking the transition from the Middle Ages to the Modern Era and covering the span between the 14th and 17th centuries. Renaissance is known for extensive overseas exploration and spreading of humanistic ideas across Europe"
        },
        modern: {
          name: "Modern period",
          shortDescription: "Modern era began approximately in the early 16th century; notable historical milestones included the European Renaissance, the Age of Discovery, and the Protestant Reformation. Modern period ends together with World War II and beginning of contemporary history."
        },
        wwi: {
          name: "World War I",
          shortDescription: "World War I was a global war originating in Europe that lasted from 28 July 1914 to 11 November 1918. More than 70 million military personnel, including 60 million Europeans, were mobilised in one of the largest wars in history"
        },
        wwii: {
          name: "World War II",
          shortDescription: "World War II, also known as the Second World War, was a global war that lasted from 1939 to 1945, although related conflicts began earlier. World War II was the deadliest conflict in human history, marked by 50 to 85 million fatalities"
        },
        contemporary: {
          name: "Contemporary times",
          shortDescription: "Contemporary history is a subset of modern history which describes the historical period from approximately 1945 to the present. Science began transforming after 1945: spaceflight, nuclear technology, laser and semiconductor technology were developed alongside molecular biology and genetics, particle physics, and the Standard Model of quantum field theory"
        },
        other: {
          name: "Other",
          shortDescription: "Hardly related to any of the given periods"
        }
      }
    }
  }
};
