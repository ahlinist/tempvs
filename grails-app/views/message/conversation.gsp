<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="message.title"/></title>
      <script>
        function loadConversations() {
            var element = $('div#conversationsBox');
            var url = '/message/loadConversations';
            ajaxHandler.processAjaxRequest(document, url, null, 'GET', '#conversationsBox', ajaxHandler.actions, true, true);
        }

        window.onload = loadConversations;
      </script>
    </head>
    <body>
      <div class="col-sm-4" style="border-right: 1px solid #AAA; height: calc(100vh - 75px);">
        <div id="conversationsBox"></div>
      </div>
      <div class="col-sm-8" style="position: absolute; bottom:37px; right:0px;">
        <div id="messagesBox">
          <g:if test="${conversation}">
            <g:render template="/message/templates/messages" model="${[conversation: conversation]}"/>
          </g:if>
        </div>
      </div>
    </body>
</html>
