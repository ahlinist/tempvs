<div class="row">
  <span class="pull-left">
    <g:link controller="library" action=" ">
      <u>
        <g:message code="library.link"/>
      </u>
    </g:link>
  </span>
  <g:if test="${period}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="library" action="period" id="${period.id}" class="pull-left">
        <u>${period.value}</u>
      </g:link>
    </span>
  </g:if>
  <g:if test="${controllerName == 'source' && actionName == 'show'}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="source" action="show" id="${source.id}" class="pull-left">
        <u>${source.name}</u>
      </g:link>
    </span>
  </g:if>
  <g:if test="${actionName == 'admin'}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="library" action="admin" class="pull-left">
        <u>
          <g:message code="library.admin.nav.link"/>
        </u>
      </g:link>
    </span>
  </g:if>
</div>
