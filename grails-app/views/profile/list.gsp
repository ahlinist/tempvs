<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <tempvs:fullName profile="${user.userProfile}"/></title>
    </head>
    <body>
      <div class="col-sm-3">
        <label>
          <g:message code="userProfile.list.message"/>
        </label>
        <g:link class="list-group-item" controller="profile" action="userProfile" id="${user.userProfile.id}">
          <tempvs:fullName profile="${user.userProfile}"/>
        </g:link>
        <g:render template="/profile/templates/clubProfiles" model="${[user: user]}"/>
        <div>
          <tempvs:modalButton id="createProfile" message="clubProfile.create.link">
            <g:render template="/profile/templates/clubProfileForm" model="${[action: 'createClubProfile', button: 'clubProfile.create.button']}"/>
          </tempvs:modalButton>
        </div>
      </div>
    </body>
</html>
