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
            <tempvs:ajaxLink message="item.delete.button" controller="item" action="deleteItem" id="${item.id}"/>
          </div>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.item.notFound.message"/>
      </g:else>
    </body>
</html>
