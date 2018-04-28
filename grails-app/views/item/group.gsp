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
            <div class="ajax-form">
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="name" value="${itemGroup.name}" objectId="${itemGroupId}" label="item.group.name.label" editAllowed="${editAllowed}" mandatory="${true}"/>
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="description" value="${itemGroup.description}" objectId="${itemGroupId}" label="item.group.description.label" editAllowed="${editAllowed}"/>
            </div>
          </div>
          <div class="row">
            <b><g:message code="item.items.message"/></b>:
            <g:render template="/item/templates/itemList"/>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <span class="row">
            <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'item.createItem.tooltip')}">
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'itemForm', icon: 'glyphicon glyphicon-plus']}">
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
                    <g:message code="item.createItem.button"/>
                  </g:render>
                </g:render>
              </g:render>
            </span>
          </span>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
