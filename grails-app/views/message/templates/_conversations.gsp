<ul>
  <g:each var="conversation" in="${conversations}">
    <g:set var="lastMessage" value="${conversation.lastMessage}"/>
    <li class="btn btn-default col-sm-12" style="${lastMessage.unread ? 'background-color: #E9F9FF;' : ''}"
        onclick="messaging.conversation('${conversation.id}', '#messagesBox');">
      <b class="pull-left">${conversation.conversant}</b>
      <br>
      <i class="pull-left">${lastMessage.text}</i>
    </li>
  </g:each>
</ul>
