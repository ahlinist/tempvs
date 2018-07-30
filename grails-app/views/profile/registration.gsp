<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="location" content="/${controllerName}/${actionName}" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <title>Tempvs - Creating user profile</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <g:message code="user.confirm.registration.message" args="${[user.email]}"/>
        <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'profile', action: 'createProfile']}">
          <g:render template="/common/templates/formField" model="${[type: 'file', name: 'imageUploadBean.image', label: 'profile.avatar.label']}"/>
          <g:render template="/common/templates/formField" model="${[type: 'text', name: 'imageUploadBean.imageInfo', label: 'profile.avatarInfo.label']}"/>
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
