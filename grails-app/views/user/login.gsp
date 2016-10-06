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
                <tempvs:formField type="email" name="username" label="user.email.label" />
                <tempvs:formField type="password" name="password" label="user.password.label" />
                <tempvs:formField type="checkbox" name="remember-me" label="user.login.rememberMe.label" />
                <g:submitButton class="col-sm-12" name="login" value="${message(code:'user.login.button')}"/>
              </g:form>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
