<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - <tempvs:fullName profile="${profile}"/></title>
      </g:if>
    </head>
    <body>
      <g:if test="${profile}">
        <tempvs:fullName profile="${profile}"/>
        <tempvs:avatar profile="${profile}"/>
        <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
        <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>

        <div>
          <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${profile.user.lastActive}"/></div>
          <div>${profile.profileEmail}</div>
          <div>${profile.location}</div>
        </div>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
