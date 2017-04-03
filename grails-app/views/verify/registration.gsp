<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Register user</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-1">
      </div>
      <div class="col-sm-8">
        <g:message code="user.confirm.registration.message" args="${[email]}"/>
        <br/>
        <g:message code="user.confirm.registration.instructions"/>
        <tempvs:ajaxForm controller="user" action="register">
          <tempvs:formField type="text" name="firstName" label="user.firstName.label" />
          <tempvs:formField type="text" name="lastName" label="user.lastName.label" />
          <tempvs:formField type="password" name="password" label="user.password.label" />
          <tempvs:formField type="password" name="repeatPassword" label="user.repeatPassword.label" />
          <tempvs:ajaxSubmitButton value="user.confirm.registration.button" />
        </tempvs:ajaxForm>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
