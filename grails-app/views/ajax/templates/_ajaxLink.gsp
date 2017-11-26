<span onclick="sendAjaxRequest(this, '${g.createLink(controller: controller, action: action, id: id, params: params)}', '${method}', '${selector}');">
  <span class="btn btn-default submit-button">
    <g:message code="${g.message(code: message)}" />
  </span>
  <asset:image class="ajaxSpinner" style="display: none" src="spinner.gif" />
</span>
