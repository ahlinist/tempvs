<g:each var="itemEntry" in="${itemMap}">
  <h4><g:message code="item.type.${itemEntry.key}.value"/>:</h4>
  <g:each var="item" in="${itemEntry.value}">
    <g:set var="itemId" value="${item.id}"/>
    <li class="row" id="item-${itemId}">
      <g:link controller="item" action="show" id="${itemId}" class="btn btn-default col-sm-11">
        ${item.name}
      </g:link>
      <g:if test="${editAllowed}">
        <div class="pull-left">
          <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.remove.item.button')}">
            <tempvs:modalButton id="unlinkSource-${item.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
              <g:message code='passport.removeConfirmation.text' args="${[item.name]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="passport" action="removeItem" params="${[passportId: passport.id, itemId: itemId]}" method="DELETE" selector="ul#passportItems"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </span>
        </div>
      </g:if>
    </li>
  </g:each>
</g:each>
