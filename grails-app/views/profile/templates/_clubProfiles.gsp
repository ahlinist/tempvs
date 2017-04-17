<g:if test="${user.clubProfiles}">
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
</g:if>