<g:set var="itemGroup" value="${itemGroup ?: item?.itemGroup}"/>
<g:set var="itemStash" value="${itemStash ?: itemGroup?.itemStash}"/>
<g:set var="userProfile" value="${itemStash.user.userProfile}"/>
  <div class="row">
  <span class="pull-left"><g:link action="stash" id="${itemStash.id}"><u><g:message code="item.stash.link" args="${[userProfile]}"/></u></g:link></span>
  <g:if test="${itemGroup}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left"><g:link action="group" id="${itemGroup.id}" class="pull-left"><u>${itemGroup.name}</u></g:link></span>
  </g:if>
  <g:if test="${item}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left"><g:link action="show" id="${item.id}" class="pull-left"><u>${item.name}</u></g:link></span>
  </g:if>
</div>
