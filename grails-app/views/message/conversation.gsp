<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
      <script>
        window.onload = function() {
            messaging.loadConversations();
            messaging.scrollMessagesDown();
        };
      </script>
    </head>
    <body>
    <div class="container">
      <div class="col-sm-4" style="border-right: 1px solid #AAA; height: calc(100vh - 75px); overflow: auto;">
        <div id="conversationsBox"></div>
      </div>
      <div id="messagesScroll" class="col-sm-8" style="position: relative; height: calc(100vh - 75px); overflow: auto;">
        <div id="messagesBox">
          <g:if test="${conversation}">
            <g:render template="/message/templates/messages" model="${[conversation: conversation]}"/>
          </g:if>
        </div>
      </div>
    </div>
    </body>
</html>
