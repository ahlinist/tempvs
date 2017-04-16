<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - <tempvs:fullName profile="${profile}"/></title>
      </g:if>
    </head>
    <body>
      <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
      <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>

      <g:if test="${profile}">
        <div class="col-sm-3">
          <tempvs:fullName profile="${profile}"/>
          <tempvs:avatar profile="${profile}"/>
        </div>
        <div class="col-sm-3">
          <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${profile.user.lastActive}"/></div>
          <div><g:message code="clubProfile.profileEmail.label" />: ${profile.profileEmail}</div>
          <div><g:message code="clubProfile.location.label" />: ${profile.location}</div>
          <div><g:message code="clubProfile.clubName.label" />: ${profile.clubName}</div>
        </div>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
