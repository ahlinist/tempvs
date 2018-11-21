<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - <g:message code="message.title"/></title>
    <script>
      window.onload = function() {
        var appendConversations = true;
        messaging.loadConversations(appendConversations);
        messaging.conversation('${conversationId}', messaging.defaultPageNumber, messaging.defaultMessagesSize);
      };
    </script>
  </head>
  <body>
  <div class="container">
    <div class="col-sm-3" style="border-right: 1px solid #AAA; height: calc(100vh - 75px); overflow: auto;">
      <div class="row" style="margin:10px 0px;">
        <button class="btn btn-default col-sm-12" data-toggle="modal" data-target="#create-conversation-popup">
          <span class="pull-left">
            <g:message code="conversation.create.button"/>&nbsp;
          </span>
          <span class="pull-right">
            <span class="fa fa-plus"></span>
          </span>
        </button>
        <div id="create-conversation-popup" class="modal fade" role="dialog">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-body">
                <span class="dropdown" style="margin:10px 0px;">
                  <input style="width: 524px;" placeholder="${g.message(code: 'conversation.search.participants.placeholder')}" type="text" class="profile-search-box" name="query"/>
                  <button class="btn btn-default dropdown-toggle profile-search-button" onclick="profileSearcher.search(this, 0, messaging.createConversationActions); return false;">
                    <span class="glyphicon glyphicon-search"></span>
                  </button>
                  <div class="dropdown-menu" style="width: 300px;">
                    <ul class="profile-search-result"></ul>
                    <button class="btn btn-secondary col-sm-12 load-more-button" onclick="profileSearcher.search(this, 10, messaging.createConversationActions); return false;">
                      <i><g:message code="profile.search.loadMore.link"/></i>
                    </button>
                  </div>
                </span>
                <g:form controller="message" action="createConversation" onsubmit="messaging.createConversation(this); return false;">
                  <input style="height:0; width:0; padding:0; border:none; display: block;" name="receivers">
                  <span style="display: none;" id="participants-counter">0</span>
                  <ul id="create-conversation-participants-container" class="row"></ul>
                  <div id="new-conversation-name-container" class="row" style="display: none;">
                    <hr style="margin: 10px 0px;"/>
                    <input class="col-sm-12" placeholder="${g.message(code: 'conversation.name.placeholder')}" type="text" name="name">
                  </div>
                  <hr style="margin: 10px 0px;"/>
                  <textarea placeholder="${g.message(code: 'message.create.text')}" name="text" style="width: 100%; height: 100px;"></textarea>
                  <button class="btn btn-default submit-button">
                    <g:message code="message.send.message.button"/>
                  </button>
                </g:form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr/>
      <div id="conversations-container">
        <template id="conversation-template">
          <li class="btn btn-default col-sm-12">
            <b class="pull-left conversation-name"></b>
            <br>
            <i class="pull-left last-message"></i>
          </li>
        </template>
        <ul style="margin:10px 0px;" id="conversations-list" class="row"></ul>
        <div id="load-more-conversations" class="row hidden">
          <button class="btn btn-default center-block" onclick="messaging.loadConversations(true)">
            <g:message code="conversations.load.more.button"/>
          </button>
        </div>
        <div class="row">
          <img class="spinner load-more hidden center-block" src="/assets/spinner.gif">
        </div>
      </div>
    </div>
    <div class="col-sm-9">
      <div class="row hidden" id="conversation-details">
        <div id="messages-container" class="col-sm-8" style="position: relative; height: calc(100vh - 75px); overflow: auto;">
          <div class="row">
            <img class="load-more-messages-spinner hidden center-block" src="/assets/spinner.gif">
          </div>
          <template id="message-template">
            <li>
              <div style="margin: 5px; padding: 5px;">
                <a class="message-author"><b></b></a>:
                <span class="message-text"></span>
                <a class="message-subject"><b></b></a>
                <span class="pull-right message-created-date"></span>
              </div>
            </li>
          </template>
          <div id="load-more-messages" class="row hidden" style="margin: 10px 0 11px 0;">
            <button class="btn btn-default center-block" onclick="messaging.loadMessages();">
              <g:message code="messages.load.more.button"/>
            </button>
          </div>
          <ul id="messages-list"></ul>
          <form id="message-form">
            <input type="text" style="width: calc(100% - 45px);" name="message">
            <button class="btn btn-default">
              <span class="fa fa-paper-plane" aria-hidden="true"></span>
            </button>
          </form>
        </div>
        <div class="col-sm-4">
          <div class="row hidden conversation-name-container">
            <b><g:message code="conversation.name.label"/></b>:
            <form class="conversation-name-form" style="display: inline;">
              <span class="hovering text-wrapper">
                <span class="text-holder" style="line-height: 40px; padding-left: 15px;"></span>
                <span onclick="ajaxHandler.activateSmartForm(this, messaging.actions);" class="glyphicon glyphicon-pencil"></span>
              </span>
              <span class="hidden input-wrapper">
                <input style="margin: 4px 0px 3px 0px;" type="text" name="conversationName" value="" autocomplete="off">
              </span><img style="width: 15px; height: 15px;" class="spinner hidden" src="/assets/spinner-sm.gif">
            </form>
          </div>
          <div class="row participants-container">
            <b><g:message code="message.participants.label"/>:</b>
            <template class="participant-template">
              <li class="row">
                <a class="btn btn-default col-sm-10 active-conversation-participant"></a>
                <span class="hidden remove-participant-button">
                  <button class="btn btn-default glyphicon glyphicon-trash" data-toggle="modal" data-target="#removeParticipantModal-"></button>
                  <div class="modal fade" role="dialog">
                    <div class="modal-dialog modal-sm">
                      <div class="modal-content">
                        <div class="modal-body">
                          <g:message code='message.remove.participant.text'/>
                          <br/>
                          <button class="btn btn-default confirm-remove-participant-button"><g:message code="yes"/></button>
                          <button class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                        </div>
                      </div>
                    </div>
                  </div>
                </span>
              </li>
            </template>
            <ul id="participants-list"></ul>
          </div>
          <button class="btn btn-default glyphicon glyphicon-plus" data-toggle="modal" data-target="#add-participant-profile-search"></button>
          <div id="add-participant-profile-search" class="modal fade" role="dialog">
            <div class="modal-dialog modal-sm">
              <div class="modal-content">
                <div class="modal-body">
                  <span class="dropdown" style="margin:10px 0px;">
                    <input style="width: 524px;" placeholder="${g.message(code: 'profile.search.placeholder')}" type="text" class="profile-search-box" name="query"/>
                    <button class="btn btn-default dropdown-toggle profile-search-button" onclick="profileSearcher.search(this, 0, messaging.addParticipantActions);">
                      <span class="glyphicon glyphicon-search"></span>
                    </button>
                    <div class="dropdown-menu" style="width: 300px;">
                      <ul class="profile-search-result"></ul>
                      <button class="btn btn-secondary col-sm-12 load-more-button" onclick="profileSearcher.search(this, 10, messaging.addParticipantActions);">
                        <i><g:message code="profile.search.loadMore.link"/></i>
                      </button>
                    </div>
                  </span>
                  <div id="add-participant-to-conversation-container" style="height: 50px;"></div>
                </div>
              </div>
            </div>
          </div>
      </div>
    </div>
  </body>
</html>
