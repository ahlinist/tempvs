<g:if test="${profiles}">
  <g:each var="profile" in="${profiles}">
    <li class="row">
      <g:link class="btn btn-default col-sm-12" controller="profile" action="${profile.class.simpleName.uncapitalize()}" id="${profile.id}">
        ${profile}
      </g:link>
    </li>
  </g:each>
</g:if>
<g:else>
  <li class="col-sm-12 text-center">
    <i class="text-secondary"><g:message code="profile.search.noMatches.message"/></i>
  </li>
</g:else>
