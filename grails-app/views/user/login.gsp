<!DOCTYPE html>
<html>
  <head>
    <title>Tempvs - Login</title>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="row">
              <g:form uri="/login/authenticate" method="POST">
                <div class="col-sm-6">
                  <label for="username"><g:message code="user.email.label" /></label>
                </div>
                <div>
                  <g:field class="col-sm-6" type="email" name="username" value="${user?.email}" />
                </div>
                <div class="col-sm-6">
                  <label for="password"><g:message code="user.password.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="password" value="${user?.password}" />
                </div>
                <div class="col-sm-6">
                  <label for="remember-me"><g:message code="user.login.rememberMe.label" /></label>
                </div>
                <div class="pull-right">
                  <g:checkBox name="remember-me"/>
                </div>
              <g:submitButton class="col-sm-12" name="login" value="${message(code:'user.login.button')}"/>
              </g:form>
              <g:if test="${loginFailed}">
                <div class="alert alert-danger text-center">
                  <g:message code="${loginFailed}" />
                </div>
              </g:if>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
