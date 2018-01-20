<span onclick="sendAjaxRequest(this, '${g.createLink(controller: controller, action: action, id: id, params: params)}', '${method}', '${selector}', getActions());">
  <span class="btn btn-default submit-button">
    ${raw(body)}
  </span>
  <asset:image class="ajaxSpinner" style="display: none" src="spinner.gif" />
</span>
