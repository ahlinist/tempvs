<g:set var="passportId" value="${passport.id}"/>
<div id="item-section">
  <ul>
    <g:each var="itemEntry" in="${itemMap}">
      <h4><g:message code="item.type.${itemEntry.key}.value"/>:</h4>
      <g:each var="item2Passport" in="${itemEntry.value}">
        <g:set var="item" value="${item2Passport.item}"/>
        <g:set var="quantity" value="${item2Passport.quantity}"/>
        <g:set var="itemId" value="${item.id}"/>
        <li class="row" id="item-${itemId}">
          <span class="btn btn-default pull-left" style="cursor:default;">${quantity} x </span>
          <g:link controller="item" action="show" id="${itemId}" class="btn btn-default col-sm-10">
            ${item.name}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.remove.item.button')}">
                <tempvs:modalButton id="unlinkSource-${item.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                  <g:message code='passport.removeConfirmation.text' args="${[item.name]}"/>
                  <br/>
                  <tempvs:ajaxLink controller="passport" action="removeItem" params="${[passportId: passportId, itemId: itemId]}" method="DELETE" selector="div#item-section">
                    <g:message code="yes"/>
                  </tempvs:ajaxLink>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </tempvs:modalButton>
              </span>
            </div>
          </g:if>
        </li>
      </g:each>
    </g:each>
  </ul>
  <g:if test="${editAllowed}">
    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#addItemCollapse">
              <g:message code="passport.addItem.collapse.title"/>
              <span class="caret"></span>
            </a>
          </h4>
        </div>
        <div id="addItemCollapse" class="panel-collapse collapse">
          <tempvs:ajaxForm controller="passport" action="addItem" selector="div#item-section">
            <g:select class="col-sm-12 tempvs-form-field" name="itemId" from="${availableItems}" noSelection="${['':'-']}"
                      optionKey="id" optionValue="name"/>
            <input type="number" name="quantity" value="1" />
            <input type="hidden" name="passportId" value="${passportId}" />
            <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
          </tempvs:ajaxForm>
        </div>
      </div>
    </div>
  </g:if>
</div>
