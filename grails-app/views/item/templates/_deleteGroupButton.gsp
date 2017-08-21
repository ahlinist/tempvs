<span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.group.delete.tooltip')}">
  <tempvs:modalButton id="deleteGroup-${itemGroup.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
    <g:message code='item.group.deleteConfirmation.text' args="${[itemGroup.name]}"/>
    <br/>
    <tempvs:ajaxLink message="yes" controller="item" action="deleteGroup" id="${itemGroup.id}" method="DELETE"/>
    <button class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
  </tempvs:modalButton>
</span>
