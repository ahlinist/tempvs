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
            <div><b><g:message code="clubProfile.profileEmail.label" />:</b> ${profile.profileEmail}</div>
            <div><b><g:message code="clubProfile.location.label" />:</b> ${profile.location}</div>
            <div><b><g:message code="clubProfile.clubName.label" />:</b> ${profile.clubName}</div>
            <div><b><g:message code="periodization.period.value.label"/>:</b> ${profile.period.value}</div>
          </div>
          <div class="col-sm-3">
            <g:render template="/profile/templates/listedUserProfile" model="${[userProfile: user.userProfile]}"/>
          </div>
          <div class="col-sm-3">
            <g:if test="${editAllowed}">
              <div class="row">
                <div><b><g:message code="actions.label"/></b>:</div>
                <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.delete.tooltip')}">
                  <tempvs:modalButton id="deleteProfile" size="modal-sm" classes="glyphicon glyphicon-trash">
                    <g:message code='profile.deleteConfirmation.text' args="${[profile]}"/>
                    <br/>
                    <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${profile.id}"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </tempvs:modalButton>
                </span>
                <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.updateProfile.tooltip')}">
                  <tempvs:modalButton id="updateProfile" classes="glyphicon glyphicon-pencil">
                    <tempvs:ajaxForm action="updateProfileEmail">
                      <tempvs:formField type="email" name="email" value="${profile.profileEmail}" label="clubProfile.profileEmail.label" />
                      <tempvs:ajaxSubmitButton value="clubEmail.update.button" />
                    </tempvs:ajaxForm>
                    <g:render template="/profile/templates/clubProfileForm"
                        model="${[action: 'updateClubProfile', button: 'clubProfile.update.button', profile: profile]}"/>
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
