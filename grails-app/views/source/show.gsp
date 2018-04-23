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
            <g:render template="/library/templates/navBar" model="${[period, source]}"/>
          </div>
          <div class="col-sm-4">
            <g:if test="${editAllowed}">
              <div class="pull-right">
                <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.delete.button')}">
                  <g:render template="/common/templates/modalButton"
                      model="${[id: 'deleteSource' + sourceId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                    <g:message code='source.deleteConfirmation.text' args="${[source.name]}"/>
                    <br/>
                    <tempvs:ajaxLink controller="source" action="deleteSource" id="${sourceId}" method="DELETE" classes="btn btn-default">
                      <g:message code="yes"/>
                    </tempvs:ajaxLink>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </g:render>
                </span>
              </div>
            </g:if>
          </div>
        </div>
        <div class="row">
          <div class="col-sm-6">
            <div class="ajax-form">
              <tempvs:ajaxSmartForm type="text" action="editSourceField" name="name" value="${source.name}" objectId="${sourceId}" editAllowed="${canEditFields}" label="source.name.label" mandatory="${true}"/>
              <tempvs:ajaxSmartForm type="text" action="editSourceField" name="description" value="${source.description}" objectId="${sourceId}" editAllowed="${canEditFields}" label="source.description.label"/>
              <tempvs:ajaxSmartForm type="text" value="${source.itemType.value}" label="item.itemType.dropdown.label" editAllowed="${false}"/>
              <tempvs:ajaxSmartForm type="text" value="${source.sourceType.value}" label="source.sourceType.dropdown.label" editAllowed="${false}"/>
              <tempvs:ajaxSmartForm type="text" value="${source.period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
            </div>
            <g:render template="/communication/templates/comments" model="${[controllerName: 'source', object: source, objectId: sourceId]}"/>
          </div>
          <div class="col-sm-6">
            <g:render template="/image/templates/modalCarousel" model="${[objectId: sourceId]}"/>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
