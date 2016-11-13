<g:set var="userProfile" value="${user.userProfile}" />
<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
      <title>title</title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-8">
          <tempvs:ajaxForm action="updateAvatar">
            <tempvs:formField type="file" name="avatar" label="user.profile.avatar.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.avatar.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateProfileEmail">
             <tempvs:formField type="email" name="profileEmail" value="${userProfile.profileEmail}" label="user.profile.profileEmail.label" />
            <tempvs:ajaxSubmitButton value="user.profileEmail.update.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateUserProfile">
            <tempvs:formField type="text" name="firstName" value="${userProfile.firstName}" label="user.profile.firstName.label" />
            <tempvs:formField type="text" name="lastName" value="${userProfile.lastName}" label="user.profile.lastName.label" />
            <tempvs:formField type="text" name="location" value="${userProfile.location}" label="user.profile.location.label" />
            <tempvs:formField type="text" name="customId" value="${userProfile.customId}" label="user.profile.customId.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
