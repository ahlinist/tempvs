<g:set var="profile" value="${user.userProfile}" />

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - Edit ${profile.firstName} ${profile.lastName}</title>
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
          <tempvs:ajaxForm controller="image" action="updateAvatar">
            <tempvs:formField type="file" name="avatar" label="user.profile.avatar.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.avatar.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateProfileEmail">
             <tempvs:formField type="email" name="profileEmail" value="${profile.profileEmail}" label="user.profile.profileEmail.label" />
            <tempvs:ajaxSubmitButton value="user.profileEmail.update.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateUserProfile">
            <tempvs:formField type="text" name="firstName" value="${profile.firstName}" label="user.profile.firstName.label" />
            <tempvs:formField type="text" name="lastName" value="${profile.lastName}" label="user.profile.lastName.label" />
            <tempvs:formField type="text" name="location" value="${profile.location}" label="user.profile.location.label" />
            <tempvs:formField type="text" name="customId" value="${profile.customId}" label="user.profile.customId.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>