<g:set var="userProfile" value="${user.userProfile}" />
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
          <g:form action="updateAvatar" class="ajax-form">
            <tempvs:formField type="file" name="avatar" label="user.profile.avatar.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.avatar.button" />
          </g:form>
          <g:form action="updateUserProfile" onsubmit="sendAjaxRequest(this); return false;">
            <tempvs:formField type="text" name="firstName" value="${userProfile.firstName}" label="user.profile.firstName.label" />
            <tempvs:formField type="text" name="lastName" value="${userProfile.lastName}" label="user.profile.lastName.label" />
            <tempvs:formField type="email" name="profileEmail" value="${userProfile.profileEmail}" label="user.profile.profileEmail.label" />
            <tempvs:formField type="text" name="location" value="${userProfile.location}" label="user.profile.location.label" />
            <tempvs:formField type="text" name="customId" value="${userProfile.customId}" label="user.profile.customId.label" />
            <tempvs:ajaxSubmitButton value="user.profile.update.button" />
          </g:form>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
