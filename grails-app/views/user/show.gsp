<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
        <meta name="layout" content="main"/>
    </head>
    <body>
    ${message}
    <g:if test="${user}">
      <div>
        <div>${user.email}</div>
      </div>
      <div>
        <g:if test="${(session.user?.id == id) || (session.user?.userProfile?.customId == id)}">
          <g:link action="editUserProfile"><g:message code="user.profile.edit.label" /></g:link>
        </g:if>
      </div>
    </g:if>
    <g:if test="${session.user}">
      <div>

      </div>
    </g:if>
    </body>
</html>
