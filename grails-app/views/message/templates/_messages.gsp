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
      <form onsubmit="messaging.send(this, '#messagesBox', '${conversation.id}'); return false;">
        <input type="text" style="width: calc(100% - 45px);" name="message">
        <button class="btn btn-default">
          <span class="fa fa-paper-plane" aria-hidden="true"></span>
        </button>
      </form>
    </g:if>
  </div>
  <div class="col-sm-4">
    <g:if test="${conversation}">
      <b><g:message code="message.participants.label"/>:</b>
      <ul style="margin: 10px;">
        <g:each var="participant" in="${conversation.participants}">
          <g:set var="participantId" value="${participant.id}"/>
          <g:set var="participantName" value="${participant.name}"/>
          <g:if test="${participantId != currentProfile.id}">
            <li>
              <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${participantId}">
                ${participant.name}
              </g:link>
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
            </li>
          </g:if>
        </g:each>
      <ul>
      <g:render template="/common/templates/modalButton"
          model="${[elementId: 'addParticipantProfileSearch', size: 'modal-sm', icon: 'glyphicon glyphicon-plus']}">
        <span class="dropdown" style="margin:10px;">
          <input style="width: 300px;" placeholder="${g.message(code: 'profile.search.placeholder')}" type="text" class="profile-search-box" name="query"/>
          <button class="btn btn-default dropdown-toggle profile-search-button" onclick="profileSearcher.search(this, 0, messaging.actions);">
            <span class="glyphicon glyphicon-search"></span>
          </button>
          <div class="dropdown-menu" style="width: 300px;">
            <ul class="profile-search-result">
            </ul>
            <button class="btn btn-secondary col-sm-12 load-more-button" onclick="profileSearcher.search(this, 10, messaging.actions);">
              <i><g:message code="profile.search.loadMore.link"/></i>
            </button>
          </div>
        </span>
        <div id="profileToAdd" style="height: 50px;">
        </div>
      </g:render>
    </g:if>
  </div>
</div>
