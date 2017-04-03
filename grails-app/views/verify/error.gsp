<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Email verification</title>
  </head>
  <body>
      <div class="container">
        <div class="col-sm-1">
        </div>
        <div class="col-sm-8">
          <g:if test="${message}">
            <div class="alert alert-danger"><g:message code="${message}"/></div>
          </g:if>
        </div>
        <div class="col-sm-3">
        </div>
      </div>
  </body>
</html>
