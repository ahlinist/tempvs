<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
        <meta name="layout" content="main"/>
    </head>
    <body>
      <g:if test="${user}">
        <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
        <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>

        <div>
          <div id="fullName">${user.firstName} ${user.lastName}</div>
          <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${user.lastActive}" /></div>
          <div>${user.userProfile.profileEmail}</div>
          <div>${user.userProfile.location}</div>
        </div>
      </g:if>
      <g:elseif test="${message}">
        ${message}
      </g:elseif>
    </body>
</html>
