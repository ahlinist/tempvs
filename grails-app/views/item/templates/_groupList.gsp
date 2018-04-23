<div id="group-list">
  <ul>
    <g:each var="itemGroup" in="${itemGroups.sort { it.id }}">
      <g:set var="itemGroupId" value="${itemGroup.id}"/>
      <g:set var="itemGroupName" value="${itemGroup.name}"/>
      <li id="itemGroup-${itemGroupId}">
        <div class="row">
          <g:link class="btn btn-default col-sm-3" controller="item" action="group" id="${itemGroupId}" data-toggle="tooltip" data-placement="bottom" title="${itemGroup.description}">
              ${itemGroupName}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.group.delete.tooltip')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'deleteGroup-' + itemGroupId, icon: 'glyphicon glyphicon-trash', size: 'modal-sm']}">
                  <g:message code='item.group.deleteConfirmation.text' args="${[itemGroupName]}"/>
                  <br/>
                  <tempvs:ajaxLink controller="item" action="deleteGroup" id="${itemGroupId}" method="DELETE" selector="div#group-list" classes="btn btn-default">
                    <g:message code="yes"/>
                  </tempvs:ajaxLink>
                  <button class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </g:render>
              </span>
            </div>
          </g:if>
        </div>
      </li>
    </g:each>
  </ul>
</div>
