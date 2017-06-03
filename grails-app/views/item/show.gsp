<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.show.title"/></title>
    </head>
    <body>
      <g:if test="${item}">
        <g:render template="/item/templates/navBar" model="${item}"/>
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
        <div class="row">
          <g:link class="btn btn-default" action="deleteItem" id="${item.id}">
            <g:message code="item.delete.button"/>
          </g:link>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.item.notFound.message"/>
      </g:else>
    </body>
</html>
