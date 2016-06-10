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
          <div>Last Active: ${user.lastActive}</div>
          <div>${profile.profileEmail}</div>
          <div>${profile.location}</div>
        </div>
      </g:if>
    </body>
</html>
