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
  <input type="text" style="width: calc(100% - 45px);">
  <span class="btn btn-default">
    <span class="fa fa-paper-plane" aria-hidden="true"></span>
  </span>
</g:if>
