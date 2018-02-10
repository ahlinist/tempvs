<g:if test="${profiles}">
  <ul>
    <g:each var="profile" in="${profiles}">
      <g:set var="profileId" value="${profile.id}"/>
      <g:set var="profileClassName" value="${profile.class.simpleName}"/>
      <li class="row">
        <g:link class="btn btn-default col-sm-12" controller="profile" action="${profileClassName.uncapitalize()}" id="${profileId}">
          ${profile}
        </g:link>
      </li>
    </g:each>
  </ul>
</g:if>
<g:else>
  <span class="col-sm-12 text-center">
    <g:message code="profile.search.noMatches.message"/>
  </span>
</g:else>
