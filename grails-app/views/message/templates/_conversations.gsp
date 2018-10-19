<ul>
  <g:each var="conversation" in="${conversations}">
    <g:set var="lastMessage" value="${conversation.lastMessage}"/>
    <g:link controller="message" action="conversation" id="${conversation.id}">
      <li class="btn btn-default col-sm-12" style="${lastMessage.unread ? 'background-color: #E9F9FF;' : ''}">
        <b class="pull-left">${conversation.conversant}</b>
        <br>
        <i class="pull-left">${lastMessage.text}</i>
      </li>
    </g:link>
  </g:each>
</ul>
