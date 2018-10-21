var messaging = {
    actions: {
        replaceElement: function(element, response, selector) {
            var container = document.querySelector(selector);
            container.innerHTML = response.template;
            messaging.scrollMessagesDown();
        },
        appendElement: function(element, response, selector) {
            var container = document.querySelector(selector);
            container.innerHTML += response.template;
            messaging.scrollMessagesDown();
        },
    },
    send: function(form, selector, conversationId) {
        var isValid = true;
        var isHidden = true;
        var data = new FormData(form);
        var url = '/message/add/' + conversationId

        if (!data.get('message')) {
            return;
        }

        ajaxHandler.processAjaxRequest(document, url, data, 'POST', selector, messaging.actions, isValid, isHidden);
    },
    conversation: function(conversationId, selector, page, size) {
        var isValid = true;
        var isHidden = true;
        var url = '/message/loadMessages/' + conversationId + '?page=' + page + '&size=' + size;
        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversationId);
        ajaxHandler.processAjaxRequest(document, url, null, 'GET', selector, messaging.actions, isValid, isHidden);
    },
    loadConversations: function() {
        var url = '/message/loadConversations?page=0&size=40';
        ajaxHandler.processAjaxRequest(document, url, null, 'GET', '#conversationsBox', messaging.actions, true, true);
    },
    scrollMessagesDown: function() {
        var scrollable = document.querySelector('div#messagesScroll');
        scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
    }
}
