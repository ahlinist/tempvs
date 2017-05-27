<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.show.title"/></title>
    </head>
    <body>
      <g:if test="${item}">
        <g:render template="/item/templates/navBar" model="${item}"/>
        <div class="col-sm-3">
          <b>${item.name}</b>
          <br/>
          <b>${item.description}</b>
          <br/>
          <g:message code="item.itemImage.label"/>: <tempvs:image objectId="${item.itemImageId}" collection="item"/>
          <g:message code="item.sourceImage.label"/>: <tempvs:image objectId="${item.sourceImageId}" collection="source"/>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.item.notFound.message"/>
      </g:else>
    </body>
</html>
