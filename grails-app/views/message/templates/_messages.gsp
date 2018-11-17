<div class="row" id="messagesBox">
  <div id="messagesScroll" class="col-sm-8" style="position: relative; height: calc(100vh - 75px); overflow: auto;">
    <g:if test="${conversation}">
      <div id="conversationIdHolder" style="display: none;" >${conversation.id}</div>
      <ul>
        <g:each var="message" in="${conversation.messages}">
          <li style="${message.system ? 'font-style: italic;' : ''}">
            <div style="margin: 5px; padding: 5px; ${message.unread ? 'background-color: #E9F9FF;' : ''}">
              <g:link controller='profile' action='show' id="${message.author.id}">
                <b>${message.author.name}</b>
              </g:link>:
              ${message.text}
              <g:if test="${message.subject}">
                <g:link controller='profile' action='show' id="${message.subject.id}">
                  <b>${message.subject.name}</b>
                </g:link>
              </g:if>
              <span class="pull-right">${message.createdDate}</span>
            </div>
          </li>
        </g:each>
      <ul>
      <form onsubmit="messaging.send(this, '${conversation.id}'); return false;">
        <input type="text" style="width: calc(100% - 45px);" name="message">
        <button class="btn btn-default">
          <span class="fa fa-paper-plane" aria-hidden="true"></span>
        </button>
      </form>
    </g:if>
  </div>
  <div class="col-sm-4">
    <g:if test="${conversation}">
      <g:if test="${conversation.type == 'CONFERENCE'}">
        <div class="row">
          <b><g:message code="conversation.name.label"/></b>:
          <g:form controller="message" action="updateConversationName" id="${conversation.id}" style="display: inline;">
            <span class="hovering text-wrapper">
              <span class="text-holder" style="line-height: 40px; padding-left: 15px;">${conversation.name}</span>
              <span onclick="ajaxHandler.activateSmartForm(this, messaging.actions);" class="glyphicon glyphicon-pencil"></span>
            </span>
            <span class="hidden input-wrapper">
              <g:field style="margin: 4px 0px;" type="text" name="conversationName" value="${conversation.name}" autocomplete="off"/>
            </span><img style="width: 15px; height: 15px;" class="spinner hidden" src="/assets/spinner-sm.gif">
          </g:form>
        </div>
      </g:if>
      <div class="row">
        <b><g:message code="message.participants.label"/>:</b>
        <ul>
          <g:each var="participant" in="${conversation.participants}">
            <g:set var="participantId" value="${participant.id}"/>
            <g:set var="participantName" value="${participant.name}"/>
            <g:if test="${participantId != currentProfile.id}">
              <li class="row">
                <g:link class="btn btn-default col-sm-10 active-conversation-participant" controller="profile" action="show" id="${participantId}">
                  ${participant.name}
                </g:link>
                  <g:if test="${currentProfile.id == conversation.admin?.id}">
                    <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'message.remove.participant.button')}">
                      <g:render template="/common/templates/modalButton"
                          model="${[elementId: 'removeParticipant' + participantId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                        <g:message code='message.remove.participant.text' args="${[participantName]}"/>
                        <br/>
                        <span onclick="messaging.updateParticipants(${participantId}, 'REMOVE');">
                          <span class="btn btn-default">
                            <g:message code="yes"/>
                          </span>
                        </span>
                        <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                      </g:render>
                    </span>
                  </g:if>
              </li>
            </g:if>
          </g:each>
        </ul>
      </div>
      <g:render template="/common/templates/modalButton"
          model="${[elementId: 'addParticipantProfileSearch', size: 'modal-sm', icon: 'glyphicon glyphicon-plus']}">
        <span class="dropdown" style="margin:10px 0px;">
          <input style="width: 524px;" placeholder="${g.message(code: 'profile.search.placeholder')}" type="text" class="profile-search-box" name="query"/>
          <button class="btn btn-default dropdown-toggle profile-search-button" onclick="profileSearcher.search(this, 0, messaging.addParticipantActions);">
            <span class="glyphicon glyphicon-search"></span>
          </button>
          <div class="dropdown-menu" style="width: 300px;">
            <ul class="profile-search-result">
            </ul>
            <button class="btn btn-secondary col-sm-12 load-more-button" onclick="profileSearcher.search(this, 10, messaging.addParticipantActions);">
              <i><g:message code="profile.search.loadMore.link"/></i>
            </button>
          </div>
        </span>
        <div id="add-participant-to-conversation-container" style="height: 50px;"></div>
      </g:render>
    </g:if>
  </div>
</div>
