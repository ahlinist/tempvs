<div class="row" id="messagesBox">
  <div id="messagesScroll" class="col-sm-8" style="position: relative; height: calc(100vh - 75px); overflow: auto;">
    <g:if test="${conversation}">
      <ul>
        <g:each var="message" in="${conversation.messages}">
          <li>
            <div style="margin: 5px; padding: 5px; ${message.unread ? 'background-color: #E9F9FF;' : ''}">
              <g:link controller='profile' action='show' id="${message.author.id}">
              <b>${message.author.name}</b>:
              </g:link>
              ${message.text}
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
        <g:if test="${participant.id != currentProfile.id}">
          <li>
            <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${participant.id}">
              ${participant.name}
            </g:link>
          </li>
        </g:if>
      </g:each>
    <ul>
    </g:if>
  </div>
</div>
