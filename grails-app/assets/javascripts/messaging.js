var messaging = {
  conversationId: null,
  defaultPageNumber: 0,
  defaultConversationsSize: 40,
  defaultMessagesSize: 40,
  currentConversationsPage: 0,
  currentMessagesPage: 0,
  newParticipantsCounter: 0,
  actions: {
    200: function(response) {
      response.json().then(function(data) {
        var conversation = data.conversation;
        var conversationDetails = document.querySelector('div#conversation-details');
        ajaxHandler.hideModals();
        messaging.currentMessagesPage = messaging.defaultPageNumber;
        messaging.conversationId = conversation.id;
        conversationDetails.classList.remove('hidden');

        //messages section
        var append = false;
        messaging.appendMessages(conversation.messages, conversationDetails, append);

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
        //TODO: implement clearing function to clean up forms and popups
        messaging.newParticipantsCounter = 0;
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
    //TODO: support multiple participants to be added
    200: function(response) {
      var resultContainer = document.querySelector('#add-participant-to-conversation-container');
      var resultList = resultContainer.querySelector('ul');
      var addParticipantForm = resultContainer.querySelector('form.add-participant-to-conversation-form');
      var addParticipantButton = addParticipantForm.querySelector('button');
      addParticipantForm.action = '/message/addParticipant/' + messaging.conversationId;
      addParticipantButton.classList.add('hidden');

      response.json().then(function(data) {
        profileSearcher.recoverUI();
        var profileSearchContainer = document.querySelector('#add-participant-profile-search');
        var profileSearchTemplate = profileSearchContainer.querySelector('template.profile-search-template');
        var profileSearchListItem = profileSearchTemplate.content.querySelector('li');

        data.forEach(function(dataEntry){
          var profileId = dataEntry.id;
          var profileName = dataEntry.name;

          if (document.querySelector('.active-conversation-participant[href$="/' + profileId + '"]')) {
            return;
          }

          var profileSearchNode = document.importNode(profileSearchListItem, true);
          var profileResultLink = profileSearchNode.querySelector('a.search-result');
          profileResultLink.innerHTML = profileName;

          profileResultLink.onclick = function() {
            var searchResultTemplate = profileSearchContainer.querySelector('template.profile-search-result-template');
            var searchResultListItem = searchResultTemplate.content.querySelector('li');
            var searchResultNode = document.importNode(searchResultListItem, true);
            var searchResultLink = searchResultNode.querySelector('a.search-result-link');
            var remove = searchResultNode.querySelector('button span.fa-remove');
            var input = searchResultNode.querySelector('input[type=hidden]');

            resultList.innerHTML = '';
            addParticipantButton.classList.remove('hidden');
            input.name = 'subject';
            input.value = profileId;
            searchResultLink.href = '/profile/show/' + profileId;
            searchResultLink.innerHTML = profileName;
            remove.onclick = function() {resultList.innerHTML = '';};
            resultList.appendChild(searchResultNode);
          };

          profileSearcher.searchResult.appendChild(profileSearchNode);
        });
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
  //TODO: unite with addParticipants logic
  createConversationActions: {
    200: function(response) {
      response.json().then(function(data) {
        profileSearcher.recoverUI();
        var createConversationPopup = document.querySelector('#create-conversation-popup');
        var conversationNameContainer = createConversationPopup.querySelector('#new-conversation-name-container');
        var participantsList = createConversationPopup.querySelector('ul#create-conversation-participants-container');
        var profileSearchTemplate = createConversationPopup.querySelector('template.profile-search-template');
        var profileSearchListItem = profileSearchTemplate.content.querySelector('li');

        data.forEach(function(dataEntry){
          var profileId = dataEntry.id;
          var profileName = dataEntry.name;

          if (participantsList.querySelector('input[name^="receivers"][value="' + profileId + '"]')) {
            return;
          }

          var profileSearchNode = document.importNode(profileSearchListItem, true);
          var profileResultLink = profileSearchNode.querySelector('a.search-result');
          profileResultLink.innerHTML = profileName;

          profileResultLink.onclick = function() {
            var participantsCounter = messaging.newParticipantsCounter;
            var searchResultTemplate = createConversationPopup.querySelector('template.profile-search-result-template');
            var searchResultListItem = searchResultTemplate.content.querySelector('li');
            var searchResultNode = document.importNode(searchResultListItem, true);
            var searchResultLink = searchResultNode.querySelector('a.search-result-link');
            var remove = searchResultNode.querySelector('button.remove-participant');
            var input = searchResultNode.querySelector('input[type=hidden]');

            searchResultLink.href = '/profile/show/' + profileId;
            searchResultLink.innerHTML = profileName;
            input.value = profileId;
            input.name = 'receivers[' + participantsCounter + ']';
            messaging.newParticipantsCounter++;
            toggleConversationNameContainer();

            remove.onclick = function() {
              participantsList.removeChild(searchResultNode);
              toggleConversationNameContainer();
            };

            function toggleConversationNameContainer() {
              if (participantsList.querySelectorAll('input[name^="receivers"]').length > 1) {
                conversationNameContainer.style.display = 'block';
              } else {
                conversationNameContainer.style.display = 'none';
              }
            }

            participantsList.appendChild(searchResultNode);
          };

          profileSearcher.searchResult.appendChild(profileSearchNode);
         });
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
  appendMessages: function(messages, conversationDetails, append) {
    var messagesContainer = conversationDetails.querySelector('div#messages-container');
    var messagesList = messagesContainer.querySelector('ul#messages-list');
    var messageTemplate = messagesContainer.querySelector('template#message-template');
    var messageForm = messagesContainer.querySelector('form#message-form');
    var firstListItem = messagesList.firstChild;
    var spinner = document.querySelector('img.load-more-messages-spinner');
    spinner.classList.add('hidden');

    if (!append) {
      messagesList.innerHTML = '';

      messageForm.onsubmit = function() {
        messaging.send(this);
        return false;
      }
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

      append ? messagesList.insertBefore(messageNode, firstListItem) : messagesList.appendChild(messageNode);
    });

    var loadMoreButton = messagesContainer.querySelector('div#load-more-messages');

    if (messages.length == messaging.defaultMessagesSize) {
      loadMoreButton.classList.remove('hidden');
    } else {
      loadMoreButton.classList.add('hidden');
    }
  },
  loadMessages: function() {
    var page = ++messaging.currentMessagesPage;
    var size = messaging.defaultMessagesSize;
    var url = '/message/loadMessages/' + messaging.conversationId + '?page=' + page + '&size=' + size;
    var append = true;
    var conversationDetails = document.querySelector('div#conversation-details');
    var messagesContainer = conversationDetails.querySelector('div#messages-container');
    var loadMoreButton = messagesContainer.querySelector('div#load-more-messages');
    loadMoreButton.classList.add('hidden');
    var spinner = document.querySelector('img.load-more-messages-spinner');
    spinner.classList.remove('hidden');

    var actions = {
      200: function(response) {
        response.json().then(function(data) {
          var conversation = data.conversation;
          messaging.appendMessages(conversation.messages, conversationDetails, append);
        });
      }
    }

    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);
  },
  loadConversations: function(append) {
    var conversationsContainer = document.querySelector('#conversations-container');
    var conversationsList = conversationsContainer.querySelector('ul#conversations-list');
    var spinner = conversationsContainer.querySelector('img.spinner');

    if (!append) {
      messaging.currentConversationsPage = messaging.defaultPageNumber;
    } else {
      spinner.classList.remove('hidden');
    }

    var actions = {
      200: function(response) {
        response.json().then(function(data) {
          var conversations = data.conversations
          var conversationTemplate = document.querySelector('template#conversation-template');

          spinner.classList.add('hidden');

          if (!append) {
            conversationsList.innerHTML = '';
          }

          conversations.forEach(function(conversation) {
            var lastMessage = conversation.lastMessage;
            var conversationListItem = conversationTemplate.content.querySelector('li');
            var conversationNode = document.importNode(conversationListItem, true);
            var conversationNameContainer = conversationNode.querySelector('b.conversation-name');
            var lastMessageContainer = conversationNode.querySelector('i.last-message');

            conversationNode.onclick = function() {
              messaging.conversation(conversation.id, messaging.defaultPageNumber, messaging.defaultConversationsSize);
            }

            if (lastMessage.unread) {
              conversationNode.style.backgroundColor = '#E9F9FF';
            }

            if (conversation.type == 'CONFERENCE' && conversation.name) {
              conversationNameContainer.innerHTML = conversation.name;
            } else {
              conversationNameContainer.innerHTML = conversation.conversant;
            }

            lastMessageContainer.innerHTML = lastMessage.author.name + ': ' + lastMessage.text;

            if (lastMessage.subject) {
              lastMessageContainer.innerHTML += ' ' + lastMessage.subject.name;
            }

            conversationsList.appendChild(conversationNode);
          });

          messaging.currentConversationsPage++;
          var loadMoreButton = conversationsContainer.querySelector('div#load-more-conversations');

          if (conversations.length == messaging.defaultConversationsSize) {
            loadMoreButton.classList.remove('hidden');
          } else {
            loadMoreButton.classList.add('hidden');
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
  addParticipant: function(form) {
    var data = new FormData(form);

    var payload = {
      method: 'POST',
      body: data
    };

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, messaging.actions);
  },
  removeParticipant: function(subjectId) {
    var url = '/message/removeParticipant/' + messaging.conversationId;
    var data = new FormData();
    data.append('subject', subjectId);

    var payload = {
      method: 'POST',
      body: data
    };

    ajaxHandler.blockUI();
    ajaxHandler.fetch(null, url, payload, messaging.actions);
  },
  displayNewMessagesCounter: function() {
    var counter = document.querySelector('span#new-conversations');
    var url = '/message/getNewConversationsCount';

    var actions = {
      200: function(response) {
        response.json().then(function(data) {
          var count = data.count;

          if (count > 0) {
            counter.classList.remove('hidden');
            counter.innerHTML = count;
          } else {
            counter.classList.add('hidden');
            counter.innerHTML = '';
          }
        });
      }
    };

    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);
  }
}
