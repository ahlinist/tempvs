<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - <g:message code="following.show.page.title"/></title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-6">
        <b><g:message code="following.followings.label"/></b>
        <ul>
          <g:each var="profile" in="${followingProfiles}">
            <g:set var="profileId" value="${profile.id}"/>
            <li class="row" id="followingProfile-${profileId}">
              <g:link class="btn btn-default col-sm-10" controller="profile" action="${profile.class.simpleName}" id="${profileId}">
                ${profile}
              </g:link>
            </li>
          </g:each>
        </ul>
      </div>
      <div class="col-sm-6">
        <b><g:message code="following.followers.label"/></b>
        <ul>
          <g:each var="profile" in="${followerProfiles}">
            <g:set var="profileId" value="${profile.id}"/>
            <li class="row" id="followingProfile-${profileId}">
              <g:link class="btn btn-default col-sm-10" controller="profile" action="${profile.class.simpleName}" id="${profileId}">
                ${profile}
              </g:link>
            </li>
          </g:each>
        </ul>
      </div>
    </div>
  </body>
</html>
