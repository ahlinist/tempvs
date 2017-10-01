<g:set var="clubProfiles" value="${user.clubProfiles}"/>
<g:if test="${clubProfiles}">
  <label>
    <g:message code="clubProfile.list.message"/>
  </label>
  <ul class="list-group">
    <g:each var="clubProfile" in="${clubProfiles}">
      <li>
        <g:link class="list-group-item" controller="profile" action="clubProfile" id="${clubProfile.id}">
          ${clubProfile}
        </g:link>
      </li>
    </g:each>
  </ul>
</g:if>
