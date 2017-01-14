<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
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
          <g:if test="${createUser}">
            <g:message code="user.create.message"/>
            <tempvs:ajaxForm action="createUser">
              <tempvs:formField type="text" name="firstName" label="user.firstName.label" />
              <tempvs:formField type="text" name="lastName" label="user.lastName.label" />
              <tempvs:ajaxSubmitButton value="user.create.button" />
            </tempvs:ajaxForm>
          </g:if>
        </div>
        <div class="col-sm-3">
        </div>
      </div>
  </body>
</html>
