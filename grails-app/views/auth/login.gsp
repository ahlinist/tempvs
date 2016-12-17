<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Login</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <tempvs:ajaxForm action="login">
          <tempvs:formField type="email" name="email" label="user.email.label" />
          <tempvs:formField type="password" name="password" label="user.password.label" />
          <tempvs:ajaxSubmitButton value="user.login.button" />
        </tempvs:ajaxForm>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
