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
      <div class="col-sm-3" style="border-right: 1px solid #AAA; height: calc(100vh - 75px); overflow: auto;">
        <div id="conversationsBox"></div>
      </div>
      <div class="col-sm-9">
        <g:render template="/message/templates/messages" model="${[conversation: conversation]}"/>
      </div>
    </div>
  </body>
</html>
