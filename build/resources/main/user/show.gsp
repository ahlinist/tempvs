<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
    ${message}
    <g:if test="${user}">
      <div>
        <div>${user.email}</div>
      </div>
      <div>
        <g:if test="${session.user && (session.user.id == id)}">
          <g:link action="editUserProfile"><g:message code="user.profile.edit.label" /></g:link>
        </g:if>
      </div>
    </g:if>
    <g:if test="${session.user}">
      <div>
        <g:link action="logout"><g:message code="user.logout.button" /></g:link>
      </div>
    </g:if>
    </body>
</html>
