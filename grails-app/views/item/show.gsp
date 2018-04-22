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
        <div class="col-sm-6">
          <div class="ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editItemField" name="name" value="${item.name}" objectId="${itemId}" label="item.name.label" editAllowed="${editAllowed}" mandatory="${true}"/>
            <tempvs:ajaxSmartForm type="text" action="editItemField" name="description" value="${item.description}" objectId="${itemId}" label="item.description.label" editAllowed="${editAllowed}"/>
            <tempvs:ajaxSmartForm type="text" value="${item.itemType.value}" label="item.itemType.dropdown.label" editAllowed="${false}"/>
            <tempvs:ajaxSmartForm type="text" value="${period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
          <g:render template="/item/templates/linkedSources" model="${[itemId: itemId]}"/>
        </div>
        <div class="col-sm-6">
          <g:render template="/image/templates/modalCarousel" model="${[objectId: itemId]}"/>
          <g:render template="/communication/templates/comments" model="${[controllerName: 'item', object: item, objectId: itemId]}"/>
        </div>
      </div>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
