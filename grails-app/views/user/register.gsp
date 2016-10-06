<!DOCTYPE html>
<html>
  <head>
    <title>Tempvs - Register</title>
    <meta name="layout" content="main"/>
    <asset:stylesheet src="form.css"/>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <g:form action="register" class="ajax-form">
          <tempvs:formField type="email" name="email" label="user.email.label" />
          <tempvs:formField type="password" name="password" label="user.password.label" />
          <tempvs:formField type="password" name="repeatPassword" label="user.repeatPassword.label" />
          <tempvs:formField type="text" name="firstName" label="user.firstName.label" />
          <tempvs:formField type="text" name="lastName" label="user.lastName.label" />
          <tempvs:ajaxSubmitButton value="user.register.button" />
        </g:form>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
