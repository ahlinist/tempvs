<button type="button" class="btn btn-default ${cls}" data-toggle="modal" data-target="#${id}">
  <g:message code="${message}"/>
</button>

<div id="${id}" class="modal fade" role="dialog">
  <div class="modal-dialog ${size}">
    <div class="modal-content">
      <div class="modal-body">
        ${raw(body)}
      </div>
    </div>
  </div>
</div>
