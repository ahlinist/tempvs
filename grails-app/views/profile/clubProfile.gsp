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
        <div class="row">
          <div class="col-sm-3">
            <g:render template="/profile/templates/identity"/>
            <div><b><g:message code="date.lastActive" /></b> <tempvs:dateFromNow date="${user.lastActive}"/></div>
            <g:render template="/profile/templates/listedUserProfile"/>
          </div>
          <div class="col-sm-6 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="firstName" value="${profile.firstName}" label="profile.firstName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="lastName" value="${profile.lastName}" label="profile.lastName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="nickName" value="${profile.nickName}" label="profile.nickName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="profileId" value="${profile.profileId}" label="profile.profileId.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileEmail" name="profileEmail" value="${profile.profileEmail}" label="profile.profileEmail.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="location" value="${profile.location}" label="profile.location.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="clubName" value="${profile.clubName}" label="profile.clubName.label"/>
            <tempvs:ajaxSmartForm type="select" action="editProfileField" name="period" value="${profile.period?.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
          <div class="col-sm-3">
            <g:if test="${editAllowed}">
              <div class="row">
                <div><b><g:message code="actions.label"/></b>:</div>
                <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.delete.tooltip')}">
                  <tempvs:modalButton id="deleteProfile" size="modal-sm" classes="glyphicon glyphicon-trash">
                    <g:message code='profile.deleteConfirmation.text' args="${[profile]}"/>
                    <br/>
                    <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${profile.id}" method="DELETE"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
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
