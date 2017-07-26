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
            <g:render template="/profile/templates/clubProfileForm" model="${[action: 'createClubProfile', button: 'clubProfile.create.button']}"/>
          </tempvs:modalButton>
        </div>
      </div>
    </body>
</html>
