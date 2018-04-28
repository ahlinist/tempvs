<div id="item-section">
  <g:set var="passportId" value="${passport.id}"/>
  <ul>
    <g:each var="itemEntry" in="${itemMap}">
      <h4><g:message code="item.itemType.${itemEntry.key}.value"/>:</h4>
      <g:each var="item2Passport" in="${itemEntry.value}">
        <g:set var="item" value="${item2Passport.item}"/>
        <g:set var="itemName" value="${item.name}"/>
        <g:set var="quantity" value="${item2Passport.quantity}"/>
        <g:set var="itemId" value="${item.id}"/>
        <li class="row" id="item-${itemId}">
          <span class="pull-left" style="padding:5px;">
            <g:if test="${editAllowed}">
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'passport', action: 'editQuantity', params: [item2PassportId: item2Passport.id, delta: -1], method: 'POST', selector: 'div#item-section', classes: 'glyphicon glyphicon-arrow-down']}"/>
              <b>${quantity}</b>
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'passport', action: 'editQuantity', params: [item2PassportId: item2Passport.id, delta: 1], method: 'POST', selector: 'div#item-section', classes: 'glyphicon glyphicon-arrow-up']}"/>
            </g:if>
            <g:else>
              <b>${quantity}</b>
            </g:else>
          </span>
          <g:link controller="item" action="show" id="${itemId}" class="btn btn-default col-sm-10">
            ${itemName}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.remove.item.button')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'unlinkSource' + itemId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                  <g:message code='passport.removeConfirmation.text' args="${[itemName]}"/>
                  <br/>
                  <g:render template="/ajax/templates/ajaxLink"
                      model="${[controller: 'passport', action: 'removeItem', params: [passportId: passportId, itemId: itemId], method: 'DELETE', selector: 'div#item-section', classes: 'btn btn-default']}">
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
            <g:render template="/ajax/templates/submitButton">
              <g:message code="passport.add.item.button"/>
            </g:render>
          </tempvs:ajaxForm>
        </div>
      </div>
    </div>
  </g:if>
</div>
