<ul id="linkedSources">
  <g:each var="source" in="${sources}">
    <g:set var="sourceId" value="${source.id}"/>
    <li class="row" id="source-${sourceId}">
      <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-10">
        ${source.name}
      </g:link>
      <g:if test="${editAllowed}">
        <div class="pull-left">
          <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.unlink.button')}">
            <tempvs:modalButton id="unlinkSource-${sourceId}" size="modal-sm" classes="glyphicon glyphicon-trash">
              <g:message code='source.unlinkConfirmation.text' args="${[source.name]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="item" action="unlinkSource" params="${[itemId: itemId, sourceId: source.id]}" method="DELETE" selector="ul#linkedSources"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </span>
        </div>
      </g:if>
    </li>
  </g:each>
  <g:if test="${editAllowed}">
    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#sourceCollapse">
              <g:message code="item.linkSource.collapse.title"/>
              <span class="caret"></span>
            </a>
          </h4>
        </div>
        <div id="sourceCollapse" class="panel-collapse collapse">
          <li class="row">
            <tempvs:ajaxForm controller="item" action="linkSource" selector="ul#linkedSources">
              <g:select class="col-sm-12 tempvs-form-field" name="sourceId" from="${availableSources}" noSelection="${['':'-']}"
                        optionKey="id" optionValue="name"/>
              <input type="hidden" name="itemId" value="${itemId}" />
              <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
            </tempvs:ajaxForm>
          </li>
        </div>
      </div>
    </div>
  </g:if>
</ul>
