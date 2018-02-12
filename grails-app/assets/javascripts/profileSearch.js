var profileSearchOffsetCounter;

function searchProfile(element, offset) {
    var searchProfileUrl;
    const loadMoreButton = document.querySelector('#load-more-button');
    const profileSearchBox = document.querySelector('#profile-search-box');
    const profileSearchSpinner = document.querySelector('#profile-search-spinner');
    const profileSearchDropdown = document.querySelector('#profile-search-dropdown');
    const profileSearchButton = document.querySelector('#profile-search-button');
    const profileSearchResult = document.querySelector('#profile-search-result');

    var actions = {
            error: error,
            replaceElement: replaceElement,
            none: noMatches,
    };

    if (element == profileSearchButton) {
        loadMoreButton.classList.remove('hidden');
        profileSearchResult.innerHTML = '';
        profileSearchOffsetCounter = 0;
    } else if (element == loadMoreButton) {
        profileSearchOffsetCounter += offset;
    }

    loadMoreButton.disabled = true;
    profileSearchButton.disabled = true;
    profileSearchSpinner.style.display = 'inline';
    profileSearchDropdown.classList.add('open');

    searchProfileUrl = '/profile/search?query=' + profileSearchBox.value + '&offset=' + profileSearchOffsetCounter;
    sendAjaxRequest(element, searchProfileUrl, 'GET', '#profile-search-result', actions);

    function error(element, response) {
        recoverUI();
        profileSearchResult.innerHTML += 'Something went wrong :(';
    }

    function replaceElement(element, response, selector) {
        recoverUI();
        profileSearchResult.innerHTML += response.template;
    }

    function noMatches(element, response, selector) {
        recoverUI();
        loadMoreButton.classList.add('hidden');
        profileSearchResult.innerHTML += response.template;
    }

    function recoverUI() {
        profileSearchButton.disabled = false;
        loadMoreButton.disabled = false;
        profileSearchSpinner.style.display = 'none';
        window.addEventListener('click', eventListener);
    }

    var eventListener = function(event) {
        if ((event.target != profileSearchButton) && (event.target != loadMoreButton)) {
            profileSearchDropdown.classList.remove('open');
        }

        removeListener();
    }

    function removeListener() {
        window.removeEventListener('click', eventListener);
    }
}
