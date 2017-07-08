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
        <g:set var="user" value="${profile.user}"/>
        <div class="row">
          <div class="col-sm-3">
            <tempvs:fullName profile="${profile}"/>
            <tempvs:image image="${profile.avatar}"/>
          </div>
          <div class="col-sm-3">
            <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${user.lastActive}"/></div>
            <div><g:message code="userProfile.profileEmail.label" />: ${profile.profileEmail}</div>
            <div><g:message code="userProfile.location.label" />: ${profile.location}</div>
          </div>
          <div class="col-sm-3">
            <g:render template="/profile/templates/clubProfiles" model="${[user: profile.user]}"/>
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
