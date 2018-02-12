<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - ${profile}</title>
      </g:if>
    </head>
    <body>
      <g:if test="${profile}">
        <div class="row">
          <div class="col-sm-3">
            <g:render template="/profile/templates/identity"/>
          </div>
          <div class="col-sm-6 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="firstName" value="${profile.firstName}" label="profile.firstName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="lastName" value="${profile.lastName}" label="profile.lastName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="profileId" value="${profile.profileId}" label="profile.profileId.label"/>
            <tempvs:ajaxSmartForm type="email" action="editProfileEmail" name="profileEmail" value="${profile.profileEmail}" label="profile.profileEmail.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="location" value="${profile.location}" label="profile.location.label"/>
          </div>
        </div>
      </g:if>
      <g:elseif test="${notFoundMessage}">
        <g:message code="${notFoundMessage}" args="${[id]}" />
      </g:elseif>
    </body>
</html>
