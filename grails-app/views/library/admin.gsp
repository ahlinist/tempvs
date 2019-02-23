<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script src="/static/js/library.js"></script>
    <script>
      window.onload = function() {
        library.renderAdminPage();
      };
    </script>
  </head>
  <body>
    <div class="row">
      <span class="pull-left">
        <a href="/library">
          <u id="breadcrumb-library"></u>
        </a>
      </span>
      <span class="pull-left">&nbsp;&gt;&nbsp;</span>
      <span class="pull-left">
        <a href="/library/admin">
          <u id="breadcrumb-admin"></u>
        </a>
      </span>
    </div>
    <h1 id="admin-panel-heading"></h1>
    <div class="row">
      <div class="col-sm-2"></div>
      <div class="col-sm-8">
        <table class="table table-condensed">
          <thead>
            <tr>
              <th id="user-header"></th>
              <th id="authority-header"></th>
              <th id="actions-header"></th>
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
