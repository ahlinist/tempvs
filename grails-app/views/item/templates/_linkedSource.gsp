<g:set var="sourceId" value="${source.id}"/>
<li class="row" id="source-${sourceId}">
  <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-10">
    ${source.name}
  </g:link>
  <g:if test="${editAllowed}">
    <div class="pull-left">
      <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.unlink.button')}">
        <tempvs:modalButton id="unlinkSource-${source.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
          <g:message code='source.unlinkConfirmation.text' args="${[source.name]}"/>
          <br/>
          <tempvs:ajaxLink message="yes" controller="item" action="unlinkSource" params="${[itemId: itemId, sourceId: source.id]}" method="DELETE" selector="li#source-${sourceId}"/>
          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
        </tempvs:modalButton>
      </span>
    </div>
  </g:if>
</li>
