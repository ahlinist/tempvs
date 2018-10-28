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
      var actions = {
        success: function(data) {
          var conversations = data.conversations
          var conversationsBox = document.querySelector('ul#conversationsBox');

          for (var j = 0; j < conversations.length; j++) {
            var conversationRow = buildConversationOption(conversations[j]);
            conversationsBox.appendChild(conversationRow);
          }

          function buildConversationOption(conversation) {
            var li = document.createElement('li');
            li.classList.add('btn', 'btn-default', 'col-sm-12');
            li.onclick = function() {messaging.conversation(conversation.id, '#messagesBox', 0, 40);}

            if (conversation.lastMessage.unread) {
              li.style.backgroundColor = '#E9F9FF';
            }

            var b = document.createElement('b');
            b.classList.add('pull-left');
            b.innerHTML = conversation.conversant;

            var br = document.createElement('br');

            var i = document.createElement('i');
            i.classList.add('pull-left');
            i.innerHTML = conversation.lastMessage.text;

            li.appendChild(b);
            li.appendChild(br);
            li.appendChild(i);

            return li;
          }
        }
      }

      var url = '/message/loadConversations?page=0&size=40';
      ajaxHandler.fetch(url, {method: 'GET'}, actions);
    },
    scrollMessagesDown: function() {
        var scrollable = document.querySelector('div#messagesScroll');
        scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
    }
}
