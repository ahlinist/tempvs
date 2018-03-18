<sec:ifLoggedIn>
  <g:set var="canEditFields" value="${true}"/>
</sec:ifLoggedIn>

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
          </div>
        </div>
        <div class="row">
          <div class="col-sm-6 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="name" value="${source.name}" objectId="${sourceId}" editAllowed="${canEditFields}" label="source.name.label"/>
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="description" value="${source.description}" objectId="${sourceId}" editAllowed="${canEditFields}" label="source.description.label"/>
            <tempvs:ajaxSmartForm type="text" value="${source.type.value}" label="item.type.dropdown.label" editAllowed="${false}"/>
            <tempvs:ajaxSmartForm type="text" value="${source.period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
          <div class="col-sm-6">
            <g:render template="/source/templates/comments"/>
          </div>
        </div>
        <div class="row">
          <g:render template="/image/templates/imageSection" model="${[objectId: sourceId]}"/>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
