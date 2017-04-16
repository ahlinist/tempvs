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
        <label>
          <g:message code="clubProfile.list.message"/>
        </label>
        <ul class="list-group">
          <g:each var="clubProfile" in="${user.clubProfiles}">
            <li>
              <g:link class="list-group-item" controller="profile" action="clubProfile" id="${clubProfile.id}">
                <tempvs:fullName profile="${clubProfile}"/>
              </g:link>
            </li>
          </g:each>
        </ul>
        <div>
          <g:link class="btn btn-default" controller="profile" action="create"><g:message code="clubProfile.create.link"/></g:link>
        </div>
      </div>
    </body>
</html>
