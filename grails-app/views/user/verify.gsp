<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="location" content="/${controllerName}/${actionName}" />
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <title>Tempvs - Email verification</title>
  </head>
  <body>
      <div class="container">
        <div class="col-sm-3">
        </div>
        <div class="col-sm-6">
          <g:if test="${message}">
            <div class="alert alert-danger"><g:message code="${message}"/></div>
          </g:if>
        </div>
        <div class="col-sm-3">
        </div>
      </div>
  </body>
</html>
