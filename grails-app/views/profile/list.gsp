<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${userProfile}</title>
    </head>
    <body>
      <div class="col-sm-3">
        <g:render template="/profile/templates/listedUserProfile" model="${[userProfile: userProfile]}"/>
        <g:render template="/profile/templates/listedClubProfiles" model="${[clubProfiles: clubProfiles]}"/>
        <div>
          <tempvs:modalButton id="createProfile" message="clubProfile.create.link">
            <tempvs:ajaxForm action="createClubProfile">
              <tempvs:formField type="file" name="imageUploadBean.image" label="profile.avatar.label"/>
              <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="profile.avatarInfo.label"/>
              <tempvs:formField type="text" name="firstName" label="profile.firstName.label"/>
              <tempvs:formField type="text" name="lastName" label="profile.lastName.label"/>
              <tempvs:formField type="text" name="location" label="profile.location.label"/>
              <tempvs:formField type="text" name="profileId" label="profile.profileId.label"/>
              <tempvs:formField type="text" name="nickName" label="profile.nickName.label"/>
              <tempvs:formField type="text" name="clubName" label="profile.clubName.label"/>
              <tempvs:formField type="select" name="period" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
              <tempvs:ajaxSubmitButton value="clubProfile.create.button"/>
            </tempvs:ajaxForm>
          </tempvs:modalButton>
        </div>
      </div>
    </body>
</html>
