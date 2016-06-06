<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
    <title>title</title>
    <style>
      form {
        margin:10px;
        padding:10px;
        border: 4px #eee groove;
      }

      input[type=submit] {
        margin-top:10px;
      }
    </style>
    </head>
    <body>
      <div>
        <g:form action="updateEmail">
          <div>
            <g:field type="email" name="email" placeholder="${message(code:'user.edit.email.placeholder')}" value="${user?.email}" />
          </div>
          <g:if test="${flash.emailError}">
            <div class="alert alert-danger text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.emailError}" />
            </div>
          </g:if>
          <g:if test="${flash.emailSuccess}">
            <div class="alert alert-success text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.emailSuccess}" />
            </div>
          </g:if>
          <g:submitButton class="btn btn-primary" name="updateEmail" value="${message(code:'user.edit.email.button')}" />
        </g:form>
        <g:form action="updatePassword">
          <div>
            <g:field type="password" name="currentPassword" placeholder="${message(code:'user.edit.currentPassword.placeholder')}" />
          </div>
          <div>
            <g:field type="password" name="password" placeholder="${message(code:'user.edit.password.placeholder')}" />
          </div>
          <div>
            <g:field type="password" name="repeatPassword" placeholder="${message(code:'user.repeatPassword.placeholder')}" />
          </div>
          <g:if test="${flash.passwordError}">
            <div class="alert alert-danger text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.passwordError}" />
            </div>
          </g:if>
          <g:if test="${flash.passwordSuccess}">
            <div class="alert alert-success text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.passwordSuccess}" />
            </div>
          </g:if>
          <g:submitButton class="btn btn-primary" name="updatePassword" value="${message(code:'user.edit.password.button')}" />
        </g:form>
        <g:form action="updateName">
          <div>
            <g:field type="text" name="firstName" placeholder="${message(code:'user.edit.firstName.placeholder')}" value="${user?.firstName}" />
          </div>
          <div>
            <g:field type="text" name="lastName" placeholder="${message(code:'user.edit.lastName.placeholder')}" value="${user?.lastName}" />
          </div>
          <g:if test="${flash.nameError}">
            <div class="alert alert-danger text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.nameError}" />
            </div>
          </g:if>
          <g:if test="${flash.nameSuccess}">
            <div class="alert alert-success text-center">
              <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
              <g:message code="${flash.nameSuccess}" />
            </div>
          </g:if>
          <g:submitButton class="btn btn-primary" name="updateName" value="${message(code:'user.edit.name.button')}" />
        </g:form>
      </div>
    </body>
</html>
