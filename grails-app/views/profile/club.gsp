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
      <g:set var="active" value="${profile.active}"/>
      <g:if test="${!active}">
        <div class="well">
          <div class="text-right">
            <b><g:message code="profile.inactive.message"/></b>&nbsp;
            <g:if test="${profile.user == user}">
              <tempvs:ajaxLink class="btn btn-default" controller="profile" action="activateProfile" id="${profile.id}" method="POST" classes="btn btn-default">
                <g:message code="profile.activate.button"/>
              </tempvs:ajaxLink>
            </g:if>
          </div>
        </div>
      </g:if>
      <div class="row">
        <div class="col-sm-3">
          <g:render template="/profile/templates/identity"/>
        </div>
        <div class="col-sm-5 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="firstName" value="${profile.firstName}" label="profile.firstName.label" editAllowed="${editAllowed && active}" mandatory="${true}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="lastName" value="${profile.lastName}" label="profile.lastName.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="nickName" value="${profile.nickName}" label="profile.nickName.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="profileId" value="${profile.profileId}" label="profile.profileId.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileEmail" name="profileEmail" value="${profile.profileEmail}" label="profile.profileEmail.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="location" value="${profile.location}" label="profile.location.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="text" action="editProfileField" name="clubName" value="${profile.clubName}" label="profile.clubName.label" editAllowed="${editAllowed && active}"/>
          <tempvs:ajaxSmartForm type="select" action="editProfileField" name="period" value="${profile.period?.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
        </div>
        <div class="col-sm-4">
          <b><g:message code="passport.list.label"/></b>
          <g:render template="/profile/templates/passportList"/>
          <g:if test="${editAllowed && active}">
            <div>
              <tempvs:modalButton id="createPassport" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm controller="passport" action="createPassport">
                  <tempvs:imageUploader fieldName="imageUploadBeans"/>
                  <tempvs:formField type="text" name="name" label="passport.name.label" mandatory="${true}"/>
                  <tempvs:formField type="text" name="description" label="passport.description.label"/>
                  <tempvs:ajaxSubmitButton value="passport.create.button"/>
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </div>
          </g:if>
        </div>
      </div>
    </g:if>
    <g:elseif test="${notFoundMessage}">
      <g:message code="${notFoundMessage}" args="${[id]}" />
    </g:elseif>
  </body>
</html>
