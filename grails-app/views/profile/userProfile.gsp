<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - ${profile}</title>
      </g:if>
    </head>
    <body>
      <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
      <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>
      <g:if test="${profile}">
        <g:set var="user" value="${profile.user}"/>
        <div class="row">
          <div class="col-sm-4">
            ${profile}
            <tempvs:image image="${profile.avatar}"/>
          </div>
          <div class="col-sm-4">
            <div><b><g:message code="date.lastActive" /></b> <tempvs:dateFromNow date="${user.lastActive}"/></div>
            <div><b><g:message code="userProfile.profileEmail.label" /></b>: ${profile.profileEmail}</div>
            <div><b><g:message code="userProfile.location.label" /></b>: ${profile.location}</div>
          </div>
          <div class="col-sm-4">
            <g:render template="/profile/templates/listedClubProfiles" model="${[clubProfiles: user.clubProfiles]}"/>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <div class="row">
            <tempvs:modalButton id="updateProfile" message="profile.updateProfile.link">
              <g:render template="/profile/templates/editUserProfile" model="${[profile: profile]}"/>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
