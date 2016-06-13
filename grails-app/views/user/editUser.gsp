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
      <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-8">
          <g:form action="updateEmail">
            <div class="row">
              <div class="col-sm-6">
                <label for="email"><g:message code="user.edit.email.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:field class="col-sm-12" type="email" name="email" value="${user?.email}" />
              </div>
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
            <div class="row">
              <div class="col-sm-6">
                <label for="currentPassword"><g:message code="user.edit.currentPassword.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:passwordField class="col-sm-12" name="currentPassword" />
              </div>
              <div class="col-sm-6">
                <label for="password"><g:message code="user.edit.password.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:passwordField class="col-sm-12" name="password" />
              </div>
              <div class="col-sm-6">
                <label for="repeatPassword"><g:message code="user.edit.repeatPassword.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:passwordField class="col-sm-12" name="repeatPassword" />
              </div>
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
            <div class="row">
              <div class="col-sm-6">
                <label for="firstName"><g:message code="user.edit.firstName.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="firstName" value="${user?.firstName}" />
              </div>
              <div class="col-sm-6">
                <label for="lastName"><g:message code="user.edit.lastName.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="lastName" value="${user?.lastName}" />
              </div>
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
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
