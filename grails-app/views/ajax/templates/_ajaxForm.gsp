<g:form controller="${controller}" action="${action}" class="ajax-form"
    onsubmit="ajaxHandler.processAjaxRequest(this, this.action, new FormData(this), 'POST', '${selector}', ajaxHandler.actions); return false;">
  ${raw(body)}
</g:form>
