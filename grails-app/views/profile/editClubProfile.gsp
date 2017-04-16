<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - Edit <tempvs:fullName profile="${profile}"/></title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-10">
          <tempvs:ajaxForm controller="image" action="updateAvatar">
            <tempvs:formField type="file" name="avatar" label="clubProfile.avatar.label" />
            <tempvs:ajaxSubmitButton value="clubProfile.update.avatar.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateProfileEmail">
             <tempvs:formField type="email" name="profileEmail" value="${profile.profileEmail}" label="clubProfile.profileEmail.label" />
            <tempvs:ajaxSubmitButton value="clubEmail.update.button" />
          </tempvs:ajaxForm>
          <tempvs:ajaxForm action="updateClubProfile">
            <tempvs:formField type="text" name="firstName" value="${profile.firstName}" label="clubProfile.firstName.label" />
            <tempvs:formField type="text" name="lastName" value="${profile.lastName}" label="clubProfile.lastName.label" />
            <tempvs:formField type="text" name="nickName" value="${profile.nickName}" label="clubProfile.nickName.label" />
            <tempvs:formField type="text" name="clubName" value="${profile.clubName}" label="clubProfile.clubName.label" />
            <tempvs:formField type="text" name="profileId" value="${profile.profileId}" label="clubProfile.profileId.label" />
            <tempvs:ajaxSubmitButton value="clubProfile.update.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>