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
            <div><b><g:message code="clubProfile.profileEmail.label" />:</b> ${profile.profileEmail}</div>
            <div><b><g:message code="clubProfile.location.label" />:</b> ${profile.location}</div>
            <div><b><g:message code="clubProfile.clubName.label" />:</b> ${profile.clubName}</div>
            <div><b><g:message code="periodization.period.value.label"/>:</b> ${profile.period.value}</div>
          </div>
          <div class="col-sm-4">
            <g:render template="/profile/templates/listedUserProfile" model="${[userProfile: user.userProfile]}"/>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <div class="row">
            <tempvs:modalButton id="updateProfile" message="profile.updateProfile.link">
              <tempvs:ajaxForm action="updateProfileEmail">
                <tempvs:formField type="email" name="email" value="${profile.profileEmail}" label="clubProfile.profileEmail.label" />
                <tempvs:ajaxSubmitButton value="clubEmail.update.button" />
              </tempvs:ajaxForm>
              <g:render template="/profile/templates/clubProfileForm"
                  model="${[action: 'updateClubProfile', button: 'clubProfile.update.button', profile: profile]}"/>
            </tempvs:modalButton>
            <tempvs:modalButton id="deleteProfile" size="modal-sm" message="profile.delete.button">
              <g:message code='profile.deleteConfirmation.text' args="${[profile]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${profile.id}"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
