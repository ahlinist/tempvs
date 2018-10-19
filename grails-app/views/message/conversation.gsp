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
        <div id="conversationsBox">
        </div>
      </div>
      <div class="col-sm-8" style="position: absolute; bottom:37px; right:0px;">
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
      </div>
    </body>
</html>
