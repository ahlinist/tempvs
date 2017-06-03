<g:set var="user" value="${applicationContext.userService.currentUser}"/>

<span class="dropdown">
  <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
    <tempvs:fullName profile="${applicationContext.profileHolder.profile}"/>
    <span class="caret"></span>
  </button>
  <ul class="dropdown-menu list-group">
    <li>
      <g:link class="list-group-item disableable" controller="profile" action="switchProfile">
        <tempvs:fullName profile="${user.userProfile}"/>
      </g:link>
    </li>
    <g:each var="profile" in="${user.clubProfiles}">
      <li>
        <g:link class="list-group-item disableable" controller="profile" action="switchProfile" id="${profile.id}">
          <tempvs:fullName profile="${profile}"/>
        </g:link>
      </li>
    </g:each>
  </ul>
</span>
