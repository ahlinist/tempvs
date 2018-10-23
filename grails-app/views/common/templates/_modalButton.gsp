<button class="btn btn-default ${icon} ${classes}" style="${styles}" data-toggle="modal" data-target="#${elementId}">
  <g:if test="${template}">
    <g:render template="${template}"/>
  </g:if>
  <g:else>
    <g:message code="${message}"/>
  </g:else>
</button>

<div id="${elementId}" class="modal fade" role="dialog">
  <div class="modal-dialog ${size}">
    <div class="modal-content">
      <div class="modal-body">
        ${raw(body())}
      </div>
    </div>
  </div>
</div>
