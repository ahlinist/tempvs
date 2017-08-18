<span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.delete.button')}">
  <tempvs:modalButton id="deleteItem-${item.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
    <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
    <br/>
    <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${item.id}"/>
    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
  </tempvs:modalButton>
</span>
