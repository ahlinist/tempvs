<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Register user</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <g:message code="user.confirm.registration.message" args="${[user.email]}"/>
        <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'profile', action: 'createProfile']}">
          <g:render template="/common/templates/formField" model="${[type: 'text', name: 'firstName', label: 'profile.firstName.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'text', name: 'lastName', label: 'profile.lastName.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'text', name: 'profileId', label: 'profile.profileId.label']}"/>
          <g:render template="/common/templates/formField" model="${[type: 'email', name: 'profileEmail', label: 'profile.profileEmail.label']}"/>
          <input type="hidden" name="type" value="USER"/>
          <g:render template="/ajax/templates/submitButton">
            <g:message code="user.confirm.registration.button"/>
          </g:render>
        </g:render>
        <g:message code="user.confirm.registration.instructions"/>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
