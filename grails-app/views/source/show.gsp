<sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
  <g:set var="isContributor" value="${true}"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_SCRIBE">
  <g:set var="isScribe" value="${true}"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_ARCHIVARIUS">
  <g:set var="isArchivarius" value="${true}"/>
</sec:ifAnyGranted>

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
          <g:render template="/library/templates/navBar" model="${[period, source]}"/>
        </div>
        <div class="row">
          <div class="col-sm-4">
            <div class="ajax-form">
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editSourceField', name: 'name', value: source.name, objectId: sourceId, label: 'source.name.label', editAllowed: isScribe, mandatory: true]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editSourceField', name: 'description', value: source.description, objectId: sourceId, label: 'source.description.label', editAllowed: isScribe]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', value: source.itemType.value, label: 'item.itemType.dropdown.label', editAllowed: false]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', value: source.sourceType.value, label: 'source.sourceType.dropdown.label', editAllowed: false]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', value: source.period.value, label: 'periodization.period.dropdown.label', editAllowed: false]}"/>
            </div>
            <div class="row">
              <g:if test="${isArchivarius}">
                <div class="pull-left">
                  <g:render template="/common/templates/modalButton"
                      model="${[id: 'deleteSource' + sourceId, size: 'modal-sm', message: 'source.delete.button']}">
                    <g:message code='source.deleteConfirmation.text' args="${[source.name]}"/>
                    <br/>
                    <g:render template="/ajax/templates/ajaxLink"
                        model="${[controller: 'source', action: 'deleteSource', id: sourceId, method: 'DELETE', classes: 'btn btn-default']}">
                      <g:message code="yes"/>
                    </g:render>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </g:render>
                </div>
              </g:if>
            </div>
          </div>
          <div class="col-sm-4">
            <g:render template="/image/templates/modalCarousel" model="${[objectId: sourceId, addingAllowed: isContributor, deletingAllowed: isScribe]}"/>
          </div>
          <div class="col-sm-4">
            <g:render template="/communication/templates/comments" model="${[controllerName: 'source', object: source, objectId: sourceId]}"/>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
