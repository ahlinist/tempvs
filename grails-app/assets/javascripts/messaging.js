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
    conversation: function(conversationId, selector, page, size) {
        var isValid = true;
        var isHidden = true;
        var url = '/message/loadMessages/' + conversationId + '?page=' + page + '&size=' + size;
        ajaxHandler.processAjaxRequest(document, url, null, 'GET', selector, ajaxHandler.actions, isValid, isHidden);
        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversationId);
    },
    loadConversations: function() {
        var url = '/message/loadConversations?page=0&size=40';
        ajaxHandler.processAjaxRequest(document, url, null, 'GET', '#conversationsBox', ajaxHandler.actions, true, true);
    }
}
