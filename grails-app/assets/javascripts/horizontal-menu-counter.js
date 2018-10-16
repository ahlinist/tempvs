var horizontalMenuCounter = {
    actions: {
        displayCounter: function(element, response, selector) {
            var count = response.count;

            if (count > 0) {
                var counter = document.querySelector(selector);
                counter.classList.remove('hidden');
                counter.innerHTML = response.count;
            }
        },
        none: function() {},
    },
    displayCounter: function(selector, url) {
        var element = document.querySelector(selector);
        var isValid = true;
        var isHidden = true;
        ajaxHandler.processAjaxRequest(element, url, '', 'GET', selector, horizontalMenuCounter.actions, isValid, isHidden);
    }
};
