<g:set var="url" value="${g.createLink(controller: controller, action: action, id: id, params: params)}"/>
<span onclick="ajaxHandler.processAjaxRequest(this, '${url}', '', '${method}', '${selector}', ajaxHandler.actions);">
  <span class="${classes} submit-button">
    ${raw(body)}
  </span>
</span>
