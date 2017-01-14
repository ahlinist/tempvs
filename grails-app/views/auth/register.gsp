<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Register</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <tempvs:ajaxForm action="register">
          <tempvs:formField type="email" name="email" label="user.email.label" />
          <tempvs:formField type="password" name="password" label="user.password.label" />
          <tempvs:formField type="password" name="repeatPassword" label="user.repeatPassword.label" />
          <tempvs:ajaxSubmitButton value="user.register.button" />
        </tempvs:ajaxForm>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
