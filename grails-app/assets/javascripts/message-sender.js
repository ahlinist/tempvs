var messageSender = {
    send: function(form, selector, url) {
        var isValid = true;
        var isHidden = true;
        var data = new FormData(form);

        if (!data.get('message')) {
            return;
        }

        ajaxHandler.processAjaxRequest(document, url, data, 'POST', selector, ajaxHandler.actions, isValid, isHidden);
    }
}
