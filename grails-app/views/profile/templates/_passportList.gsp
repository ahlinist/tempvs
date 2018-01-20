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
                <tempvs:modalButton id="deletePassport-${passport.id}" size="modal-sm" classes="glyphicon glyphicon-trash">
                  <g:message code='passport.deleteConfirmation.text' args="${[passportName]}"/>
                  <br/>
                  <tempvs:ajaxLink controller="passport" action="deletePassport" id="${passportId}" method="DELETE" selector="div#passport-list">
                    <g:message code="yes"/>
                  </tempvs:ajaxLink>
                  <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                </tempvs:modalButton>
              </span>
            </div>
          </g:if>
        </li>
      </g:each>
    </ul>
  </g:if>
</div>
