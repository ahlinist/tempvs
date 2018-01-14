<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Passport</title>
  </head>
  <body>
    <div class="row">
        <g:render template="/passport/templates/navBar"/>
    </div>
    <g:if test="${passport}">
      <g:set var="passportId" value="${passport.id}"/>
      <div class="row">
        <div class="col-sm-6 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editPassportField" name="name" value="${passport.name}" objectId="${passportId}" label="passport.name.label"/>
          <tempvs:ajaxSmartForm type="text" action="editPassportField" name="description" value="${passport.description}" objectId="${passportId}" label="passport.description.label"/>
        </div>
        <div class="col-sm-6">
          <g:render template="/passport/templates/itemSection"/>
        </div>
      </div>
    </g:if>
    <g:else>
      <g:message code="${notFoundMessage}" args="${[id]}" />
    </g:else>
  </body>
</html>
