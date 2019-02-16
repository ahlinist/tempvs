var library = {
  welcomePage: function() {
    var url = '/library/api/library';
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
            window.location.href = '/library/admin';
          };
        } else {
          var method = data.roleRequestAvailable ? 'POST' : 'DELETE';

          button.onclick = function() {
            var url = '/library/api/library/role/' + role;
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
    var url = '/library/api/library/admin';
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
            var url = '/library/api/library/' + roleRequest.role + '/' + roleRequest.userId;
            ajaxHandler.fetch(null, url, {method: 'POST'}, actions);
          };

          rejectButton.onclick = function() {
            var url = '/library/api/library/' + roleRequest.role + '/' + roleRequest.userId;
            ajaxHandler.fetch(null, url, {method: 'DELETE'}, actions);
          };

          requestsSection.appendChild(requestBlockNode);
        }
      });
    }
  }
};
