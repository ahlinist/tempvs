<div id="source-list">
  <g:if test="${sources}">
    <ul>
      <g:each in="${sources}" var="source">
        <g:set var="sourceId" value="${source.id}"/>
        <g:set var="sourceName" value="${source.name}"/>
        <li class="row" id="source-${sourceId}">
          <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-3">
            ${sourceName}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.delete.button')}">
                <tempvs:modalButton id="deleteSource-${source.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                  <g:message code='source.deleteConfirmation.text' args="${[sourceName]}"/>
                  <br/>
                  <tempvs:ajaxLink controller="source" action="deleteSource" id="${sourceId}" params="[period: period.key]" method="DELETE" selector="div#source-list" classes="btn btn-default">
                    <g:message code="yes"/>
                  </tempvs:ajaxLink>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </tempvs:modalButton>
              </span>
            </div>
          </g:if>
        </li>
      </g:each>
    </ul>
  </g:if>
</div>