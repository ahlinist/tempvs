var messaging = {
    send: function(form, selector, conversationId) {
        var isValid = true;
        var isHidden = true;
        var data = new FormData(form);
        var url = '/message/add/' + conversationId

        if (!data.get('message')) {
            return;
        }

        ajaxHandler.processAjaxRequest(document, url, data, 'POST', selector, ajaxHandler.actions, isValid, isHidden);
    },
    conversation: function(conversationId, selector) {
        var isValid = true;
        var isHidden = true;
        ajaxHandler.processAjaxRequest(document, '/message/loadMessages/' + conversationId, null, 'GET', selector, ajaxHandler.actions, isValid, isHidden);
        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversationId);
    }
}
