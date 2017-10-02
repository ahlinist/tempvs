<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="location" content="/${controllerName}/${actionName}" />
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <title>Tempvs - Auth</title>
  </head>
  <body>
    <div class="container" style="margin-top: 7em;">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="row">
          <g:render template="/auth/templates/loginForm"/>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
