<div class="row">
  <div class="col-sm-5">
    <label for="fieldValue"><g:message code="${label}" /></label>
  </div>
  <div class="col-sm-7">
    <g:if test="${editAllowed}">
      <g:form name="${name}-form" action="${action}">
        <p id="${name}-text" class="hovering" style="padding: 8px 15px;">
          ${(type == 'select' ? value?.value : value) ?: '-'}&nbsp;
          <span onclick="smartformHandler.activateInput(this, '${name}', '${type == 'select' ? 'select' : 'input'}');" class="glyphicon glyphicon-pencil"></span>
        </p>
        <span id="${name}-input" class="hidden">
          <g:if test="${type == 'select'}">
            <g:select class="col-sm-12 tempvs-form-field" name="fieldValue" from="${from}" value="${value}" noSelection="${['': '-']}"
                optionKey="${optionKey}" optionValue="${optionValue}" disabled="${disabled}"/>
          </g:if>
          <g:else>
            <g:field class="col-sm-12 tempvs-form-field" type="text" name="fieldValue" value="${value}"/>
          </g:else>
        </span>
        <input type="hidden" name="objectId" value="${objectId}"/>
        <input type="hidden" name="fieldName" value="${name}"/>
      </g:form>
    </g:if>
    <g:else>
      <p id="${name}-text" style="padding: 8px 15px;">
        ${(type == 'select' ? value?.value : value) ?: '-'}
      </p>
    </g:else>
  </div>
</div>
