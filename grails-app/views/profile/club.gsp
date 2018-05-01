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
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'profile', action: 'activateProfile', id: profile.id, method: 'POST', classes:'btn btn-default']}">
                <g:message code="profile.activate.button"/>
              </g:render>
            </g:if>
          </div>
        </div>
      </g:if>
      <div class="row">
        <div class="col-sm-3">
          <g:render template="/profile/templates/identity"/>
        </div>
        <div class="col-sm-5 ajax-form">
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'firstName', value: profile.firstName, label: 'profile.firstName.label', mandatory: true]}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'lastName', value: profile.lastName, label: 'profile.lastName.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'nickName', value: profile.nickName, label: 'profile.nickName.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'profileId', value: profile.profileId, label: 'profile.profileId.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'profileEmail', value: profile.profileEmail, label: 'profile.profileEmail.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'location', value: profile.location, label: 'profile.location.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'editProfileField', name: 'clubName', value: profile.clubName, label: 'profile.clubName.label']}"/>
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', name: 'period', value: profile.period?.value, label: 'periodization.period.dropdown.label', editAllowed: false]}"/>
        </div>
        <div class="col-sm-4">
          <b><g:message code="passport.list.label"/></b>
          <g:render template="/profile/templates/passportList"/>
          <g:if test="${editAllowed && active}">
            <div>
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'createPassport', icon: 'glyphicon glyphicon-plus']}">
                <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'passport', action: 'createPassport']}">
                  <g:render template="/image/templates/imageUploader"/>
                  <g:render template="/common/templates/formField" model="${[type: 'text', name: 'name', label: 'passport.name.label', mandatory: true]}"/>
                  <g:render template="/common/templates/formField" model="${[type: 'text', name: 'description', label: 'passport.description.label']}"/>
                  <g:render template="/ajax/templates/submitButton">
                    <g:message code="passport.create.button"/>
                  </g:render>
                </g:render>
              </g:render>
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
