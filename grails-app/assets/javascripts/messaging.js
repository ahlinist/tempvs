var messaging = {
  conversationId: null,
  defaultPageNumber: 0,
  defaultConversationsSize: 40,
  defaultMessagesSize: 40,
  currentConversationsPage: 0,
  currentMessagesPage: 0,
  didScroll: false,
  actions: {
    200: function(response) {
      var currentProfileId = response.headers.get("Profile");

      response.json().then(function(conversation) {
        if (!window.location.href.includes('/message')) {
          window.location.href = "/message/conversation/" + conversation.id;
          return;
        }

        var conversationDetails = document.querySelector('div#conversation-details');
        messaging.clearForms();
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
          conversationNameForm.querySelector('input[name=name]').value = conversation.name;
          conversationNameForm.action = '/message/api/conversations/' + conversation.id + '/name';
        }

        //participants section
        var participants = conversation.participants;
        var participantsContainer = conversationDetails.querySelector('.participants-container');
        var participantTemplate = conversationDetails.querySelector('template.participant-template');
        var participantsList = conversationDetails.querySelector('ul#participants-list');
        participantsList.innerHTML = '';

        participants.forEach(function(participant) {
          if (participant.id == currentProfileId) {
            return;
          }

          var participantListItem = participantTemplate.content.querySelector('li');
          var participantNode = document.importNode(participantListItem, true);
          var profileLink = participantNode.querySelector('.active-conversation-participant');
          profileLink.setAttribute('href', '/profile/show/' + participant.id);
          profileLink.innerHTML = participant.name;

          if ((conversation.type == 'CONFERENCE') && (conversation.admin.id == currentProfileId) && (participants.length > 2)) {
            var removeForm = participantNode.querySelector('form.participant-deletion-form');
            removeForm.action = '/message/api/conversations/' + conversation.id + '/participants/' + participant.id;
            var removeButton = participantNode.querySelector('.remove-participant-button');
            removeButton.classList.remove('hidden');
            removeButton.querySelector('[data-toggle=modal]').setAttribute('data-target', '#removeParticipantModal-' + participant.id);
            removeButton.querySelector('[role=dialog]').setAttribute('id', 'removeParticipantModal-' + participant.id);
          }

          participantsList.appendChild(participantNode);
        });

        window.history.pushState("", "Tempvs - Message", '/message/conversation/' + conversation.id);
        messaging.markAsRead();
        messaging.scrollMessagesDown();
        messaging.loadConversations(false);
        messaging.displayNewMessagesCounter();
      });
    },
    400: function(response, form) {
      response.json().then(function (data) {
        var errors = data.errors;

        for (fieldName in errors) {
          var validationMessage = errors[fieldName];
          var field = form.querySelector('[name="' + fieldName + '"]')

          if (!field) {
              field = form.querySelector('.submit-button');
          }

          field.classList.add('popped-over');

          $(field).popover({
              placement: 'bottom',
              content: validationMessage,
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
  addParticipantsActions: {
    200: function(response) {
      var resultContainer = document.querySelector('#add-participant-to-conversation-container');
      var resultList = resultContainer.querySelector('ul');
      var addParticipantForm = resultContainer.querySelector('form.add-participant-to-conversation-form');
      var addParticipantButton = addParticipantForm.querySelector('button');
      addParticipantForm.action = '/message/api/conversations/' + messaging.conversationId + '/participants';

      response.json().then(function(data) {
        profileSearcher.recoverUI();
        var profileSearchContainer = document.querySelector('#add-participant-profile-search');
        var profileSearchTemplate = profileSearchContainer.querySelector('template.profile-search-template');
        var profileSearchListItem = profileSearchTemplate.content.querySelector('li');

        data.forEach(function(dataEntry){
          var profileId = dataEntry.id;
          var profileName = dataEntry.name;

          var isParticipant = document.querySelector('.active-conversation-participant[href$="/' + profileId + '"]');
          var isForAddition = profileSearchContainer.querySelector('.search-result-link[href$="/' + profileId + '"]');

          if (isParticipant || isForAddition) {
            return;
          }

          var profileSearchNode = document.importNode(profileSearchListItem, true);
          var profileResultLink = profileSearchNode.querySelector('a.search-result');
          profileResultLink.innerHTML = profileName;

          profileResultLink.onclick = function() {
            var searchResultTemplate = document.querySelector('template#profile-search-result-template');
            var searchResultListItem = searchResultTemplate.content.querySelector('li');
            var searchResultNode = document.importNode(searchResultListItem, true);
            var searchResultLink = searchResultNode.querySelector('a.search-result-link');
            var remove = searchResultNode.querySelector('span.remove-participant');
            var input = searchResultNode.querySelector('input[type=hidden]');

            input.value = profileId;
            searchResultLink.href = '/profile/show/' + profileId;
            searchResultLink.innerHTML = profileName;

            remove.onclick = function() {
              resultList.removeChild(searchResultNode);
              toggleAddButton();
            };

            resultList.appendChild(searchResultNode);
            toggleAddButton();
          };

          profileSearcher.searchResult.appendChild(profileSearchNode);
        });

        toggleAddButton();

        function toggleAddButton() {
          if (addParticipantForm.querySelectorAll('a.search-result-link').length > 0) {
            addParticipantButton.classList.remove('hidden');
          } else {
            addParticipantButton.classList.add('hidden');
          }
        }
      });
    }
  },
  createConversation: function(form) {
    var profileSearchField = document.querySelector('#create-conversation-popup input[name="query"]');
    var textarea = form.querySelector('textarea[name=text]');
    var blankMessageError = !textarea.value || !textarea.value.trim();
    var noParticipantsError = !form.querySelector('input[name=participants]');

    if (blankMessageError) {
      $(textarea).tooltip('show');
    }

    if (noParticipantsError) {
      $(profileSearchField).tooltip('show');
    }

    if (blankMessageError || noParticipantsError) {
      return;
    }

    ajaxHandler.blockUI();
    var formData = new FormData(form);

    var object = {
      name: formData.get('name'),
      text: formData.get('text'),
      receivers: formData.getAll('participants')
    };

    var payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(object)
    }

    ajaxHandler.fetch(form, form.action, payload, messaging.actions);
  },
  createConversationActions: {
    200: function(response) {
      response.json().then(function(data) {
        profileSearcher.recoverUI();
        var createConversationWrapper = document.querySelector('#conversation-popup-wrapper');
        var participantsList = createConversationWrapper.querySelector('ul.create-conversation-participants-container');
        var profileSearchTemplate = createConversationWrapper.querySelector('template.profile-search-template');
        var profileSearchListItem = profileSearchTemplate.content.querySelector('li');

        data.forEach(function(dataEntry){
          var profileId = dataEntry.id;
          var profileName = dataEntry.name;

          if (participantsList.querySelector('input[name=participants][value="' + profileId + '"]')) {
            return;
          }

          var profileSearchNode = document.importNode(profileSearchListItem, true);
          var profileResultLink = profileSearchNode.querySelector('a.search-result');
          profileResultLink.innerHTML = profileName;

          profileResultLink.onclick = function() {
            var searchResultTemplate = document.querySelector('template#profile-search-result-template');
            var searchResultListItem = searchResultTemplate.content.querySelector('li');
            var searchResultNode = document.importNode(searchResultListItem, true);
            var searchResultLink = searchResultNode.querySelector('a.search-result-link');
            var remove = searchResultNode.querySelector('span.remove-participant');
            var input = searchResultNode.querySelector('input[type=hidden]');

            searchResultLink.href = '/profile/show/' + profileId;
            searchResultLink.innerHTML = profileName;
            input.value = profileId;

            remove.onclick = function() {
              participantsList.removeChild(searchResultNode);
              toggleConversationNameContainer();
            };

            participantsList.appendChild(searchResultNode);
            toggleConversationNameContainer();
          };

          profileSearcher.searchResult.appendChild(profileSearchNode);

          function toggleConversationNameContainer() {
            var conversationNameContainer = createConversationWrapper.querySelector('div.new-conversation-name-container');

            if (participantsList.querySelectorAll('input[name=participants]').length > 1) {
              conversationNameContainer.style.display = 'block';
            } else {
              conversationNameContainer.style.display = 'none';
            }
          }
        });
      });
    }
  },
  send: function(form) {
    var formData = new FormData(form);
    var message = formData.get('message');

    if (!message) {
      var inputBox = form.querySelector('input[name=message]');
      $(inputBox).tooltip('show');
      return;
    }

    form.querySelector('input[name=message]').value = '';
    var url = '/message/api/conversations/' + messaging.conversationId + '/messages';

    var payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({text: message})
    };

    ajaxHandler.fetch(form, url, payload, messaging.actions);
  },
  conversation: function(conversationId, page, size) {
    if (conversationId) {
      var url = '/message/api/conversations/' + conversationId + '?page=' + page + '&size=' + size;
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
        messageNode.classList.add('message-unread');
      }

      messageNode.querySelector('span.message-created-date').innerHTML = message.createdDate;
      messageNode.setAttribute('message-id', message.id);

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
    var url = '/message/api/conversations/' + messaging.conversationId + '?page=' + page + '&size=' + size;
    var conversationDetails = document.querySelector('div#conversation-details');
    var messagesContainer = conversationDetails.querySelector('div#messages-container');
    var loadMoreButton = messagesContainer.querySelector('div#load-more-messages');
    loadMoreButton.classList.add('hidden');
    var spinner = document.querySelector('img.load-more-messages-spinner');
    spinner.classList.remove('hidden');

    var actions = {
      200: function(response) {
        response.json().then(function(conversation) {
          var append = true;
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
            var unreadCounter = conversationNode.querySelector('.badge-notify');

            unreadCounter.innerHTML = conversation.unreadMessagesCount || "";

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

    var url = '/message/api/conversations?page=' + messaging.currentConversationsPage + '&size=' + messaging.defaultConversationsSize;
    ajaxHandler.fetch(null, url, {method: 'GET'}, actions);
  },
  scrollMessagesDown: function() {
    var scrollable = document.querySelector('div#messages-container');
    scrollable.scrollTop = scrollable.scrollHeight - scrollable.clientHeight;
  },
  removeParticipant: function(form) {
    var payload = {
      method: 'DELETE',
    };

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, messaging.actions);
  },
  addParticipants: function(form) {
    var formData = new FormData(form);

    var object = {
      participants: formData.getAll('participants')
    };

    var payload = {
      method: 'POST',
      headers:{
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(object)
    };

    ajaxHandler.blockUI();
    ajaxHandler.fetch(form, form.action, payload, messaging.actions);
  },
  displayNewMessagesCounter: function() {
    var counter = document.querySelector('span#new-conversations');
    var url = '/message/api/conversations';

    var actions = {
      200: function(response) {
        var count = response.headers.get("X-Total-Count");

        if (count > 0) {
          counter.classList.remove('hidden');
          counter.innerHTML = count;
        } else {
          counter.classList.add('hidden');
          counter.innerHTML = '';
        }
      }
    };

    ajaxHandler.fetch(null, url, {method: 'HEAD'}, actions);
  },
  markAsRead: function() {
    if (messaging.didScroll) {
      return;
    }

    messaging.didScroll = true;
    var conversationId = messaging.conversationId;

    if (!conversationId) {
      messaging.didScroll = false;
      return;
    }

    var messagesList = document.querySelector('ul#messages-list');
    var unreadMessages = messagesList.getElementsByClassName('message-unread');
    var messagesToMarkAsRead = [];

    setTimeout(function() {
      for (var i = 0; i < unreadMessages.length; i++) {
        var message = unreadMessages[i];
        
        if (isMessageVisible(message)) {
          messagesToMarkAsRead.push(message);
        }
      }

      if (!messagesToMarkAsRead.length) {
        messaging.didScroll = false;
        return;
      }

      var url = '/message/api/conversations/' + conversationId + '/read';
      var data = {'messages': messagesToMarkAsRead.map(getMessageId)};

      var payload = {
        method: 'POST',
        headers:{
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      };

      var actions = {
        200: function(response) {
          if (response.ok) {
            setTimeout(function() {
              var appendConversations = false;
              messaging.loadConversations(appendConversations);
              messaging.displayNewMessagesCounter();
              [].slice.call(messagesToMarkAsRead).forEach(dropUnreadState)
            }, 1000);
          }
        }
      };

      messaging.didScroll = false;
      ajaxHandler.fetch(null, url, payload, actions);
    }, 1000);

    function getMessageId(message) {
      return message.getAttribute('message-id');
    }

    function dropUnreadState(message) {
      message.classList.remove('message-unread');
    }

    function isMessageVisible(message) {
      var top = message.getBoundingClientRect().top

      if ((top > 40) && (top < window.innerHeight - 40)) {
        return true;
      }

      return false;
    }
  },
  clearForms: function() {
    var conversationPopupWrapper = document.querySelector('div#conversation-popup-wrapper');
    var conversationPopupTemplate = document.querySelector('template#conversation-popup-template');
    var conversationPopupContent = conversationPopupTemplate.content.querySelector('div');
    var conversationPopupNode = document.importNode(conversationPopupContent, true);
    conversationPopupWrapper.innerHTML = '';
    conversationPopupWrapper.appendChild(conversationPopupNode);

    var addParticipantWrapper = document.querySelector('div#add-participant-wrapper');
    var addParticipantTemplate = document.querySelector('template#add-participant-template');
    var addParticipantContent = addParticipantTemplate.content.querySelector('div');
    var addParticipantNode = document.importNode(addParticipantContent, true);
    addParticipantWrapper.innerHTML = '';
    addParticipantWrapper.appendChild(addParticipantNode);
  }
}
