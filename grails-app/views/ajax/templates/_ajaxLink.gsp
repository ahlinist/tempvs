<span onclick="sendAjaxRequest(this, '${g.createLink(controller: controller, action: action, id: id, params: params)}', '${method}', '${selector}', getActions());">
  <span class="${classes} submit-button">
    ${raw(body)}
  </span>
</span>
