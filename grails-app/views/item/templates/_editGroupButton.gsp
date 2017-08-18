<span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.group.edit.tooltip')}">
  <tempvs:modalButton id="editGroup-${itemGroup.hashCode()}" classes="glyphicon glyphicon-pencil">
    <g:render template="/item/templates/groupForm" model="${[action: 'editGroup', itemGroup: itemGroup]}"/>
  </tempvs:modalButton>
</span>
