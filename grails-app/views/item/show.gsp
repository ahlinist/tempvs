<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
    </head>
    <body>
      <g:if test="${item}">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
        <div class="row">
          <b>${item.name}</b>
          <br/>
          <b>${item.description}</b>
          <hr/>
          <div class="col-sm-3">
            <g:message code="item.itemImage.label"/>: <tempvs:image objectId="${item.itemImageId}" collection="item"/>
          </div>
          <div class="col-sm-3">
            <g:message code="item.sourceImage.label"/>: <tempvs:image objectId="${item.sourceImageId}" collection="source"/>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <div class="row">
            <tempvs:modalButton id="itemForm" message="item.updateItem.link">
              <g:render template="/item/templates/itemForm" model="${[action: 'editItem', button: 'item.updateItem.button', item: item]}"/>
            </tempvs:modalButton>
            <tempvs:modalButton id="deleteItem" size="modal-sm" message="item.delete.button">
              <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${item.id}"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.item.notFound.message"/>
      </g:else>
    </body>
</html>
