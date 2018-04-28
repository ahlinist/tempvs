<div id="passport-list">
  <g:if test="${passports}">
    <ul>
      <g:each var="passport" in="${passports}">
        <g:set var="passportId" value="${passport.id}"/>
        <g:set var="passportName" value="${passport.name}"/>
        <li class="row" id="passport-${passportId}">
          <g:link class="btn btn-default col-sm-10" controller="passport" action="show" id="${passportId}"  data-toggle="tooltip" data-placement="bottom" title="${passport.description}">
            ${passportName}
          </g:link>
          <g:if test="${editAllowed}">
            <div class="pull-left">
              <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.delete.button')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'deletePassport' + passportId, size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                  <g:message code='passport.deleteConfirmation.text' args="${[passportName]}"/>
                  <br/>
                  <g:render template="/ajax/templates/ajaxLink"
                      model="${[controller: 'passport', action: 'deletePassport', id: passportId, method: 'DELETE', selector: 'div#passport-list', classes: 'btn btn-default']}">
                    <g:message code="yes"/>
                  </g:render>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </g:render>
              </span>
            </div>
          </g:if>
        </li>
      </g:each>
    </ul>
  </g:if>
</div>
