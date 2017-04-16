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

          <div>
            <div>
              <g:message code="date.lastActive" /> <tempvs:dateFromNow date="${profile.user.lastActive}"/>
            </div>
            <div>${profile.profileEmail}</div>
            <div>${profile.location}</div>
          </div>
        </div>
        <div class="col-sm-3">
          <div>
            <label>
              <g:message code="clubProfile.list.message"/>
            </label>
            <ul class="list-group">
              <g:each var="clubProfile" in="${profile.user.clubProfiles}">
                <li>
                  <g:link class="list-group-item" controller="profile" action="clubProfile" id="${clubProfile.id}">
                    <tempvs:fullName profile="${clubProfile}"/>
                  </g:link>
                </li>
              </g:each>
            </ul>
          </div>
        </div>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
