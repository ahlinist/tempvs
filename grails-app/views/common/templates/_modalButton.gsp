<button class="btn btn-default ${icon} ${classes}" style="${styles}" data-toggle="modal" data-target="#${elementId}">
  <g:message code="${message}"/>
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
