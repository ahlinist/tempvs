var messaging = {
    actions: {
        //TODO: replace with statuscode
        replaceElement: function(element, response, selector) {
            var container = document.querySelector(selector);
            container.innerHTML = response.template;
            messaging.scrollMessagesDown();
        },
        //TODO: replace with statuscode
        appendElement: function(element, response, selector) {
            var container = document.querySelector(selector);
            container.innerHTML += response.template;
            messaging.scrollMessagesDown();
        },
        200: function(response) {
          response.json().then(function(data) {
            profileSearcher.recoverUI();

            for (var i = 0; i < data.length; i++) {
              var li = document.createElement('li');
              li.classList.add('row');
              var a = document.createElement('a');
              a.classList.add('btn', 'btn-default', 'col-sm-12');
              var profileId = data[i].id;
              var profileName = data[i].name;
              a.onclick = function() { messaging.chooseProfile(profileId, profileName); };
              a.innerHTML = data[i].name;
              li.appendChild(a);
              profileSearcher.searchResult.appendChild(li);
            }
          });
        }
    },
    send: function(form, conversationId) {
      var url = '/message/send/' + conversationId;
      var data = new FormData(form);

      if (!data.get('message')) {
        return;
      }

      var payload = {
        method: 'POST',
        body: data
      };

      ajaxHandler.fetch(url, payload, messaging.loadMessagesActions);
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
        200: function(response) {
          response.json().then(function(data) {
            var conversations = data.conversations
            var conversationsBox = document.querySelector('ul#conversationsBox');
            conversationsBox.innerHTML = '';

            for (var j = 0; j < conversations.length; j++) {
              var conversationRow = buildConversationOption(conversations[j]);
              conversationsBox.appendChild(conversationRow);
            }

            function buildConversationOption(conversation) {
              var lastMessage = conversation.lastMessage;
              var subject = lastMessage.subject;
              var li = document.createElement('li');
              li.classList.add('btn', 'btn-default', 'col-sm-12');
              li.onclick = function() {messaging.conversation(conversation.id, '#messagesBox', 0, 40);}

              if (lastMessage.unread) {
                li.style.backgroundColor = '#E9F9FF';
              }

              var b = document.createElement('b');
              b.classList.add('pull-left');
              b.innerHTML = conversation.conversant;

              var br = document.createElement('br');
              var i = document.createElement('i');
              i.classList.add('pull-left');
              i.innerHTML = lastMessage.author.name + ': ' + lastMessage.text;

              if (subject) {
                i.innerHTML += ' ' + subject.name;
              }

              li.appendChild(b);
              li.appendChild(br);
              li.appendChild(i);

              return li;
            }
          });
        }
      }

      var url = '/message/loadConversations?page=0&size=40';
      ajaxHandler.fetch(url, {method: 'GET'}, actions);
    },
    scrollMessagesDown: function() {
        var scrollable = document.querySelector('div#messagesScroll');
        scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
    },
    chooseProfile: function(profileId, profileName) {
      var profileToAdd = document.querySelector('#profileToAdd');
      profileToAdd.innerHTML = '';
      var a = document.createElement('a');
      a.classList.add('btn', 'btn-default', 'col-sm-10');
      a.href = '/profile/show/' + profileId;
      a.innerHTML = profileName;

      var ok = document.createElement('span');
      ok.classList.add('glyphicon', 'glyphicon-ok', 'btn', 'btn-default', 'col-sm-1');
      ok.style.color = 'green';
      ok.style.borderRadius = '150px';
      ok.onclick = function() { messaging.updateParticipants(profileId, 'ADD') };

      var remove = document.createElement('span');
      remove.classList.add('glyphicon', 'glyphicon-remove', 'btn', 'btn-default', 'col-sm-1');
      remove.style.color = 'red';
      remove.style.borderRadius = '150px';
      remove.onclick = function() { profileToAdd.innerHTML = '';};

      profileToAdd.appendChild(a);
      profileToAdd.appendChild(ok);
      profileToAdd.appendChild(remove);
    },
    updateParticipants: function(subjectId, updateAction) {
      var conversationId = document.querySelector('div#conversationIdHolder').innerHTML;
      var url = '/message/updateParticipants/' + conversationId;

      var payload = {
        method: 'POST',
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: 'updateAction=' + updateAction + '&subject=' + subjectId
      };

      ajaxHandler.blockUI();
      ajaxHandler.fetch(url, payload, messaging.loadMessagesActions);
    },
    loadMessagesActions: {
      200: function(response) {
        response.json().then(function(data) {
          var messageBox = document.querySelector('div#messagesBox');
          ajaxHandler.hideModals();
          messageBox.innerHTML = data.template;
          var newConversationId = document.querySelector('div#conversationIdHolder').innerHTML;
          window.history.pushState("", "Tempvs - Message", '/message/conversation/' + newConversationId);
          ajaxHandler.unblockUI();
          messaging.scrollMessagesDown();
          messaging.loadConversations();
        });
      },
      404: function(response) {
        console.log('Status code 404 returned.');
      }
    }
}
