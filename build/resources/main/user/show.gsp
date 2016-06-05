<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
        <meta name="layout" content="main"/>
    </head>
    <body>
      <g:set var="profile" value="${user.userProfile}" />
      <g:if test="${user}">
        <div>
          <div>${user.firstName} ${user.lastName}</div>
          <div>${profile.profileEmail}</div>
        </div>
        <div>
          <g:if test="${(session.user?.id == id) || (session.user?.userProfile?.customId == id)}">
            <g:link action="editUserProfile"><g:message code="user.profile.edit.label" /></g:link>
          </g:if>
        </div>
      </g:if>
    </body>
</html>
