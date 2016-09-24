<!DOCTYPE html>
<html>
  <head>
    <title>Tempvs - Register</title>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="row">
              <g:form action="register" onsubmit="sendAjaxRequest(this); return false;">
                <div class="col-sm-6">
                  <label for="email"><g:message code="user.email.label" /></label>
                </div>
                <div>
                  <g:field class="col-sm-6" type="email" name="email" value="${user?.email}" />
                </div>
                <div class="col-sm-6">
                  <label for="password"><g:message code="user.password.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="password" value="${user?.password}" />
                </div>
                <div class="col-sm-6">
                  <label for="repeatPassword"><g:message code="user.repeatPassword.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="repeatPassword" value="${user?.repeatPassword}" />
                </div>
                <div class="col-sm-6">
                  <label for="firstName"><g:message code="user.firstName.label" /></label>
                </div>
                <div>
                  <g:textField class="col-sm-6" name="firstName" value="${user?.firstName}" />
                </div>
                <div class="col-sm-6">
                  <label for="lastName"><g:message code="user.lastName.label" /></label>
                </div>
                <div>
                  <g:textField class="col-sm-6" name="lastName" value="${user?.lastName}" />
                </div>
                <g:render template="../templates/ajaxTools" model="[button: 'user.register.button']" />
              </g:form>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
