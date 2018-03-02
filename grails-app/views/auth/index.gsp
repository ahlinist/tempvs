<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Auth</title>
  </head>
  <body>
    <div class="container" style="margin-top: 7em;">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="text-center">
          <h1><g:message code="auth.header.message"/></h1>
          <br/>
          <g:message code="auth.greetings.message"/>
          <br/>
          <g:message code="auth.enlist.message.start"/>
          <tempvs:modalButton id="loginForm" message="${g.message(code: 'auth.login.signup.button')}">
            <g:render template="/auth/templates/loginForm"/>
          </tempvs:modalButton>
          <g:message code="auth.enlist.message.end"/>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
