<div id="item-list">
  <ul>
    <g:each var="item" in="${items}">
      <g:set var="itemId" value="${item.id}"/>
      <g:set var="itemName" value="${item.name}"/>
      <li class="row" id="item-${itemId}">
        <g:link class="btn btn-default col-sm-4" controller="item" action="show" id="${itemId}"  data-toggle="tooltip" data-placement="bottom" title="${item.description}">${itemName}</g:link>
        <g:if test="${editAllowed}">
          <div class="pull-left">
            <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.delete.button')}">
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'deleteItem' + itemId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                <g:message code='item.deleteConfirmation.text' args="${[itemName]}"/>
                <br/>
                <tempvs:ajaxLink controller="item" action="deleteItem" id="${itemId}" method="DELETE" selector="div#item-list" classes="btn btn-default">
                  <g:message code="yes"/>
                </tempvs:ajaxLink>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </g:render>
            </span>
          </div>
        </g:if>
      </li>
    </g:each>
  </ul>
</div>
