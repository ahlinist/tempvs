<span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.updateItem.link')}">
  <tempvs:modalButton id="itemForm-${item.hashCode()}" classes="glyphicon glyphicon-pencil">
    <g:render template="/item/templates/itemForm" model="${[action: 'editItem', button: 'item.updateItem.button', item: item]}"/>
  </tempvs:modalButton>
</span>
