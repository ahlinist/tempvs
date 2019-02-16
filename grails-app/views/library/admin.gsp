<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - <g:message code="library.admin.title"/></title>
      <script>
        window.onload = function() {
          library.adminPage();
        };
      </script>
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
          <tbody id="requests-section">
          </tbody>
          <template class="request-template">
            <tr>
              <td>
                <a class="btn btn-default user"></a>
              </td>
              <td>
                <span class="authority"></span>
              </td>
              <td>
                <span class="btn btn-default accept-request" style="color: green; border-radius: 150px !important;">
                  <span class="glyphicon glyphicon-ok"></span>
                </span>
                <span class="btn btn-default reject-request" style="color: red; border-radius: 150px !important;">
                  <span class="glyphicon glyphicon-remove"></span>
                </span>
              </td>
            </tr>
          </template>
        </table>
      </div>
      <div class="col-sm-2"></div>
    </div>
  </body>
</html>
