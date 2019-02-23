<div class="row">
  <span class="pull-left">
    <g:link controller="library" action=" ">
      <u>
        <g:message code="library.link"/>
      </u>
    </g:link>
  </span>
  <g:if test="${controllerName == 'source' && actionName == 'show'}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="source" action="show" id="${source.id}" class="pull-left">
        <u>${source.name}</u>
      </g:link>
    </span>
  </g:if>
</div>
