<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - ${userProfile}</title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-1">
      </div>
      <div class="col-sm-4">
        <label>
          <g:message code="userProfile.list.message"/>
        </label>
        <g:link class="list-group-item" controller="profile" action="user" id="${userProfile.id}">
          ${userProfile}
        </g:link>
      </div>
      <div class="col-sm-1">
      </div>
      <div class="col-sm-5">
        <g:render template="/profile/templates/clubProfileList"/>
      </div>
      <div class="col-sm-1">
      </div>
    </div>
  </body>
</html>
