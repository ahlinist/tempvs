<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Passport</title>
  </head>
  <body>
    <div class="row">
        <g:render template="/passport/templates/navBar"/>
    </div>
    <g:if test="${passport}">
      <g:set var="passportId" value="${passport.id}"/>
      <div class="row">
        <div class="col-sm-4">
          <div class="ajax-form">
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editPassportField', name: 'name', value: passport.name, objectId: passportId, label: 'passport.name.label', mandatory: true]}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editPassportField', name: 'description', value: passport.description, objectId: passportId, label: 'passport.description.label']}"/>
          </div>
          <g:if test="${editAllowed}">
            <div class="row">
              <div class="pull-left">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'deletePassport' + passportId, size: 'modal-sm', message: 'passport.delete.button']}">
                  <g:message code='passport.deleteConfirmation.text' args="${[passport.name]}"/>
                  <br/>
                  <g:render template="/ajax/templates/ajaxLink"
                      model="${[controller: 'passport', action: 'deletePassport', id: passportId, method: 'DELETE', classes: 'btn btn-default']}">
                    <g:message code="yes"/>
                  </g:render>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </g:render>
              </div>
            </div>
          </g:if>
          <g:render template="/communication/templates/comments" model="${[controllerName: 'passport', object: passport, objectId: passportId]}"/>
        </div>
        <div class="col-sm-4">
          <g:render template="/passport/templates/itemSection"/>
        </div>
        <div class="col-sm-4">
          <g:render template="/image/templates/modalCarousel"
              model="${[objectId: passportId, addingAllowed: editAllowed, deletingAllowed: editAllowed]}"/>
        </div>
      </div>
    </g:if>
    <g:else>
      <g:message code="${notFoundMessage}" args="${[id]}" />
    </g:else>
  </body>
</html>
