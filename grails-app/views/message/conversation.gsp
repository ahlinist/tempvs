<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
      <script>
        window.onload = function() {
            messaging.loadConversations();
            messaging.scrollMessagesDown();
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
                    <span style="display: none;" id="participants-counter">0</span>
                    <ul id="create-conversation-participants-container" class="row"></ul>
                    <div id="conversation-name-container" class="row" style="display: none;">
                      <hr style="margin: 10px 0px;"/>
                      <input class="col-sm-12" placeholder="${g.message(code: 'conversation.name.placeholder')}" type="text" name="name">
                    </div>
                    <hr style="margin: 10px 0px;"/>
                    <textarea placeholder="${g.message(code: 'message.create.text')}" name="text" style="width: 100%; height: 100px;"></textarea>
                    <button class="btn btn-default">
                      <g:message code="message.send.message.button"/>
                    </button>
                  </g:form>
                </div>
              </div>
            </div>
          </div>
        </div>
        <hr/>
        <ul style="margin:10px 0px;" id="conversationsBox"></ul>
      </div>
      <div class="col-sm-9">
        <g:render template="/message/templates/messages" model="${[conversation: conversation]}"/>
      </div>
    </div>
  </body>
</html>
