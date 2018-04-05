<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Register user</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <g:message code="user.confirm.registration.message" args="${[emailVerification.email]}"/>
        <tempvs:ajaxForm controller="user" action="register">
          <tempvs:formField type="text" name="firstName" label="profile.firstName.label" mandatory="${true}"/>
          <tempvs:formField type="text" name="lastName" label="profile.lastName.label" mandatory="${true}"/>
          <tempvs:formField type="text" name="profileId" label="profile.profileId.label" />
          <tempvs:formField type="password" name="password" label="user.password.label" mandatory="${true}"/>
          <tempvs:formField type="password" name="confirmPassword" label="user.confirmPassword.label" mandatory="${true}"/>
          <input type="hidden" name="emailVerification" value="${emailVerification.id}"/>
          <tempvs:ajaxSubmitButton value="user.confirm.registration.button" />
        </tempvs:ajaxForm>
        <g:message code="user.confirm.registration.instructions"/>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
