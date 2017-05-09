<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.stash.title"/></title>
    </head>
    <body>
      <g:if test="${itemStash}">
        <div class="col-sm-3">
          <g:render template="/item/templates/itemGroups" model="${[itemStash: itemStash]}"/>
          <div>
            <g:link class="btn btn-default" controller="item" action="createGroup">
              <g:message code="item.createGroup.link"/>
            </g:link>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>
