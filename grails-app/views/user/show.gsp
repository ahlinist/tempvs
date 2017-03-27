<!DOCTYPE html>
<g:set var="profile" value="${user?.userProfile}" />
<html>
    <head>
      <meta name="layout" content="main"/>
        <g:if test="${profile}">
          <title>Tempvs - ${profile.firstName} ${profile.lastName}</title>
        </g:if>
    </head>
    <body>
      <g:if test="${user}">
        <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
        <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>

        <div>
          <div>${profile.firstName} ${profile.lastName}</div>
          <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${user.lastActive}"/></div>
          <div>${profile.profileEmail}</div>
          <div>${profile.location}</div>
        </div>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
