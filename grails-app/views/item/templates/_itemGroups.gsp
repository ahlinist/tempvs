<g:if test="${itemGroups}">
  <h1>
    <g:message code="item.group.list.message"/>
  </h1>
  <ul class="list-group">
    <g:each var="itemGroup" in="${itemGroups}">
      <li>
        <g:link class="list-group-item" controller="item" action="group" id="${itemGroup.id}">
          <b>${itemGroup.name}</b>
          <hr/>
          ${itemGroup.description}
        </g:link>
      </li>
    </g:each>
  </ul>
</g:if>
