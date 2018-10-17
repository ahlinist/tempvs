<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
    </head>
    <body>
      <div class="col-sm-4" style="border-right: 1px solid #AAA; height: 100%;">
        <ul>
          <g:each var="conversation" in="${conversationsDto.conversations}">
            <g:set var="lastMessage" value="${conversation.lastMessage}"/>
            <g:link controller="message" action="conversation" id="${conversation.id}">
              <li class="btn btn-default col-sm-12" style="${lastMessage.unread ? 'background-color: #E0FFFF;' : ''}">
                <b class="pull-left">${conversation.conversant}</b>
                <br>
                <i class="pull-left">${lastMessage.text}</i>
              </li>
            </g:link>
          </g:each>
        </ul>
      </div>
      <div class="col-sm-8">
      </div>
    </body>
</html>
