<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
    </div>
    <g:if test="${item}">
      <g:set var="itemId" value="${item.id}"/>
      <g:set var="period" value="${item.period}"/>
      <div class="row">
        <g:if test="${editAllowed}">
          <div class="pull-right">
            <g:render template="/common/templates/modalButton"
                model="${[id: 'deleteItem' + itemId, size: 'modal-sm', message: 'item.delete.button']}">
              <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
              <br/>
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'item', action: 'deleteItem', id: itemId, method: 'DELETE', classes: 'btn btn-default']}">
                <g:message code="yes"/>
              </g:render>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </g:render>
          </div>
        </g:if>
        <div class="col-sm-6">
          <div class="ajax-form">
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editItemField', name: 'name', value: item.name, objectId: itemId, label: 'item.name.label', editAllowed: editAllowed, mandatory: true]}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editItemField', name: 'description', value: item.description, objectId: itemId, label: 'item.description.label', editAllowed: editAllowed]}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', value: item.itemType.value, label: 'item.itemType.dropdown.label', editAllowed: false]}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', value: period.value, label: 'periodization.period.dropdown.label', editAllowed: false]}"/>
          </div>
          <g:render template="/item/templates/linkedSources" model="${[itemId: itemId]}"/>
        </div>
        <div class="col-sm-6">
          <g:render template="/image/templates/modalCarousel"
              model="${[objectId: itemId, addingAllowed: editAllowed, deletingAllowed: editAllowed]}"/>
          <g:render template="/communication/templates/comments" model="${[controllerName: 'item', object: item, objectId: itemId]}"/>
        </div>
      </div>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
