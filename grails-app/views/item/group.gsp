<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
      </div>
      <g:if test="${itemGroup}">
        <g:set var="itemGroupId" value="${itemGroup.id}"/>
        <div class="row">
          <div class="row">
            <div class="ajax-form col-sm-6">
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editItemGroupField', name: 'name', value: itemGroup.name, objectId: itemGroupId, label: 'item.group.name.label', editAllowed: editAllowed, mandatory: true]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editItemGroupField', name: 'description', value: itemGroup.description, objectId: itemGroupId, label: 'item.group.description.label', editAllowed: editAllowed]}"/>
            </div>
            <div class="col-sm-6">
              <g:if test="${editAllowed}">
                <div class="pull-right">
                  <g:render template="/common/templates/modalButton"
                      model="${[id: 'deleteGroup-' + itemGroupId, message: 'item.group.delete.button', size: 'modal-sm']}">
                    <g:message code='item.group.deleteConfirmation.text' args="${[itemGroup.name]}"/>
                    <br/>
                    <g:render template="/ajax/templates/ajaxLink"
                        model="${[controller: 'item', action: 'deleteGroup', id: itemGroupId, method: 'DELETE', classes: 'btn btn-default']}">
                      <g:message code="yes"/>
                    </g:render>
                    <button class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </g:render>
                </div>
              </g:if>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-6">
              <b><g:message code="item.items.message"/></b>:
              <g:if test="${items}">
                <ul>
                  <g:each var="item" in="${items}">
                    <g:set var="itemId" value="${item.id}"/>
                    <g:set var="itemName" value="${item.name}"/>
                    <li class="row" id="item-${itemId}">
                      <g:link class="btn btn-default col-sm-4" controller="item" action="show" id="${itemId}"  data-toggle="tooltip" data-placement="bottom" title="${item.description}">
                        ${itemName}
                      </g:link>
                    </li>
                  </g:each>
                </ul>
              </g:if>
              <g:else>
                <i><g:message code="item.no.items.message"/></i>
              </g:else>
            </div>
            <div class="col-sm-6">
              <g:if test="${editAllowed}">
                <div class="pull-right">
                  <g:render template="/common/templates/modalButton"
                      model="${[id: 'itemForm', message: 'item.create.item.button']}">
                    <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'item', action: 'createItem']}">
                      <g:render template="/image/templates/imageUploader"/>
                      <g:render template="/common/templates/formField" model="${[type: 'text', name: 'name', label: 'item.name.label', mandatory: true]}"/>
                      <g:render template="/common/templates/formField" model="${[type: 'text', name: 'description', label: 'item.description.label']}"/>
                      <g:render template="/common/templates/formField"
                          model="${[type: 'select', name: 'itemType', label: 'item.itemType.dropdown.label', mandatory: true, from: itemTypes, optionKey: 'key', optionValue: 'value']}"/>
                      <g:render template="/common/templates/formField"
                          model="${[type: 'select', name: 'period', label: 'periodization.period.dropdown.label', mandatory: true, from: periods, optionKey: 'key', optionValue: 'value']}"/>
                      <input type="hidden" name="itemGroup" value="${itemGroupId}"/>
                      <g:render template="/ajax/templates/submitButton">
                        <g:message code="item.create.item.button"/>
                      </g:render>
                    </g:render>
                  </g:render>
                </div>
              </g:if>
            </div>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
