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
        <g:set var="avatar" value="${profile.avatar}"/>
        <div class="row">
          <div class="col-sm-3">
            <g:render template="/profile/templates/identity" args="${[avatar, editAllowed]}"/>
          </div>
          <div class="col-sm-3">
            <div><b><g:message code="date.lastActive" /></b> <tempvs:dateFromNow date="${user.lastActive}"/></div>
            <div><b><g:message code="userProfile.profileEmail.label" /></b>: ${profile.profileEmail}</div>
            <div><b><g:message code="userProfile.location.label" /></b>: ${profile.location}</div>
          </div>
          <div class="col-sm-3">
            <g:render template="/profile/templates/listedClubProfiles" model="${[clubProfiles: user.clubProfiles]}"/>
          </div>
          <div class="col-sm-3">
            <g:if test="${editAllowed}">
              <div class="row">
                <div><b><g:message code="actions.label"/></b>:</div>
                <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.updateProfile.tooltip')}">
                  <tempvs:modalButton id="updateProfile" classes="glyphicon glyphicon-pencil">
                    <g:render template="/profile/templates/userProfileForm" model="${[profile: profile]}"/>
                  </tempvs:modalButton>
                </span>
              </div>
            </g:if>
          </div>
        </div>
      </g:if>
      <g:elseif test="${notFoundMessage}">
        <g:message code="${notFoundMessage}" args="${id}" />
      </g:elseif>
    </body>
</html>
