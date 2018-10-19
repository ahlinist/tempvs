<ul>
  <g:each var="conversation" in="${conversations}">
    <g:set var="lastMessage" value="${conversation.lastMessage}"/>
    <li class="btn btn-default col-sm-12" style="${lastMessage.unread ? 'background-color: #E9F9FF;' : ''}"
        onclick="ajaxHandler.processAjaxRequest(document, '/message/loadMessages/${conversation.id}', null, 'GET', '#messagesBox', ajaxHandler.actions, true, true);">
      <b class="pull-left">${conversation.conversant}</b>
      <br>
      <i class="pull-left">${lastMessage.text}</i>
    </li>
  </g:each>
</ul>
