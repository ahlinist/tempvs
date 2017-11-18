<g:set var="sourceId" value="${source.id}"/>
<g:set var="selector" value="source-${sourceId}"/>
<li class="row" id="${selector}">
  <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-10">
    ${source.name}
  </g:link>
  <g:if test="${editAllowed}">
    <div class="pull-left">
      <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.unlink.button')}">
        <tempvs:modalButton id="unlinkSource-${source.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
          <g:message code='source.unlinkConfirmation.text' args="${[source.name]}"/>
          <br/>
          <tempvs:ajaxLink message="yes" controller="item" action="unlinkSource" params="${[itemId: itemId, sourceId: source.id, selector: '#' + selector]}" method="DELETE"/>
          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
        </tempvs:modalButton>
      </span>
    </div>
  </g:if>
</li>
