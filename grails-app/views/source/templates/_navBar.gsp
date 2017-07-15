<div class="row">
  <span class="pull-left">
    <g:link controller="source">
      <u>
        <g:message code="source.library.link"/>
      </u>
    </g:link>
  </span>
  <g:if test="${period}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="source" action="period" id="${period.id}" class="pull-left">
        <u>${period.value}</u>
      </g:link>
    </span>
  </g:if>
  <g:if test="${source}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link action="show" id="${source.id}" class="pull-left">
        <u>${source.name}</u>
      </g:link>
    </span>
  </g:if>
</div>
