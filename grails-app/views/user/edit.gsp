<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>title</title>
      <asset:stylesheet src="form.css"/>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-8">
          <tempvs:ajaxForm action="updateEmail">
            <tempvs:formField type="email" name="email" value="${user?.email}" label="user.edit.email.label" />
            <tempvs:ajaxSubmitButton value="user.edit.email.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updatePassword">
            <tempvs:formField type="password" name="currentPassword" label="user.edit.currentPassword.label" />
            <tempvs:formField type="password" name="newPassword" label="user.edit.newPassword.label" />
            <tempvs:formField type="password" name="repeatNewPassword" label="user.edit.repeatNewPassword.label" />
            <tempvs:ajaxSubmitButton value="user.edit.password.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>