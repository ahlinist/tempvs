<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Email verification error</title>
  </head>
  <body>
      <div class="container">
        <div class="col-sm-1">
        </div>
        <div class="col-sm-8">
          <g:if test="${notFoundMessage}">
            <div class="alert alert-danger"><g:message code="${notFoundMessage}"/></div>
          </g:if>
        </div>
        <div class="col-sm-3">
        </div>
      </div>
  </body>
</html>
