<div class="row">
  <div class="col-sm-6">
    <label for="${name}"><g:message code="${label}" /></label>
  </div>
  <div class="col-sm-6">
    <g:if test="${type == 'select'}">
      <g:select class="col-sm-12 tempvs-form-field" name="${name}" from="${from}" value="${value}" optionKey="${optionKey}" optionValue="${optionValue}" />
    </g:if>
    <g:else>
      <g:field class="col-sm-12 tempvs-form-field" type="${type}" name="${name}" value="${value}"/>
    </g:else>
  </div>
</div>
