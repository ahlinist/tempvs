<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
    </head>
    <body>
      <div class="col-sm-4" style="border-right: 1px solid #AAA; height: calc(100vh - 75px);">
        <ul>
          <g:each var="conversation" in="${conversations}">
            <g:set var="lastMessage" value="${conversation.lastMessage}"/>
            <g:link controller="message" action="conversation" id="${conversation.id}">
              <li class="btn btn-default col-sm-12" style="${lastMessage.unread ? 'background-color: #DFFFFF;' : ''}">
                <b class="pull-left">${conversation.conversant}</b>
                <br>
                <i class="pull-left">${lastMessage.text}</i>
              </li>
            </g:link>
          </g:each>
        </ul>
      </div>
      <div class="col-sm-8" style="position: absolute; bottom:37px; right:0px;">
        <ul>
          <g:each var="message" in="${conversation?.messages}">
            <li>
              <div style="margin: 5px; padding: 5px; ${message.unread ? 'background-color: #DFFFFF;' : ''}">
                <b>${message.author.name}</b>: ${message.text}
                <span class="pull-right">${message.createdDate}</span>
              </div>
            </li>
          </g:each>
        <ul>
        <input type="text" style="width: calc(100% - 45px);">
        <span class="btn btn-default">
          <span class="fa fa-paper-plane" aria-hidden="true"></span>
        </span>
      </div>
    </body>
</html>
