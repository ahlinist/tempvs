<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${source?.name ?: g.message(code: 'source.show.title')}</title>
    </head>
    <body>
      <g:if test="${source}">
        <g:set var="sourceId" value="${source.id}"/>
        <div class="row">
          <div class="col-sm-8">
            <g:render template="/source/templates/navBar" model="${[period, source]}"/>
          </div>
          <div class="col-sm-4">
            <b><g:message code="actions.label"/></b>:
            <br/>
          </div>
        </div>
        <div class="row">
          <g:render template="/source/templates/source" model="${[source]}"/>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
