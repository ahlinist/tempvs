<span class="dropdown">
  <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
    <tempvs:fullName profile="${currentProfile}"/>
    <span class="caret"></span>
  </button>
  <ul class="dropdown-menu list-group">
    <li>
      <g:link class="list-group-item disableable" controller="profile" action="switchProfile">
        <tempvs:fullName profile="${user.userProfile}"/>
      </g:link>
    </li>
    <g:each var="clubProfile" in="${user.clubProfiles}">
      <li>
        <g:link class="list-group-item disableable" controller="profile" action="switchProfile" id="${clubProfile.id}">
          <tempvs:fullName profile="${clubProfile}"/>
        </g:link>
      </li>
    </g:each>
  </ul>
</span>
