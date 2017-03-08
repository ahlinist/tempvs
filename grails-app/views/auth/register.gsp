<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Request registration</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <tempvs:ajaxForm action="register">
          <tempvs:formField type="email" name="email" label="auth.email.label" />
          <tempvs:ajaxSubmitButton value="auth.request.registration.button" />
        </tempvs:ajaxForm>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
