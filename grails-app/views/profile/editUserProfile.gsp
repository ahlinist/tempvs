<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - Edit <tempvs:fullName profile="${profile}"/></title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-10">
          <tempvs:ajaxForm controller="profile" action="updateAvatar">
            <tempvs:formField type="file" name="avatarImage" label="userProfile.avatar.label" />
            <tempvs:ajaxSubmitButton value="userProfile.update.avatar.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateProfileEmail">
             <tempvs:formField type="email" name="profileEmail" value="${profile.profileEmail}" label="userProfile.profileEmail.label" />
            <tempvs:ajaxSubmitButton value="userProfileEmail.update.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateUserProfile">
            <tempvs:formField type="text" name="firstName" value="${profile.firstName}" label="userProfile.firstName.label" />
            <tempvs:formField type="text" name="lastName" value="${profile.lastName}" label="userProfile.lastName.label" />
            <tempvs:formField type="text" name="location" value="${profile.location}" label="userProfile.location.label" />
            <tempvs:formField type="text" name="profileId" value="${profile.profileId}" label="userProfile.profileId.label" />
            <tempvs:ajaxSubmitButton value="userProfile.update.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>