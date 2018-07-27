<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - <g:message code="following.show.page.title"/></title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-6">
        <g:if test="${followedProfiles || newFollowedProfiles}">
          <g:if test="${newFollowedProfiles}">
            <b><g:message code="following.newFollowings.label"/></b>
            <ul>
              <g:each var="profile" in="${newFollowedProfiles}">
                <g:set var="profileId" value="${profile.id}"/>
                <li class="row" id="followingProfile-${profileId}">
                  <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${profileId}" style="background-color: #99FFCC;">
                    ${profile}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </g:if>
          <g:if test="${followedProfiles}">
            <b><g:message code="following.followings.label"/></b>
            <ul>
              <g:each var="profile" in="${followedProfiles}">
                <g:set var="profileId" value="${profile?.id}"/>
                <li class="row" id="followingProfile-${profileId}">
                  <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${profileId}">
                    ${profile}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </g:if>
        </g:if>
        <g:else>
          <i>
            <g:message code="following.followings.empty.message"/>
          </i>
        </g:else>
      </div>
      <div class="col-sm-6">
        <g:if test="${followerProfiles || newFollowerProfiles}">
          <g:if test="${newFollowerProfiles}">
            <b><g:message code="following.newFollowers.label"/></b>
            <ul>
              <g:each var="profile" in="${newFollowerProfiles}">
                <g:set var="profileId" value="${profile.id}"/>
                <li class="row" id="followingProfile-${profileId}">
                  <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${profileId}" style="background-color: #99FFCC;">
                    ${profile}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </g:if>
          <g:if test="${followerProfiles}">
            <b><g:message code="following.followers.label"/></b>
            <ul>
              <g:each var="profile" in="${followerProfiles}">
                <g:set var="profileId" value="${profile.id}"/>
                <li class="row" id="followingProfile-${profileId}">
                  <g:link class="btn btn-default col-sm-10" controller="profile" action="show" id="${profileId}">
                    ${profile}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </g:if>
        </g:if>
        <g:else>
          <i>
            <g:message code="following.followers.empty.message"/>
          </i>
        </g:else>
      </div>
    </div>
  </body>
</html>
