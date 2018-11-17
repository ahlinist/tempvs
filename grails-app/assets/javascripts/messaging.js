var messaging = {
    defaultConversationsPage: 0,
    defaultConversationsSize: 40,
    currentConversationsPage: 0,
    actions: {
      200: function(response) {
        response.json().then(function(data) {
          var messageBox = document.querySelector('div#messagesBox');
          ajaxHandler.hideModals();
          messageBox.innerHTML = data.template;
          var newConversationId = document.querySelector('div#conversationIdHolder').innerHTML;
          window.history.pushState("", "Tempvs - Message", '/message/conversation/' + newConversationId);
          messaging.scrollMessagesDown();
          messaging.loadConversations(false);
          messaging.displayNewMessagesCounter();
        });
      },
      400: function(response, form) {
        response.json().then(function (data) {
          for (entry in data) {
            var fieldEntry = data[entry];
            var field = form.querySelector('[name="' + fieldEntry.name + '"]')

            if (!field) {
                field = form.querySelector('.submit-button');
            }

            field.classList.add('popped-over');

            $(field).popover({
                placement: 'bottom',
                content: fieldEntry.message,
                html: true,
                container: 'body'
            }).popover('show');
          }

          messaging.scrollMessagesDown();
          messaging.loadConversations(false);
          messaging.displayNewMessagesCounter();
        });
      },
      404: function(response) {
        console.log('Status code 404 returned.');

        messaging.scrollMessagesDown();
        messaging.loadConversations(false);
        messaging.displayNewMessagesCounter();
      }
    },
    addParticipantActions: {
      200: function(response) {
        var container = document.querySelector('#add-participant-to-conversation-container');

        response.json().then(function(data) {
          profileSearcher.recoverUI();

          for (var i = 0; i < data.length; i++) {
            (function(){
              var profileId = data[i].id;
              var profileName = data[i].name;

              if (document.querySelector('.active-conversation-participant[href$="' + data[i].id + '"]')) {
                return;
              }

              var li = document.createElement('li');
              li.classList.add('row');
              var a = document.createElement('a');
              a.classList.add('btn', 'btn-default', 'col-sm-12');

              a.onclick = function() {
                container.innerHTML = '';
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
                remove.onclick = function() { container.innerHTML = '';};

                container.appendChild(a);
                container.appendChild(ok);
                container.appendChild(remove);
              };

              a.innerHTML = data[i].name;
              li.appendChild(a);
              profileSearcher.searchResult.appendChild(li);
            })();
          }
        });
      }
    },
    createConversation: function(form) {
      //TODO: add UI internationalized validation messages

      var text = form.querySelector('textarea[name=text]');
      var receivers = form.querySelector('input[name^="receivers["]');

      if (!text.value || !text.value.trim()) {
        return;
      }

      if (!receivers) {
        return;
      }

      ajaxHandler.blockUI();
      var url = form.action;

      var payload = {
        method: 'POST',
        body: new FormData(form)
      }

      ajaxHandler.fetch(form, url, payload, messaging.actions);
    },
    createConversationActions: {
      200: function(response) {
        response.json().then(function(data) {
          profileSearcher.recoverUI();
          var createConversationPopup = document.querySelector('#create-conversation-popup');
          var conversationNameContainer = createConversationPopup.querySelector('#conversation-name-container');
          var participantsList = createConversationPopup.querySelector('#create-conversation-participants-container');

          for (var i = 0; i < data.length; i++) {
            (function(){
              var profileId = data[i].id;
              var profileName = data[i].name;

              if (participantsList.querySelector('input[name^="receivers"][value="' + profileId + '"]')) {
                return;
              }

              var li = document.createElement('li');
              var a = document.createElement('a');
              a.classList.add('btn', 'btn-default', 'col-sm-12');

              a.onclick = function() {
                var participantsCounterHolder = createConversationPopup.querySelector('span#participants-counter');
                var participantsCounter = participantsCounterHolder.innerHTML;
                var li = document.createElement('li');
                var a = document.createElement('a');
                a.classList.add('btn', 'btn-default', 'create-conversation-participant');
                a.style.width = '524px';
                a.style.margin = '0 4px 0 0';
                a.href = '/profile/show/' + profileId;
                a.innerHTML = profileName;

                var remove = document.createElement('span');
                remove.classList.add('glyphicon', 'glyphicon-remove', 'btn', 'btn-default');
                remove.style.color = 'red';
                remove.style.borderRadius = '150px';
                remove.onclick = function() {
                  participantsList.removeChild(li);
                  toggleConversationNameContainer();
                };

                var hidden = document.createElement('input');
                hidden.type = 'hidden';
                hidden.value = profileId;
                hidden.name = 'receivers[' + participantsCounter + ']';
                participantsCounterHolder.innerHTML = ++participantsCounter;

                li.appendChild(a);
                li.appendChild(remove);
                li.appendChild(hidden);
                participantsList.appendChild(li);
                toggleConversationNameContainer();

                function toggleConversationNameContainer() {
                  if (participantsList.querySelectorAll('input[name^="receivers"]').length > 1) {
                    conversationNameContainer.style.display = 'block';
                  } else {
                    conversationNameContainer.style.display = 'none';
                  }
                }
              };

              a.innerHTML = data[i].name;
              li.appendChild(a);
              profileSearcher.searchResult.appendChild(li);
            })();
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

      ajaxHandler.fetch(form, url, payload, messaging.actions);
    },
    conversation: function(conversationId, selector, page, size) {
        var url = '/message/loadMessages/' + conversationId + '?page=' + page + '&size=' + size;
        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversationId);
        ajaxHandler.fetch(null, url, {method: 'GET'}, messaging.actions);
    },
    loadConversations: function(append) {
      var conversationsContainer = document.querySelector('#conversations-container');
      var conversationsBox = conversationsContainer.querySelector('ul#conversationsBox');
      var spinner = conversationsContainer.querySelector('img.spinner');


      if (!append) {
        messaging.currentConversationsPage = messaging.defaultConversationsPage;
      } else {
        spinner.classList.remove('hidden');
      }

      var actions = {
        200: function(response) {
          response.json().then(function(data) {
            spinner.classList.add('hidden');

            if (!append) {
              conversationsBox.innerHTML = '';
            }

            var conversations = data.conversations

            for (var j = 0; j < conversations.length; j++) {
              var conversationRow = buildConversationOption(conversations[j]);
              conversationsBox.appendChild(conversationRow);
            }

            messaging.currentConversationsPage++;

            var loadMoreButton = conversationsContainer.querySelector('div#load-more-conversations');

            if (conversations.length == messaging.defaultConversationsSize) {
              loadMoreButton.classList.remove('hidden');
            } else {
              loadMoreButton.classList.add('hidden');
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

              if (conversation.type == 'CONFERENCE' && conversation.name) {
                b.innerHTML = conversation.name;
              } else {
                b.innerHTML = conversation.conversant;
              }

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

      var url = '/message/loadConversations?page=' + messaging.currentConversationsPage + '&size=' + messaging.defaultConversationsSize;
      ajaxHandler.fetch(null, url, {method: 'GET'}, actions);
    },
    scrollMessagesDown: function() {
        var scrollable = document.querySelector('div#messagesScroll');
        scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
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
      ajaxHandler.fetch(null, url, payload, messaging.actions);
    },
    displayNewMessagesCounter: function() {
      horizontalMenuCounter.displayCounter('span#new-conversations', '/message/getNewConversationsCount');
    }
}
