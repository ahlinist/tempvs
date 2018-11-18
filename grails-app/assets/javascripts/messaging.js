var messaging = {
    conversationId: null,
    defaultConversationsPage: 0,
    defaultConversationsSize: 40,
    currentConversationsPage: 0,
    actions: {
      200: function(response) {
        response.json().then(function(data) {
          var conversation = data.conversation;
          var conversationDetails = document.querySelector('div#conversation-details');
          ajaxHandler.hideModals();
          messaging.conversationId = conversation.id;
          conversationDetails.classList.remove('hidden');

          //messages section
          var messages = conversation.messages;
          var messagesContainer = conversationDetails.querySelector('div#messages-container');
          var messagesList = messagesContainer.querySelector('ul#messages-list');
          var messageTemplate = messagesContainer.querySelector('template#message-template');
          var messageForm = messagesContainer.querySelector('form#message-form');
          messagesList.innerHTML = '';

          messageForm.onsubmit = function() {
            messaging.send(this);
            return false;
          }

          messages.forEach(function(message) {
            var messageListItem = messageTemplate.content.querySelector('li');
            var messageNode = document.importNode(messageListItem, true);

            if (message.system) {
              messageNode.style.fontStyle = 'italic';
            }

            var authorLink = messageNode.querySelector('a.message-author');
            authorLink.setAttribute('href', '/profile/show/' + message.author.id);
            authorLink.querySelector('b').innerHTML = message.author.name;
            messageNode.querySelector('span.message-text').innerHTML = message.text;

            if (message.subject) {
              var subjectLink = messageNode.querySelector('a.message-subject');
              subjectLink.setAttribute('href', '/profile/show/' + message.subject.id);
              subjectLink.querySelector('b').innerHTML = message.subject.name;
            }

            if (message.unread) {
              messageNode.style.backgroundColor = '#E9F9FF';
            }

            messageNode.querySelector('span.message-created-date').innerHTML = message.createdDate;
            messagesList.appendChild(messageNode);
          });

          //conversation name section
          var conversationNameContainer = document.querySelector('div.conversation-name-container');
          conversationNameContainer.classList.add('hidden');

          if (conversation.type == 'CONFERENCE') {
            var conversationNameForm = conversationNameContainer.querySelector('form');
            var conversationNameSpinner = conversationNameContainer.querySelector('.spinner');
            conversationNameSpinner.classList.add('hidden');
            conversationNameContainer.classList.remove('hidden');
            conversationNameForm.querySelector('.text-holder').innerHTML = conversation.name;
            conversationNameForm.querySelector('input[name=conversationName]').value = conversation.name;
            conversationNameForm.action = '/message/updateConversationName/' + conversation.id;
          }

          //participants section
          var participants = conversation.participants;
          var currentProfile = data.currentProfile;
          var participantsContainer = conversationDetails.querySelector('.participants-container');
          var participantTemplate = conversationDetails.querySelector('template.participant-template');
          var participantsList = conversationDetails.querySelector('ul#participants-list');
          participantsList.innerHTML = '';

          participants.forEach(function(participant) {
            if (participant.id == currentProfile.id) {
              return;
            }

            var participantListItem = participantTemplate.content.querySelector('li');
            var participantNode = document.importNode(participantListItem, true);
            var profileLink = participantNode.querySelector('.active-conversation-participant');
            var removeButton = participantNode.querySelector('.remove-participant-button');
            profileLink.setAttribute('href', '/profile/show/' + participant.id);
            profileLink.innerHTML = participant.name;

            if ((conversation.type == 'CONFERENCE') && (conversation.admin.id == currentProfile.id) && (participants.length > 2)) {
              removeButton.classList.remove('hidden');
              removeButton.querySelector('[data-toggle=modal]').setAttribute('data-target', '#removeParticipantModal-' + participant.id);
              removeButton.querySelector('[role=dialog]').setAttribute('id', 'removeParticipantModal-' + participant.id);
              removeButton.querySelector('.confirm-remove-participant-button').onclick = function() {
                messaging.removeParticipant(participant.id);
              };
            }

            participantsList.appendChild(participantNode);
          });

          window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversation.id);
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
                ok.onclick = function() { messaging.addParticipant(profileId) };

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
          var conversationNameContainer = createConversationPopup.querySelector('#new-conversation-name-container');
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
    send: function(form) {
      var data = new FormData(form);

      if (!data.get('message')) {
        return;
      }

      form.querySelector('input[name=message]').value = '';
      var url = '/message/send/' + messaging.conversationId;

      var payload = {
        method: 'POST',
        body: data
      };

      ajaxHandler.fetch(form, url, payload, messaging.actions);
    },
    conversation: function(conversationId, page, size) {
      if (conversationId) {
        var url = '/message/loadMessages/' + conversationId + '?page=' + page + '&size=' + size;
        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversationId);
        ajaxHandler.fetch(null, url, {method: 'GET'}, messaging.actions);
      }
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
              li.onclick = function() {messaging.conversation(conversation.id, 0, 40);}

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
        var scrollable = document.querySelector('div#messages-container');
        scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
    },
    addParticipant: function(subjectId) {
      var url = '/message/addParticipant/' + messaging.conversationId;

      var payload = {
        method: 'POST',
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: 'subject=' + subjectId
      };

      ajaxHandler.blockUI();
      ajaxHandler.fetch(null, url, payload, messaging.actions);
    },
    removeParticipant: function(participantId) {
      var url = '/message/removeParticipant/' + messaging.conversationId;

      var payload = {
        method: 'POST',
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: '&subject=' + participantId
      };

      ajaxHandler.blockUI();
      ajaxHandler.fetch(null, url, payload, messaging.actions);
    },
    displayNewMessagesCounter: function() {
      horizontalMenuCounter.displayCounter('span#new-conversations', '/message/getNewConversationsCount');
    }
}
