<%@ page import="java.time.ZoneId" %>
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
          <g:render template="/ajax/templates/ajaxSmartForm"
              model="${[type: 'text', action: 'updateEmail', name: 'email', value: user.email, label: 'user.edit.email.label', editAllowed: true, mandatory: true]}"/>
        </div>
        <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'user', action: 'updateTimeZone']}">
          <g:render template="/common/templates/formField"
              model="${[type: 'select', name: 'timeZone', label: 'user.edit.timeZone.label', value: user.timeZone,
                  from: ZoneId.getAvailableZoneIds().sort()]}"/>
          <g:render template="/ajax/templates/submitButton">
            <g:message code="user.edit.timeZone.button"/>
          </g:render>
        </g:render>
        <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'user', action: 'updatePassword']}">
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'currentPassword', label: 'user.edit.currentPassword.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'newPassword', label: 'user.edit.newPassword.label', mandatory: true]}"/>
          <g:render template="/common/templates/formField" model="${[type: 'password', name: 'repeatNewPassword', label: 'user.edit.repeatNewPassword.label', mandatory: true]}"/>
          <g:render template="/ajax/templates/submitButton">
            <g:message code="user.edit.password.button"/>
          </g:render>
        </g:render>
        <div class="col-sm-3"></div>
      </div>
    </div>
  </body>
</html>
