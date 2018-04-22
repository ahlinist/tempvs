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
              <tempvs:modalButton id="itemForm" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm action="createItem">
                  <g:render template="/image/templates/imageUploader"/>
                  <tempvs:formField type="text" name="name" label="item.name.label" mandatory="${true}"/>
                  <tempvs:formField type="text" name="description" label="item.description.label" />
                  <tempvs:formField type="select" name="itemType" from="${itemTypes}" optionKey="key" optionValue="value" label="item.itemType.dropdown.label" mandatory="${true}"/>
                  <tempvs:formField type="select" name="period" from="${periods}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label" mandatory="${true}"/>
                  <input type="hidden" name="itemGroup" value="${itemGroupId}"/>
                  <tempvs:ajaxSubmitButton value="item.createItem.button"/>
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </span>
          </span>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
