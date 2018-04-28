<div id="linkedSources">
  <h2><g:message code="item.sources.label"/></h2>
  <ul>
    <g:each var="sourceEntry" in="${sourceMap}">
      <h4><g:message code="source.sourceType.${sourceEntry.key.id}.value"/>:</h4>
      <g:each var="source" in="${sourceEntry.value}">
        <g:set var="sourceId" value="${source.id}"/>
        <li class="row" id="source-${sourceId}">
          <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-10">
            ${source.name}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.unlink.button')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'unlinkSource' + sourceId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                  <g:message code='source.unlinkConfirmation.text' args="${[source.name]}"/>
                  <br/>
                  <g:render template="/ajax/templates/ajaxLink"
                      model="${[controller: 'item', action: 'unlinkSource', params: [itemId: itemId, sourceId: source.id], method: 'DELETE', selector: 'div#linkedSources', classes: 'btn btn-default']}">
                    <g:message code="yes"/>
                  </g:render>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </g:render>
              </span>
            </div>
          </g:if>
        </li>
      </g:each>
    </g:each>
    <g:if test="${editAllowed}">
      <div class="panel-group">
        <div class="panel panel-default">
          <div class="panel-heading">
            <h4 class="panel-title">
              <a data-toggle="collapse" href="#sourceCollapse">
                <g:message code="item.add.source.collapse.title"/>
                <span class="caret"></span>
              </a>
            </h4>
          </div>
          <div id="sourceCollapse" class="panel-collapse collapse">
            <li class="row">
              <tempvs:ajaxForm controller="item" action="linkSource" selector="div#linkedSources">
                <g:select class="col-sm-12 tempvs-form-field" name="sourceId" from="${sources}" noSelection="${['':'-']}"
                          optionKey="id" optionValue="name"/>
                <input type="hidden" name="itemId" value="${itemId}" />
                <g:render template="/ajax/templates/submitButton">
                  <g:message code="item.add.source.button"/>
                </g:render>
              </tempvs:ajaxForm>
            </li>
          </div>
        </div>
      </div>
    </g:if>
  </ul>
</div>
