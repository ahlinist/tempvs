<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
    </head>
    <body>
      <div class="col-sm-4" style="border-right: 1px solid #AAA; height: 100%;">
        <g:each var="conversation" in="${conversationsDto.conversations}">
          <div class="well well-sm">
            name: ${conversation.participants}
            message: ${conversation.lastMessage.text}
          </div>
        </g:each>
      </div>
      <div class="col-sm-8">
      </div>
    </body>
</html>
