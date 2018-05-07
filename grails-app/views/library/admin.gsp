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
                <tr>
                  <td>
                    <g:link class="btn btn-default" controller="profile" action="user" id="${userProfile.id}">
                      ${userProfile}
                    </g:link>
                  </td>
                  <td>
                    <g:message code="library.role.${roleRequest.role.authority}"/>
                  </td>
                  <td>
                    <span class="btn btn-default glyphicon glyphicon-ok" style="color: green; border-radius: 150px !important;"></span>
                    <span class="btn btn-default glyphicon glyphicon-remove" style="color: red; border-radius: 150px !important;"></span>
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
