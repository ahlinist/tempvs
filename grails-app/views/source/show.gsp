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
          <div class="col-sm-8 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="name" value="${source.name}" objectId="${sourceId}" label="source.name.label" editAllowed="${editAllowed}"/>
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="description" value="${source.description}" objectId="${sourceId}" label="source.description.label" editAllowed="${editAllowed}"/>
            <tempvs:ajaxSmartForm type="text" name="period" value="${source.period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
        </div>
        <div class="row">
          <tempvs:carousel images="${source.images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
