<g:set var="userProfile" value="${user.userProfile}"/>
<label>
  <g:message code="userProfile.list.message"/>
</label>
<g:link class="list-group-item" controller="profile" action="userProfile" id="${userProfile.id}">
  ${userProfile}
</g:link>
