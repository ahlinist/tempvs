<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="library.admin.title"/></title>
    </head>
    <body>
      <div class="row">
        <g:render template="/library/templates/navBar"/>
      </div>
      <h1><g:message code="library.admin.header"/></h1>
      <div class="row">
        <div class="col-sm-2"></div>
        <div class="col-sm-8">
          <h2><g:message code="library.admin.request.list"/></h2>
          <table class="table table-condensed">
            <thead>
              <tr>
                <th><g:message code="library.admin.request.user.label"/></th>
                <th><g:message code="library.admin.request.authority.label"/></th>
                <th><g:message code="library.admin.request.actions.label"/></th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${roleRequests}" var="roleRequest">
                <g:set var="userProfile" value="${roleRequest.user.userProfile}"/>
                <g:set var="roleRequestId" value="${roleRequest.id}"/>
                <tr id="roleRequest-${roleRequestId}">
                  <td>
                    <g:link class="btn btn-default" controller="profile" action="user" id="${userProfile.id}">
                      ${userProfile}
                    </g:link>
                  </td>
                  <td>
                    <g:message code="library.role.${roleRequest.role.authority}"/>
                  </td>
                  <td>
                    <g:render template="/common/templates/modalButton"
                        model="${[elementId: 'roleRequestId' + roleRequestId, size: 'modal-sm', icon: 'glyphicon glyphicon-ok', styles: 'color: green; border-radius: 150px !important;']}">
                      <g:message code='library.roleRequest.approve.text'/>
                      <br/>
                      <g:render template="/ajax/templates/ajaxLink"
                          model="${[controller: 'library', action: 'approveRoleRequest', id: roleRequestId, method: 'POST', selector: 'tr#roleRequest-' + roleRequestId, classes: 'btn btn-default']}">
                        <g:message code="yes"/>
                      </g:render>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </g:render>
                    <g:render template="/common/templates/modalButton"
                        model="${[elementId: 'roleRequestId' + roleRequestId, size: 'modal-sm', icon: 'glyphicon glyphicon-remove', styles: 'color: red; border-radius: 150px !important;']}">
                      <g:message code='library.roleRequest.reject.text'/>
                      <br/>
                      <g:render template="/ajax/templates/ajaxLink"
                          model="${[controller: 'library', action: 'rejectRoleRequest', id: roleRequestId, method: 'DELETE', selector: 'tr#roleRequest-' + roleRequestId, classes: 'btn btn-default']}">
                        <g:message code="yes"/>
                      </g:render>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </g:render>
                  </td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </div>
        <div class="col-sm-2"></div>
      </div>
    </body>
</html>
