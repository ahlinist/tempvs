var profileDropdown = {
    actions: {
        fillDropdown: function(element, response, selector) {
            var body = response.body;
            var profileList = element.querySelector('ul#profiles');

            if (body) {
                if (body.current) {
                    element.querySelector('span#current-profile-name').innerHTML = body.current;
                }

                if (body.user) {
                    var userProfile = profileList.querySelector('li#user-profile > a')
                    userProfile.innerHTML = body.user;
                    userProfile.href = '/profile/switchProfile/';
                }

                if (body.club) {
                    for (var i = 0; i < body.club.length; i++) {
                        createClubProfileLink (body.club[i].id, body.club[i].name)
                    }
                }
            }

            function createClubProfileLink (id, name) {
                var a = document.createElement('a');
                a.classList.add('list-group-item');
                a.classList.add('disableable');
                a.href = '/profile/switchProfile/' + id;
                a.innerHTML = name;
                var li = document.createElement('li');
                li.appendChild(a);
                profileList.appendChild(li);
            }
        },
        none: function() {},
    },
    displayDropdown: function() {
        var selector = 'span#profile-dropdown';
        var url = '/profile/getProfileDropdown';
        var dropdown = document.querySelector(selector);
        var isValid = true;
        var isSpinnerHidden = true;
        ajaxHandler.processAjaxRequest(dropdown, url, '', 'GET', selector, profileDropdown.actions, isValid, isSpinnerHidden);
    }
};
