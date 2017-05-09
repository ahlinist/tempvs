<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <g:if test="${itemGroup}">
        <div class="col-sm-3">
          <b>${itemGroup.name}</b>
          <br/>
          <b>${itemGroup.description}</b>
          <hr/>
          <g:link class="btn btn-default disabled" controller="item" action="createItem">
            <g:message code="item.createItem.link"/>
          </g:link>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
