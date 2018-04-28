<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Edit ${user.email}</title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-3"></div>
      <div class="col-sm-6">
        <div class="ajax-form">
          <tempvs:ajaxSmartForm type="text" action="updateEmail" name="email" value="${user.email}" label="user.edit.email.label" editAllowed="${true}" mandatory="${true}"/>
        </div>
        <tempvs:ajaxForm action="updatePassword">
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'currentPassword', label: 'user.edit.currentPassword.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'newPassword', label: 'user.edit.newPassword.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'repeatNewPassword', label: 'user.edit.repeatNewPassword.label', mandatory: true]}"/>
          <g:render template="/ajax/templates/submitButton">
            <g:message code="user.edit.password.button"/>
          </g:render>
        </tempvs:ajaxForm>
        <div class="col-sm-3"></div>
      </div>
    </div>
  </body>
</html>
