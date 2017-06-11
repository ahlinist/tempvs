<g:set var="itemGroup" value="${item?.itemGroup}"/>
<g:set var="itemStash" value="${itemGroup?.itemStash}"/>
<g:set var="userProfile" value="${itemStash?.user?.userProfile}"/>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
    </head>
    <body>
      <g:if test="${item}">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, itemStash, userProfile]}"/>
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
        <g:if test="${ownItem}">
          <div class="row">
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#itemForm">
              <g:message code="item.updateItem.link"/>
            </button>
            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#deleteItem">
              <g:message code="item.delete.button"/>
            </button>

            <g:render template="/templates/modal" model="${[modalId: 'itemForm']}">
              <g:render template="/item/templates/itemForm" model="${[action: 'editItem', button: 'item.updateItem.button', item: item]}"/>
            </g:render>
            <g:render template="/templates/modal" model="${[modalId: 'deleteItem', size: 'modal-sm']}">
              <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${item.id}"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </g:render>
          </div>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.item.notFound.message"/>
      </g:else>
    </body>
</html>
