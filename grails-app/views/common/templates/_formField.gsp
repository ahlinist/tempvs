<div class="row ${name}-wrapper ${classes}" style="${styles}">
  <div class="col-sm-6">
    <label for="${name}"><g:message code="${label}" /></label>
  </div>
  <div class="col-sm-6">
    <g:if test="${type == 'select'}">
      <g:select class="col-sm-12 tempvs-form-field" name="${name}" from="${from}" value="${value}" noSelection="${['':'-']}"
          optionKey="${optionKey}" optionValue="${optionValue}" disabled="${disabled}"/>
    </g:if>
    <g:elseif test="${type == 'checkbox'}">
      <g:checkBox class="pull-left tempvs-form-field" name="${name}" value="${value}" disabled="${disabled}"/>
    </g:elseif>
    <g:else>
      <g:field class="col-sm-12 tempvs-form-field" type="${type}" name="${name}" value="${value}" disabled="${disabled}"/>
    </g:else>
  </div>
</div>
